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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.rtapps.buisnessapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import rtapps.app.config.ApplicationConfigs;
import rtapps.app.config.Configurations;
import rtapps.app.databases.MessagesTable;
import rtapps.app.databases.MessagesTable_Table;
import rtapps.app.services.MessagesSynchronizer;
import rtapps.app.services.SyncDataService;
import rtapps.app.ui.MainActivity;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }
        sendNotification(message);
        startService(new Intent(this, SyncDataService.class));

    }

    /**
     * Create and show a simple
     * notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {

        NotificationCompat.Builder notificationBuilder = getNotificationBuilder();
        try {
            JSONObject jsonObject = new JSONObject(message);

            String id = jsonObject.get("id").toString();
            MessagesSynchronizer messagesSynchronizer = new MessagesSynchronizer(this);
            messagesSynchronizer.syncAllMessages();

            List<MessagesTable> messages = new Select().from(MessagesTable.class).where(MessagesTable_Table.id.is(id)).queryList();

            if (messages.size() > 1){
                throw new RuntimeException("More than one message found");
            }


            if (messages.size() == 1){

                notificationBuilder.setStyle(getBigPictureStyle(messages.get(0)));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private NotificationCompat.Style getBigPictureStyle(MessagesTable messagesTable) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        File directory = getDir("messages", Context.MODE_PRIVATE);
        File file = new File(directory, messagesTable.getId() +"/" +messagesTable.getFullImageName());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        bigPictureStyle.bigPicture(bitmap);
        bigPictureStyle.setBigContentTitle(messagesTable.getHeader());
        bigPictureStyle.setSummaryText(messagesTable.getBody());
        return bigPictureStyle;
    }

    private NotificationCompat.Builder getNotificationBuilder(){

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Notification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(this)
                //TODO set small icon
                .setSmallIcon(R.drawable.ic_stat_image_tag_faces)
                .setContentTitle(ApplicationConfigs.getBusinessName())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
    }
}