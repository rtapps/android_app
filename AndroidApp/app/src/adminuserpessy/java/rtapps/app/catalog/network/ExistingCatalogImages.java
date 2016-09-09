package rtapps.app.catalog.network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rtichauer on 8/26/16.
 */
public class ExistingCatalogImages{

    public ExistingCatalogImages(ArrayList<ExistingCatalogImage> existingCatalogImages) {
        this.existingCatalogImages = existingCatalogImages;
    }

    public List<ExistingCatalogImage> getExistingCatalogImages(){
        return this.existingCatalogImages;
    }

    public void setExistingCatalogImages(List<ExistingCatalogImage> existingCatalogImages){
        this.existingCatalogImages = existingCatalogImages;
    }

    private List <ExistingCatalogImage> existingCatalogImages;

    public static class ExistingCatalogImage
    {

        public ExistingCatalogImage(String id, int index){
            this.id = id;
            this.index = index;
        }


        private String id;
        private int index;

        public String getId() {
            return this.id;
        }

        public int getIndex() {
            return this.index;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
