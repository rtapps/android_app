package rtapps.app.account.authentication.network.responses;

/**
 * Created by rtichauer on 8/12/16.
 */
public class OAuthTokenResponse {

    //{"access_token":"d358853d-820c-47b9-af6f-0d1f3148aa82","token_type":"bearer","refresh_token":"63582977-aefe-4aae-be9a-a0815594b68d","expires_in":604556,"scope":"openid"}%

    String access_token;
    String token_type;
    String refresh_token;
    int expires_in;
    String scope;

    public String getAccess_token(){
        return this.access_token;
    }

    public String getToken_type(){
        return this.token_type;
    }

    public String getRefresh_token(){
        return this.refresh_token;
    }

    public int getExpires_in(){
        return this.expires_in;
    }

    public String getScope(){
        return this.scope;
    }

    @Override
    public String toString(){
        return "{access_token=" + access_token + ", token_type=" + token_type + ", refresh_token=" + refresh_token + ", expires_in=" + expires_in + ", scope=" +scope +"}";
    }
}
