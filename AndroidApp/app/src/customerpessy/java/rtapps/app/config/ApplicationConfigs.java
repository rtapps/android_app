package rtapps.app.config;

import com.google.android.gms.maps.model.LatLng;

import rtapps.app.account.AccountManager;

/**
 * Created by rtichauer on 9/9/16.
 */
public class ApplicationConfigs {


    //Server API key: AIzaSyAabthlTclWBLXDrebQKufnCUlPJE-tJs0
    //Sender ID:765623690504

    public static String getApplicationId(){
        return "1234";
    }

    public static String getBusinessName(){
        return "פסי בוטיק";
    }

    public static String getFacebookUrl(){
        return "https://www.facebook.com/%D7%91%D7%95%D7%98%D7%99%D7%A7-%D7%A4%D7%A1%D7%99-Pessy-136203589781897/?fref=ts";
    }

    private static LatLng location = new LatLng(32.787375, 34.984951);

    public static LatLng getBusinessLocation(){
        return location;
    }
}
