package rtapps.app.network;

import com.google.gson.JsonObject;

import retrofit.http.Body;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Field;
import retrofit.http.Query;
import rtapps.app.network.responses.AllMessagesResponse;
import retrofit.http.GET;
import rtapps.app.network.responses.PushToken;


/**
 * Created by tazo on 25/07/2016.
 */
public interface NetworkAPI {

    @GET("/messages")
    AllMessagesResponse  getAllMessages(@Query("applicationId") String applicationId,
                                        @Query("fromTime")long fromTime);

    @FormUrlEncoded
    @POST("/pushToken")
    PushToken updatePushToken(@Field("applicationId") String applicationId,
                              @Field("pushToken")String pushToken,
                              @Field("osType")String osType,
                              @Field("deviceModelType")String deviceModelType);
    @FormUrlEncoded
    @POST("/pushToken")
    PushToken updatePushToken(@Field("applicationId") String applicationId,
                              @Field("pushToken")String pushToken,
                              @Field("pushTokenId")String pushTokenId,
                              @Field("osType")String osType,
                              @Field("deviceModelType")String deviceModelType);


}
