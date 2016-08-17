package rtapps.app.services;

import android.app.IntentService;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit.RestAdapter;
import rtapps.app.config.Configurations;
import rtapps.app.databases.MessagesTable;
import rtapps.app.network.AppAPI;
import rtapps.app.network.responses.AllMessagesResponse;

/**
 * Created by tazo on 16/08/2016.
 */
public class SyncDataService extends IntentService {
    final private static String APP_PREFS = "appPref";
    final private static String LAST_UPDATED_TIME = "lastUpdatedTime";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SyncDataService(String name) {
        super(name);
    }

    public SyncDataService() {
        super("Sync All data");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // get all Messages
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Configurations.BASE_URL)
                .build();

        final AppAPI yourUsersApi = restAdapter.create(AppAPI.class);
        SharedPreferences sharedPref = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        Long lastUpdatedTime = sharedPref.getLong(LAST_UPDATED_TIME, 0);

        try {

            AllMessagesResponse allMessagesResponse = yourUsersApi.getAllMessages(Configurations.APPLICATION_ID, lastUpdatedTime);

            Log.d("AsyncGetAllMessages", "Current preference LastUpdateTime is: " + lastUpdatedTime);
            List<AllMessagesResponse.Message> messagesList = allMessagesResponse.getMessagesList();
            Log.d("AsyncGetAllMessages", "Syncing " + messagesList.size() + " messages");

            for (int i = 0; i < messagesList.size(); i++) {
                AllMessagesResponse.Message curMessage = messagesList.get(i);
                MessagesTable newEntry = new MessagesTable(curMessage);

                if (curMessage.getExist()) {
                    newEntry.save();
                    Log.d("AsyncGetAllMessages", "Added Message id = " + curMessage.toString());
                } else {
                    newEntry.delete();
                    Log.d("AsyncGetAllMessages", "Deleted Message id = " + curMessage.toString());
                }
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(LAST_UPDATED_TIME, allMessagesResponse.getLastUpdateTime());
            editor.commit();
            Log.d("AsyncGetAllMessages", "update preference LastUpdateTime to: " + allMessagesResponse.getLastUpdateTime());
            // Get preview Images


            // Get  Images
            for (AllMessagesResponse.Message currentMessage : messagesList) {
                try {
                    loadFromNetwork(currentMessage.getId(), currentMessage.getFileServerHost(), currentMessage.getFileName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }


    }

    private void loadFromNetwork(String messageId, String fileServerHost, String filename) throws IOException {
        final String imageName = messageId;
        final String imageUrl = fileServerHost + Configurations.APPLICATION_ID + "/" + imageName + "/" + filename;
        Log.d("load image", "downloaded!!! " + imageUrl);

        Bitmap bitmap = Picasso.with(this).load(imageUrl).resize(700, 700).get();
        Log.d("load image", "downloaded!!! " + bitmap.getByteCount());
        saveToInternalStorage(bitmap, imageName);
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String imageName) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, imageName + ".jpg");

        Log.d("Save", "Save to" + mypath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }
}
