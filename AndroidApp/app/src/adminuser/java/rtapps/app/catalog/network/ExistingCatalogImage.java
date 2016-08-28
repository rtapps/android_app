package rtapps.app.catalog.network;

/**
 * Created by rtichauer on 8/26/16.
 */
public class ExistingCatalogImage {
    private String id;
    private int index;

    public ExistingCatalogImage(String id, int index){
        this.id = id;
        this.index = index;
    }

    public String getId(){
        return this.id;
    }

    public int getIndex(){
        return this.index;
    }
}
