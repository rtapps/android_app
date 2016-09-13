package rtapps.app.account.authentication.network;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rtapps.app.account.authentication.network.responses.OAuthTokenResponse;
import rtapps.app.account.authentication.network.throwables.Error401;
import rtapps.app.account.authentication.network.throwables.NetworkError;

/**
 * Created by rtichauer on 8/12/16.
 */
public interface OauthService {

    @FormUrlEncoded
    @POST("/oauth/token")
    OAuthTokenResponse getAccessToken(@Field("grant_type") String grant_type,
                                      @Field("username")String username,
                                      @Field("password")String password) throws NetworkError;

    @FormUrlEncoded
    @POST("/oauth/token")
    OAuthTokenResponse getAccessToken(@Field("grant_type") String grant_type,
                                      @Field("refresh_token")String refreshToken) throws NetworkError;

}
