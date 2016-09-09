package rtapps.app.services;

import android.app.IntentService;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit.RestAdapter;
import rtapps.app.config.ApplicationConfigs;
import rtapps.app.config.Configurations;
import rtapps.app.databases.CatalogTable;
import rtapps.app.databases.MessagesTable;
import rtapps.app.infrastructure.BusHolder;
import rtapps.app.network.AppAPI;
import rtapps.app.network.responses.AllMessagesResponse;
import rtapps.app.network.responses.CatalogImage;

/**
 * Created by tazo on 16/08/2016.
 */
public class SyncDataService extends IntentService {

    final AppAPI yourUsersApi = new RestAdapter.Builder()
            .setEndpoint(Configurations.BASE_URL)
            .build().create(AppAPI.class);


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SyncDataService(String name) {
        super(name);
    }

    public SyncDataService() {
        super("Sync All data");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        syncAllMessages();
        syncCatalog();

    }

    private void syncCatalog() {
        CatalogSynchronizer catalogSynchronizer = new CatalogSynchronizer(this);
        catalogSynchronizer.syncCatalog();
    }

    private void syncAllMessages() {
        MessagesSynchronizer messagesSynchronizer = new MessagesSynchronizer(this.getApplicationContext());
        messagesSynchronizer.syncAllMessages();
    }
}
