package rtapps.app.network;

import rtapps.app.network.responses.AllMessagesResponse;
import retrofit.http.GET;


/**
 * Created by tazo on 25/07/2016.
 */
public interface NetworkAPI {

//    @GET("/contacts/1/addressbooks/{user_key}")
//    AllMessagesResponse getAllMessages(@Header("Authorization") String oauthToken,
//                                @Path("user_key") String userKey, @Query("lastModified") String lastModified);

    //@GET("/rtapps/TestServlet")
    //AllMessagesResponse getAllMessages(@Query("lastModified") String lastModified);

    @GET("/messages?applicationId=1234&fromTime=0")
    AllMessagesResponse  getAllMessages();

}
