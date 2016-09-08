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

import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit.RestAdapter;
import rtapps.app.config.Configurations;
import rtapps.app.databases.CatalogTable;
import rtapps.app.databases.MessagesTable;
import rtapps.app.infrastructure.BusHolder;
import rtapps.app.network.AppAPI;
import rtapps.app.network.responses.AllMessagesResponse;
import rtapps.app.network.responses.CatalogImage;

/**
 * Created by tazo on 16/08/2016.
 */
public class SyncDataService extends IntentService {
    final private static String APP_PREFS = "appPref";
    final private static String LAST_UPDATED_TIME = "lastUpdatedTime";

    final AppAPI yourUsersApi = new RestAdapter.Builder()
            .setEndpoint(Configurations.BASE_URL)
            .build().create(AppAPI.class);


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
        syncAllMessages();
        syncCatalog();

    }

    private void syncCatalog() {
        CatalogSynchronizer catalogSynchronizer = new CatalogSynchronizer(this);
        catalogSynchronizer.syncCatalog();
    }

    private void syncAllMessages() {

        SharedPreferences sharedPref = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        Long lastUpdatedTime = sharedPref.getLong(LAST_UPDATED_TIME, 0);

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

        // Get  All Images - read all messages from DB, and download Image if not exist
        NameAlias nameAlias = NameAlias.builder("creationDate").withTable("MessagesTable").build();
        List<MessagesTable> allMessages = new Select().from(MessagesTable.class).orderBy(nameAlias, false).queryList();
        SyncDataThreadPool.downloadAndSaveAllMessageImages(allMessages, this);

    }
}
