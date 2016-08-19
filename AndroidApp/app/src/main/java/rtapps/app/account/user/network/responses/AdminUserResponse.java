package rtapps.app.account.user.network.responses;

/**
 * Created by rtichauer on 8/13/16.
 */
public class AdminUserResponse {

    private String firstName;
    private String lastName;
    private String buisnessName;
    private String applicationId;
    private String username;


    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getBuisnessName(){
        return this.buisnessName;
    }

    public String getApplicationId(){
        return this.applicationId;
    }
    public String getUsername(){
        return this.username;
    }
}
