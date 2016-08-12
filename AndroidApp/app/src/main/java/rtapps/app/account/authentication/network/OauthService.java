package rtapps.app.account.authentication.network;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rtapps.app.account.authentication.network.responses.OAuthTokenResponse;

/**
 * Created by rtichauer on 8/12/16.
 */
public interface OauthService {

    @FormUrlEncoded
    @POST("/oauth/token")
    OAuthTokenResponse getAccessToken(@Field("grant_type") String grant_type,
                                      @Field("username")String username,
                                      @Field("password")String password);

    @FormUrlEncoded
    @POST("/oauth/token")
    OAuthTokenResponse getAccessToken(@Field("grant_type") String grant_type,
                                      @Field("refresh_token")String refreshToken);

}
