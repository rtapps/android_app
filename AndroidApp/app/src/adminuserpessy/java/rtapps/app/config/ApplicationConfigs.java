package rtapps.app.config;

import rtapps.app.account.AccountManager;

/**
 * Created by rtichauer on 9/9/16.
 */
public class ApplicationConfigs {

    public static String getApplicationId(){
        return AccountManager.get().getUser().getApplicationId();
    }

    public static String getBusinessName(){
        return AccountManager.get().getUser().getBuisnessName();
    }

    public static String getFacebookUrl(){
        return "https://www.facebook.com/%D7%91%D7%95%D7%98%D7%99%D7%A7-%D7%A4%D7%A1%D7%99-Pessy-136203589781897/?fref=ts";
    }
}
