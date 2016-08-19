package rtapps.app.account.user;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rtapps.app.account.user.network.AdminUserAPI;
import rtapps.app.account.user.network.responses.AdminUserResponse;
import rtapps.app.network.AccessToken;
import rtapps.app.network.authentication.TokenServiceGenerator;

/**
 * Created by rtichauer on 8/13/16.
 */
public class UserClient {

    ExecutorService executorService = Executors.newSingleThreadExecutor();


    public interface GetAdminUserCallBack{
        void onGetAdminUserResponse(AdminUserResponse adminUserResponse);
    }

    public void getAdminUser(final String username, final AccessToken accessToken, final GetAdminUserCallBack getAdminUserCallBack){

        executorService.execute(new Runnable() {
            @Override
            public void run() {


                AdminUserAPI adminUserAPI = TokenServiceGenerator.createService(AdminUserAPI.class, accessToken);

                AdminUserResponse adminUserResponse = adminUserAPI.getAdminUserData(username);
                getAdminUserCallBack.onGetAdminUserResponse(adminUserResponse);
            }
        });
    }
}
