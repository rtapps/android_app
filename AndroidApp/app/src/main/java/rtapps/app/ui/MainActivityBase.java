package rtapps.app.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;
import com.rtapps.kingofthejungle.R;

import rtapps.app.gcm.GcmPrefrences;
import rtapps.app.gcm.RegistrationIntentService;
import rtapps.app.inbox.AsyncGetAllMessages;
import rtapps.app.services.SyncDataService;


public abstract class MainActivityBase extends AppCompatActivity {
    private BottomBar bottomBar;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setFixedInactiveIconColor(0xffa9a9a9);

        bottomBar.setFragmentItems(getSupportFragmentManager(), R.id.fragmentContainer,
                new BottomBarFragment(new InboxFragment(), R.drawable.ic_sale, R.string.tab_sales),
                new BottomBarFragment(new CatalogFragment(), R.drawable.ic_catalog, R.string.tab_catalog),
                new BottomBarFragment(StoreInfoFragment.newInstance("Website page under construction."), R.drawable.ic_info, R.string.tab_store_info)
                //new BottomBarFragment(StoreWebsiteFragment.newInstance("Website page under construction."), R.drawable.globus, R.string.tab_website),
        );

        bottomBar.selectTabAtPosition(1, false);

        bottomBar.setActiveTabColor(0xff090909);




        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(GcmPrefrences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Toast.makeText(MainActivityBase.this, "succeeded", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(MainActivityBase.this, "failed", Toast.LENGTH_LONG);
                }
            }
        };


        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        callSyncMessages();
    }

    private void callSyncMessages(){
        startService(new Intent(this, SyncDataService.class));
//        AsyncGetAllMessages gam = new AsyncGetAllMessages();
//        gam.execute(this);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GcmPrefrences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("MainActivity", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }



}
