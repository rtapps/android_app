package rtapps.app.network.responses;

/**
 * Created by rtichauer on 7/26/16.
 */
public class PushToken {

    private String pushToken;
    private String id;
    private String applicationId;

    public PushToken(String pushToken, String id, String applicationId){
        this.pushToken = pushToken;
        this.id = id;
        this.applicationId = applicationId;
    }

    public String getPushToken(){
        return this.pushToken;
    }

    public String getId(){
        return this.id;
    }

    public String getApplicationId(){
        return this.applicationId;
    }

    public void setPushToken(String pushToken){
        this.pushToken = pushToken;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setApplicationId(String applicationId){
        this.applicationId = applicationId;
    }

}
