package rtapps.app.services;

import android.content.Context;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import retrofit.RestAdapter;
import rtapps.app.config.ApplicationConfigs;
import rtapps.app.config.Configurations;
import rtapps.app.databases.CatalogTable;
import rtapps.app.infrastructure.BusHolder;
import rtapps.app.network.AppAPI;
import rtapps.app.network.responses.CatalogImage;

/**
 * Created by rtichauer on 9/8/16.
 */
public class CatalogSynchronizer {

    private Context context;

    final AppAPI appAPI = new RestAdapter.Builder()
            .setEndpoint(Configurations.BASE_URL)
            .build().create(AppAPI.class);

    public CatalogSynchronizer(Context context){
        this.context = context;
    }

    public void syncCatalog() {
        List<CatalogImage> catalogImages = appAPI.getCatalog(ApplicationConfigs.getApplicationId());
        Log.d("syncCatalog", "catalogResponse=" + catalogImages);
        SyncDataThreadPool.downloadAndSaveAllCatalogImages(catalogImages, context);

        List<CatalogTable> allCatalogTables = new Select().from(CatalogTable.class).queryList();

        for (CatalogTable catalogTable: allCatalogTables){
            if (!isStillExists(catalogTable, catalogImages)){
                catalogTable.delete();
                //TODO delete the old catalog image
            }
        }
        for (CatalogImage catalogImage: catalogImages){
            CatalogTable newCatalogEntry = new CatalogTable(catalogImage);
            newCatalogEntry.save();
            Log.d("syncCatalog", "new catalog entry: "  + newCatalogEntry);
        }
        BusHolder.get().getBus().post(new CatalogUpdatedEvent());

    }

    private boolean isStillExists(CatalogTable oldCatalogTable, List<CatalogImage> catalogImages) {
        for (CatalogImage catalogImage: catalogImages){
            if (catalogImage.getId().equals(oldCatalogTable.getId())){
                return true;
            }
        }
        return false;
    }
}
