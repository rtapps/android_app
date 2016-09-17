package rtapps.app.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.squareup.otto.Subscribe;

import rtapps.app.infrastructure.BusHolder;
import rtapps.app.services.MessagesSynchronizer;

/**
 * Created by rtichauer on 8/13/16.
 */
public class InboxFragment extends InboxFragmentBase {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusHolder.get().getBus().register(this);
//        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusHolder.get().getBus().unregister(this);
    }
    @Subscribe
    public void onMessagesSync(MessagesSynchronizer.MessagesSyncEvent event) {
        Log.d("InboxFragmentBase", "Received MessagesSyncEvent. Reloading adapter");
        new ReadMessagesFromDb().execute();
    }

}
