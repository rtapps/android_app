package rtapps.app.account;

import android.content.SharedPreferences;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit.RetrofitError;
import rtapps.app.account.authentication.BasicAuthorizationServiceGenerator;
import rtapps.app.account.authentication.network.OauthService;
import rtapps.app.account.authentication.network.responses.OAuthTokenResponse;
import rtapps.app.account.authentication.network.throwables.Error400;
import rtapps.app.account.authentication.network.throwables.Error401;
import rtapps.app.account.authentication.network.throwables.NetworkError;
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

    public User loginSync(String username ,String password) throws NetworkError {
        OauthService oauthService = BasicAuthorizationServiceGenerator.createService(OauthService.class, "app", "appsecret");
        OAuthTokenResponse oAuthTokenResponse = oauthService.getAccessToken("password", username, password);
        AccessToken accessToken = getAccessTokenFromOAuthResponse(oAuthTokenResponse);
        savePass(password);
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
        refreshTokenAsync();
    }

    private String getPass() {
        return sharedPreferences.getString(AccountConfiguration.PASS, null);
    }

    private void savePass(String password) {
        sharedPreferences.edit().putString(AccountConfiguration.PASS, password).apply();
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void refreshTokenAsync() {
        if (AccountManager.get().getUser() == null) {
            Log.d("AccountManager", "Not logged in. No refresh done.");
            return;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final User user = getUser();
                try {
                    OauthService oauthService = BasicAuthorizationServiceGenerator.createService(OauthService.class, "app", "appsecret");
                    OAuthTokenResponse oAuthTokenResponse = oauthService.getAccessToken("refresh_token", user.getAccessToken().getRefreshToken());
                    Log.d("AccountManager", "auth token response=" + oAuthTokenResponse);
                    AccessToken accessToken = getAccessTokenFromOAuthResponse(oAuthTokenResponse);
                    getAdminUserFromServerSync(accessToken, user.getUsername());
                }catch (Error400 error400){
                    try {
                        loginSync(user.getUsername(), getPass());
                    }
                    catch (Error401 e1){
                        sharedPreferences.edit().putString(AccountConfiguration.USER, null).apply();
                    }
                    catch (NetworkError networkError){
                        Log.d("AccountManager", "Error while logging in");
                    }
                }
                catch (NetworkError networkError){
                    Log.d("AccountManager", "Error while logging in");
                }
            }
        });
    }
}
