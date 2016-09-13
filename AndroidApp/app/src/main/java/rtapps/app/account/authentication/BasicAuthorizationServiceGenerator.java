package rtapps.app.account.authentication;


import android.util.Base64;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import rtapps.app.account.authentication.network.BasicErrorHandler;
import rtapps.app.account.authentication.network.throwables.Error401;
import rtapps.app.config.Configurations;

import com.squareup.okhttp.OkHttpClient;

public class BasicAuthorizationServiceGenerator {


    private static RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(Configurations.BASE_URL)
                .setClient(new OkClient(new OkHttpClient()));

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password) {
        if (username != null && password != null) {
            // concatenate username and password with colon for authentication
            String credentials = username + ":" + password;
            // create Base64 encodet string
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Authorization", basic);
                    request.addHeader("Accept", "application/json");
                }
            });
        }
        builder.setErrorHandler(new BasicErrorHandler());

        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }
}