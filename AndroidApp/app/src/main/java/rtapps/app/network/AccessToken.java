package rtapps.app.network;

import java.util.Date;

/**
 * Created by rtichauer on 8/13/16.
 */
public class AccessToken {

    private String accessToken;
    private String refreshToken;

    private String scope;

    private String tokenType;

    private Date expirationDate;

    public AccessToken (String accessToken, String refreshToken, String scope, String tokenType, Date expirationDate){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.scope = scope;
        this.tokenType = tokenType;
        this.expirationDate = expirationDate;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        // OAuth requires uppercase Authorization HTTP header value for token type
        if ( ! Character.isUpperCase(tokenType.charAt(0))) {
            tokenType =
                    Character
                            .toString(tokenType.charAt(0))
                            .toUpperCase() + tokenType.substring(1);
        }

        return tokenType;
    }

    public String getRefreshToken(){
        return this.refreshToken;
    }
    public Date getExpirationDate(){
        return this.expirationDate;
    }

    public String getScope(){
        return this.scope;
    }

    @Override
    public String toString(){
        return "{accessToken=" + accessToken + ", refreshToken=" + refreshToken + ", scope=" + scope + ", tokenType=" + tokenType + ", expirationDate=" + expirationDate + "}";
    }
}