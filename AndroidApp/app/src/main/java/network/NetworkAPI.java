package network;

import javax.security.auth.callback.Callback;


import retrofit.http.GET;
import retrofit.http.Query;
import rtapps.app.network.responses.AllMessagesResponse;


/**
 * Created by tazo on 25/07/2016.
 */
public interface NetworkAPI {

//    @GET("/contacts/1/addressbooks/{user_key}")
//    AllMessagesResponse getAllMessages(@Header("Authorization") String oauthToken,
//                                @Path("user_key") String userKey, @Query("lastModified") String lastModified);

    //@GET("/rtapps/TestServlet")
    //AllMessagesResponse getAllMessages(@Query("lastModified") String lastModified);

    //@GET("/messages?applicationId=1234&fromTime=0")
    @GET("/messages")
    AllMessagesResponse getAllMessages( @Query("applicationId") String applicationId , @Query("fromTime") String fromTime);

}
