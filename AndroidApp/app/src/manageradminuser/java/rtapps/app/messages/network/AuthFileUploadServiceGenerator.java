package rtapps.app.messages.network;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import rtapps.app.account.authentication.network.BasicErrorHandler;
import rtapps.app.config.Configurations;
import rtapps.app.network.AccessToken;

/**
 * Created by rtichauer on 8/19/16.
 */
public class AuthFileUploadServiceGenerator {

    private static RestAdapter.Builder getBuilder(){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(Configurations.BASE_URL)
                .setClient(new OkClient(okHttpClient));

        return builder;

    }

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(Configurations.BASE_URL)
            .setClient(new OkClient(new OkHttpClient()));


    public static <S> S createService(Class<S> serviceClass, final AccessToken accessToken) {
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
