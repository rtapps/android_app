package rtapps.app.messages.network;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rtapps.app.account.authentication.network.BasicErrorHandler;
import rtapps.app.config.Configurations;
import rtapps.app.network.AccessToken;

/**
 * Created by rtichauer on 8/19/16.
 */
public class AuthFileUploadServiceGenerator {



    public static <S> S createService(Class<S> serviceClass, final AccessToken accessToken) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(0, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(0, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(0, TimeUnit.SECONDS);

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(Configurations.BASE_URL)
                .setClient(new OkClient(okHttpClient));


        if (accessToken!= null) {
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Authorization",
                            accessToken.getTokenType()+ " " + accessToken.getAccessToken());
                }
            });
        }

        builder.setErrorHandler(new BasicErrorHandler());

        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }
}
