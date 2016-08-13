package rtapps.app.account;

import android.content.SharedPreferences;

import android.content.res.Configuration;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

import rtapps.app.account.authentication.OAuthClient;
import rtapps.app.account.authentication.ServiceGenerator;
import rtapps.app.account.authentication.network.OauthService;
import rtapps.app.account.authentication.network.responses.OAuthTokenResponse;
import rtapps.app.account.user.User;
import rtapps.app.account.user.UserClient;
import rtapps.app.account.user.network.AdminUserAPI;
import rtapps.app.account.user.network.responses.AdminUserResponse;
import rtapps.app.network.AccessToken;
import rtapps.app.network.authentication.TokenServiceGenerator;

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
                AccessToken accessToken = getAccessTokenFromOAuthResponse(oAuthTokenResponse);
                getAdminUserFromServer(accessToken, username, new AdminUserFromServerCallback() {
                    @Override
                    public void onAdminUserFromServerSuccess(User user) {
                        loginCallback.onLoginSuccess(user);
                    }
                });
            }
        });
    }

    public User loginSync(String username ,String password){
        OauthService oauthService = ServiceGenerator.createService(OauthService.class, "app", "appsecret");
        OAuthTokenResponse oAuthTokenResponse = oauthService.getAccessToken("password", username, password);
        AccessToken accessToken = getAccessTokenFromOAuthResponse(oAuthTokenResponse);
        return getAdminUserFromServerSync(accessToken, username);
    }

    private AccessToken getAccessTokenFromOAuthResponse(OAuthTokenResponse oAuthTokenResponse){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + (oAuthTokenResponse.getExpires_in()* 1000));
        AccessToken accessToken = new AccessToken(oAuthTokenResponse.getAccess_token(), oAuthTokenResponse.getRefresh_token(), oAuthTokenResponse.getScope(), oAuthTokenResponse.getToken_type(),expirationDate);
        return accessToken;
    }

    private User getAdminUserFromServerSync (final AccessToken accessToken, final String username){
        AdminUserAPI adminUserAPI = TokenServiceGenerator.createService(AdminUserAPI.class, accessToken);
        AdminUserResponse adminUserResponse = adminUserAPI.getAdminUserData(username);
        User user = saveUserFromResponse(adminUserResponse, accessToken);
        return user;
    }

    private void getAdminUserFromServer (final AccessToken accessToken, final String username, final AdminUserFromServerCallback adminUserFromServerCallback){
        UserClient userClient = new UserClient();
        userClient.getAdminUser(username, accessToken, new UserClient.GetAdminUserCallBack() {
            @Override
            public void onGetAdminUserResponse(AdminUserResponse adminUserResponse) {
                User user = saveUserFromResponse(adminUserResponse, accessToken);
                Log.d("AccountManager", "Returned user=" + user);
                adminUserFromServerCallback.onAdminUserFromServerSuccess(user);
            }
        });
    }

    private User saveUserFromResponse(AdminUserResponse adminUserResponse, AccessToken accessToken){
        Gson gson = new Gson();
        User user = new User(adminUserResponse.getFirstName(), adminUserResponse.getLastName(), adminUserResponse.getBuisnessName(), adminUserResponse.getApplicationId(), adminUserResponse.getUsername(), accessToken);
        String jsonUser = gson.toJson(user);
        sharedPreferences.edit().putString(AccountConfiguration.USER, jsonUser).apply();
        return user;
    }

    public User getUser(){
        String userStr = sharedPreferences.getString(AccountConfiguration.USER, null);

        if (userStr == null){
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(userStr,User.class);
    }

    public void refreshUser(){
        final String userString = sharedPreferences.getString(AccountConfiguration.USER, null);
        if (userString == null){
            return;
        }
        Gson gson = new Gson();
        final User user = gson.fromJson(userString, User.class);


        if (!user.getAccessToken().getExpirationDate().after(new Date())){
            return;
        }
        OAuthClient oAuthClient = new OAuthClient();
        oAuthClient.refreshAccessToken(user.getAccessToken().getRefreshToken(), new OAuthClient.GetAccessTokenCallback() {
            @Override
            public void onAccessTokenResponse(OAuthTokenResponse oAuthTokenResponse) {
                AccessToken accessToken = getAccessTokenFromOAuthResponse(oAuthTokenResponse);
                getAdminUserFromServer(accessToken, user.getUsername(), new AdminUserFromServerCallback() {
                    @Override
                    public void onAdminUserFromServerSuccess(User user) {
                        Log.d("AccountManager", "Refresh token completed. User=" + user);
                    }
                });
            }
        });
    }

}
