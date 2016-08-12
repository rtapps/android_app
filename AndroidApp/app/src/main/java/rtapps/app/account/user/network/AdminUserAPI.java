package rtapps.app.account.user.network;

import retrofit.http.GET;
import retrofit.http.Query;
import rtapps.app.account.user.network.responses.AdminUserResponse;

/**
 * Created by rtichauer on 8/12/16.
 */
public interface AdminUserAPI {

    @GET("/adminUser")
    AdminUserResponse getAdminUserData(@Query("username") String username);
}
