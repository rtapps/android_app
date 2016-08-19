package rtapps.app.messages.network;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by rtichauer on 8/19/16.
 */
public interface DeleteMessageAPI {

    @FormUrlEncoded
    @POST("/deleteMessage")
    AddMessageResponse deleteMessage(@Field("applicationId") String applicationId,
                                  @Field("messageId") String messageId);
}
