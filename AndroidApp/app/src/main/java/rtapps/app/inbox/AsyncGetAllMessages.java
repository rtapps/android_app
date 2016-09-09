package rtapps.app.inbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import rtapps.app.config.ApplicationConfigs;
import rtapps.app.config.Configurations;
import rtapps.app.databases.MessagesTable;
import rtapps.app.infrastructure.BusHolder;
import rtapps.app.network.AppAPI;
import rtapps.app.account.authentication.network.OauthService;
import rtapps.app.network.responses.AllMessagesResponse;
import rtapps.app.services.MessagesSynchronizer;

/**
 * Created by tazo on 01/08/2016.
 */
public class AsyncGetAllMessages extends AsyncTask<Context, Void, Void> {

    private final Context context;
    public AsyncGetAllMessages (Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Context... params) {

        MessagesSynchronizer messagesSynchronizer = new MessagesSynchronizer(this.context);
        messagesSynchronizer.syncAllMessages();

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
