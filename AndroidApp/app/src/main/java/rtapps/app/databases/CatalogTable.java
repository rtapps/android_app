package rtapps.app.databases;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import rtapps.app.network.responses.CatalogImage;

/**
 * Created by rtichauer on 8/20/16.
 */
@Table(database = MyDatabase.class)
public class CatalogTable extends BaseModel{

    @Column
    @PrimaryKey
    String id;

    @Column
    String applicationId;

    @Column
    String fileServerHost;

    @Column
    String fullImageName;

    @Column
    int index;

    public CatalogTable(){
        super();
    }

    public CatalogTable (CatalogImage catalogImage){
        this.id = catalogImage.getId();
        this.applicationId = catalogImage.getApplicationId();
        this.fileServerHost = catalogImage.getFileServerHost();
        this.fullImageName = catalogImage.getFullImageName();
        this.index = catalogImage.getIndex();
    }


    public String getId() {
        return this.id;
    }

    public String getApplicationId() {
        return this.applicationId;
    }

    public String getFileServerHost() {
        return this.fileServerHost;
    }

    public String getFullImageName() {
        return this.fullImageName;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
