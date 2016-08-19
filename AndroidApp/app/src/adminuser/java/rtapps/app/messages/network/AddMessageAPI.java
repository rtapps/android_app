package rtapps.app.messages.network;

import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
import rtapps.app.network.responses.AllMessagesResponse;

/**
 * Created by rtichauer on 8/19/16.
 */
public interface AddMessageAPI {

    @Multipart
    @POST("/putMessage")
    AddMessageResponse putMessage(@Part("applicationId") String applicationId,
                                   @Part("messageHeader") String messageHeader,
                                   @Part("messageBody") String messageBody,
                                   @Part("sendPush") boolean sendPush,
                                   @Part("fullImage") TypedFile fullImage,
                                   @Part("previewImage") TypedFile previewImage
    );
}