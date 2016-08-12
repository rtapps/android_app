package rtapps.app.account.user;

import rtapps.app.account.authentication.network.responses.OAuthTokenResponse;
import rtapps.app.network.AccessToken;

/**
 * Created by rtichauer on 8/12/16.
 */
public class User {

    private String firstName;
    private String lastName;
    private String buisnessName;
    private int applicationId;
    private String username;
    private AccessToken accessToken;

    public User (String firstName, String lastName, String buisnessName, int applicationId, String username, AccessToken accessToken){
        this.firstName = firstName;
        this.lastName = lastName;
        this.buisnessName = buisnessName;
        this.applicationId = applicationId;
        this.username = username;
        this.accessToken = accessToken;
    }

    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getBuisnessName(){
        return this.buisnessName;
    }

    public int getApplicationId(){
        return this.applicationId;
    }
    public String getUsername(){
        return this.username;
    }

    public AccessToken getAccessToken(){
        return this.accessToken;
    }

    @Override
    public String toString(){
        return "{firstName=" + firstName + ", lastName=" + lastName + ", buisnessName=" + buisnessName + ", applicationId=" + applicationId + ", username=" + username + ", accessToken=" + accessToken +"}";
    }
}

