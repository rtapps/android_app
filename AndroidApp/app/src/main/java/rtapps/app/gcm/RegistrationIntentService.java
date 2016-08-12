/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rtapps.app.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.rtapps.kingofthejungle.R;

import java.io.IOException;
import java.util.Date;

import retrofit.RestAdapter;
import rtapps.app.config.Configurations;
import rtapps.app.network.AppAPI;
import rtapps.app.network.responses.PushToken;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    private static final long ONE_WEEK_IN_MILLIS = 1000 * 60 * 60 * 24 * 14;

    SharedPreferences sharedPreferences;
    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(GcmPrefrences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(GcmPrefrences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(GcmPrefrences.REGISTRATION_COMPLETE);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        Date now = new Date();
        long lastUpdateTime = sharedPreferences.getLong(GcmPrefrences.LAST_REFRESH_TOKEN_UPDATE, 0);

        String lastToken = sharedPreferences.getString(GcmPrefrences.PUSH_TOKEN,"");

        if (lastToken.equals(token) && (now.getTime() - lastUpdateTime) < ONE_WEEK_IN_MILLIS){
            return;
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Configurations.BASE_URL)
                .build();

        final AppAPI yourUsersApi = restAdapter.create(AppAPI.class);

        PushToken pushToken;
        String pushTokenId = sharedPreferences.getString(GcmPrefrences.PUSH_TOKEN_ID,null);
        if (pushTokenId == null){
            pushToken = yourUsersApi.updatePushToken(Configurations.APPLICATION_ID, token, Configurations.OS_TYPE, Configurations.DEVICE_MODEL_TYPE);
            if (pushToken != null) {
                sharedPreferences.edit().putString(GcmPrefrences.PUSH_TOKEN_ID, pushToken.getId()).apply();
            }
        }
        else{
            yourUsersApi.updatePushToken(Configurations.APPLICATION_ID, token, pushTokenId, Configurations.OS_TYPE, Configurations.DEVICE_MODEL_TYPE);
        }

        sharedPreferences.edit().putLong(GcmPrefrences.LAST_REFRESH_TOKEN_UPDATE, now.getTime()).apply();
        sharedPreferences.edit().putString(GcmPrefrences.PUSH_TOKEN,token).apply();

    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}