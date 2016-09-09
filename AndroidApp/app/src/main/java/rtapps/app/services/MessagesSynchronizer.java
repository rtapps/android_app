package rtapps.app.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import retrofit.RestAdapter;
import rtapps.app.config.ApplicationConfigs;
import rtapps.app.config.Configurations;
import rtapps.app.databases.MessagesTable;
import rtapps.app.network.AppAPI;
import rtapps.app.network.responses.AllMessagesResponse;

/**
 * Created by rtichauer on 9/9/16.
 */
public class MessagesSynchronizer {

    final private static String APP_PREFS = "appPref";

    final private static String LAST_UPDATED_TIME = "lastUpdatedTime";

    final AppAPI yourUsersApi = new RestAdapter.Builder()
            .setEndpoint(Configurations.BASE_URL)
            .build().create(AppAPI.class);

    private final Context context;

    public MessagesSynchronizer (Context context){
        this.context = context;
    }

    public void syncAllMessages() {

        SharedPreferences sharedPref = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        Long lastUpdatedTime = sharedPref.getLong(LAST_UPDATED_TIME, 0);

        AllMessagesResponse allMessagesResponse = yourUsersApi.getAllMessages(ApplicationConfigs.getApplicationId(), lastUpdatedTime);

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
        SyncDataThreadPool.downloadAndSaveAllMessageImages(allMessages, context);

    }
}
