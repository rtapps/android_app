package rtapps.app.catalog.network;

import java.util.List;

import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import rtapps.app.messages.network.AddMessageResponse;

/**
 * Created by rtichauer on 8/26/16.
 */
public interface UpdateCatalogAPI {

    @Multipart
    @POST("/updateCatalog")
    UpdateCatalogResponse updateCatalog(@Part("applicationId") String applicationId,
                                  @Part("newCatalogImages") List<NewCatalogImage> newCatalogImages,
                                  @Part("existingCatalogImages")  List<ExistingCatalogImage> existingCatalogImages
    );
}
