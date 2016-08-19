package rtapps.app;

import android.app.Application;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import rtapps.app.account.AccountManager;
import rtapps.app.services.SyncDataService;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, SyncDataService.class));
        FlowManager.init(new FlowConfig.Builder(this).build());
        AccountManager.get().init(PreferenceManager.getDefaultSharedPreferences(this));

        //initial Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}