package rtapps.app.account;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Date;

import rtapps.app.account.authentication.OAuthClient;
import rtapps.app.account.authentication.network.responses.OAuthTokenResponse;
import rtapps.app.account.user.User;
import rtapps.app.account.user.UserClient;
import rtapps.app.account.user.network.responses.AdminUserResponse;
import rtapps.app.network.AccessToken;

/**
 * Created by rtichauer on 8/12/16.
 */
public class AccountManager {

    private static AccountManager accountManager;

    private SharedPreferences sharedPreferences;

    private AccountManager(){

    }

    public static AccountManager get(){
        if (accountManager == null){
            synchronized (AccountManager.class){
                if (accountManager == null){
                    accountManager = new AccountManager();
                }
            }
        }
        return accountManager;
    }

    public void init(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    public interface LoginCallback{
        void onLoginSuccess(User user);

        void onLoginFail();
    }

    public interface AdminUserFromServerCallback{
        void onAdminUserFromServerSuccess(User user);
    }

    public void login(final String username, String password, final LoginCallback loginCallback){
        OAuthClient oAuthClient = new OAuthClient();
        oAuthClient.getAccessToken(username, password, new OAuthClient.GetAccessTokenCallback() {
            @Override
            public void onAccessTokenResponse(OAuthTokenResponse oAuthTokenResponse) {
                Date now = new Date();
                Date expirationDate = new Date(now.getTime() + (oAuthTokenResponse.getExpires_in()* 1000));
                AccessToken accessToken = new AccessToken(oAuthTokenResponse.getAccess_token(), oAuthTokenResponse.getRefresh_token(), oAuthTokenResponse.getScope(), oAuthTokenResponse.getToken_type(),expirationDate);
                getAdminUserFromServer(accessToken, username, new AdminUserFromServerCallback() {
                    @Override
                    public void onAdminUserFromServerSuccess(User user) {
                        loginCallback.onLoginSuccess(user);
                    }
                });
            }
        });
    }

    private void getAdminUserFromServer (final AccessToken accessToken, final String username, final AdminUserFromServerCallback adminUserFromServerCallback){
        UserClient userClient = new UserClient();
        userClient.getAdminUser(username, accessToken, new UserClient.GetAdminUserCallBack() {
            @Override
            public void onGetAdminUserResponse(AdminUserResponse adminUserResponse) {
                Gson gson = new Gson();
                User user = new User(adminUserResponse.getFirstName(), adminUserResponse.getLastName(), adminUserResponse.getBuisnessName(), adminUserResponse.getApplicationId(), adminUserResponse.getUsername(), accessToken);
                Log.d("AccountManager", "Returned user=" + user);
                String jsonUser = gson.toJson(user);
                sharedPreferences.edit().putString(AccountConfiguration.USER, jsonUser).apply();
                adminUserFromServerCallback.onAdminUserFromServerSuccess(user);
            }
        });
    }

}
