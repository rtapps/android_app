package rtapps.app.network.responses;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by rtichauer on 8/20/16.
 */

public class CatalogImage {

    String id;
    String applicationId;
    String fileServerHost;
    String fullImageName;
    int index;

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
