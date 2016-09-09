package rtapps.app.catalog.network;

import retrofit.mime.TypedFile;

/**
 * Created by rtichauer on 8/26/16.
 */
public class NewCatalogImage{
    private int index;

    public NewCatalogImage(int index){
        this.index = index;
    }

    public int getIndex(){
        return this.index;
    }

}
