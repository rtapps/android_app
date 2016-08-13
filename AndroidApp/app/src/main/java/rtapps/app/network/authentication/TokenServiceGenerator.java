package rtapps.app.network.authentication;

import com.squareup.okhttp.OkHttpClient;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import rtapps.app.config.Configurations;
import rtapps.app.network.AccessToken;

public class TokenServiceGenerator {

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(Configurations.BASE_URL)
                .setClient(new OkClient(new OkHttpClient()));


    public static <S> S createService(Class<S> serviceClass, final AccessToken accessToken) {
          if (accessToken!= null) {
              builder.setRequestInterceptor(new RequestInterceptor() {
                  @Override
                  public void intercept(RequestFacade request) {
                      request.addHeader("Accept", "application/json");
                      request.addHeader("Authorization", 
                         accessToken.getTokenType()+ " " + accessToken.getAccessToken());
                  }
              });
          }

        builder.setErrorHandler(new ErrorHandler() {
            @Override
            public Throwable handleError(RetrofitError cause) {
                //Todo: deal with expired tokens
                return null;
            }
        });

        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }
}