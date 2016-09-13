package rtapps.app.catalog.network;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import rtapps.app.account.authentication.network.throwables.NetworkError;
import rtapps.app.messages.network.AddMessageResponse;

/**
 * Created by rtichauer on 8/26/16.
 */
public interface UpdateCatalogAPI {

    @Multipart
    @POST("/updateCatalog")
    UpdateCatalogResponse updateCatalog(@Part("applicationId") String applicationId,
                                        @Part("existingCatalogImages")  ExistingCatalogImages existingCatalogImages,
                                        @Part("newCatalogImageIndexes") List<Integer> newCatalogImages,
                                        @Part("newCatalogImageFile1") TypedFile newCatalogImageFile1,
                                        @Part("newCatalogImageFile2") TypedFile newCatalogImageFile2,
                                        @Part("newCatalogImageFile3") TypedFile newCatalogImageFile3,
                                        @Part("newCatalogImageFile4") TypedFile newCatalogImageFile4,
                                        @Part("newCatalogImageFile5") TypedFile newCatalogImageFile5,
                                        @Part("newCatalogImageFile6") TypedFile newCatalogImageFile6,
                                        @Part("newCatalogImageFile7") TypedFile newCatalogImageFile7
    ) throws NetworkError;


}
