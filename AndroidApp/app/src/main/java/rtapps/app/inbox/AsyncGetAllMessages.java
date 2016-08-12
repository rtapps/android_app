package rtapps.app.inbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import rtapps.app.config.Configurations;
import rtapps.app.databases.MessagesTable;
import rtapps.app.infrastructure.BusHolder;
import rtapps.app.network.AppAPI;
import rtapps.app.account.authentication.network.OauthService;
import rtapps.app.network.responses.AllMessagesResponse;

/**
 * Created by tazo on 01/08/2016.
 */
public class AsyncGetAllMessages extends AsyncTask<Context, Void, Void> {

    final private static String APP_PREFS = "appPref";
    final private static String LAST_UPDATED_TIME = "lastUpdatedTime";

    @Override
    protected Void doInBackground(Context... params) {




        Context context = params[0];

        Log.d("AsyncGetAllMessages", "Sending network request ");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Configurations.BASE_URL)
                .build();


        final AppAPI yourUsersApi = restAdapter.create(AppAPI.class);


        SharedPreferences sharedPref = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        Long lastUpdatedTime = sharedPref.getLong(LAST_UPDATED_TIME, 0);
        Log.d("AsyncGetAllMessages", "Current preference LastUpdateTime is: " + lastUpdatedTime);

        try {
            AllMessagesResponse allMessagesResponse = yourUsersApi.getAllMessages(Configurations.APPLICATION_ID, lastUpdatedTime);
            List<AllMessagesResponse.Message> messagesList = allMessagesResponse.getMessagesList();
            Log.d("AsyncGetAllMessages", "Syncing " + messagesList.size() + " messages");

            for (int i = 0; i < messagesList.size(); i++) {
                AllMessagesResponse.Message curMessage = messagesList.get(i);

                MessagesTable newEntry = new MessagesTable(curMessage);

                if (curMessage.getExist()) {
                    newEntry.save();
                    Log.d("AsyncGetAllMessages", "Added Message id = " + curMessage.toString() );
                } else {
                    newEntry.delete();
                    Log.d("AsyncGetAllMessages", "Deleted Message id = " + curMessage.toString());
                }
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(LAST_UPDATED_TIME, allMessagesResponse.getLastUpdateTime());
            editor.commit();
            Log.d("AsyncGetAllMessages", "update preference LastUpdateTime to: " + allMessagesResponse.getLastUpdateTime());

        }
        catch (RetrofitError e){
            Log.e("AsyncGetAllMessages" ,e.toString());
        }




        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        BusHolder.get().getBus().post(new MessagesSyncEvent());

    }

    public static class MessagesSyncEvent
    {

    }
}
