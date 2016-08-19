package rtapps.app.account.authentication;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import rtapps.app.account.authentication.network.*;

import rtapps.app.account.authentication.network.responses.OAuthTokenResponse;

/**
 * Created by rtichauer on 8/12/16.
 */
public class OAuthClient {

    ExecutorService executorService = Executors.newSingleThreadExecutor();


    public interface GetAccessTokenCallback{
        void onAccessTokenResponse(OAuthTokenResponse oAuthTokenResponse);
    }

    public void getAccessToken(final String username, final String password, final GetAccessTokenCallback getAccessTokenCallback){

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                OauthService oauthService = BasicAuthorizationServiceGenerator.createService(OauthService.class, "app", "appsecret");
                OAuthTokenResponse oAuthTokenResponse = oauthService.getAccessToken("password", username, password);
                Log.d("OAuthClient", "auth token response=" + oAuthTokenResponse);
                getAccessTokenCallback.onAccessTokenResponse(oAuthTokenResponse);
            }
        });
    }

    public void refreshAccessToken(final String refreshToken, final GetAccessTokenCallback getAccessTokenCallback){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                OauthService oauthService = BasicAuthorizationServiceGenerator.createService(OauthService.class, "app", "appsecret");
                OAuthTokenResponse oAuthTokenResponse = oauthService.getAccessToken("refresh_token",refreshToken);
                Log.d("OAuthClient", "auth token response=" + oAuthTokenResponse);
                getAccessTokenCallback.onAccessTokenResponse(oAuthTokenResponse);
            }
        });
    }
}
