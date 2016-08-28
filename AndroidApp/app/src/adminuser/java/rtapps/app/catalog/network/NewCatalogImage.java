package rtapps.app.catalog.network;

import retrofit.mime.TypedFile;

/**
 * Created by rtichauer on 8/26/16.
 */
public class NewCatalogImage{
    private int index;
    private TypedFile fullImage;

    public NewCatalogImage(int index, TypedFile fullImage){
        this.index = index;
        this.fullImage = fullImage;
    }

    public int getIndex(){
        return this.index;
    }

    public TypedFile getFullImage(){
        return this.fullImage;
    }
}
