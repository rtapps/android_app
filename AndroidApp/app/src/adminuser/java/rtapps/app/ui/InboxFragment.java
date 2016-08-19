package rtapps.app.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.rtapps.kingofthejungle.R;

import rtapps.app.infrastructure.BusHolder;
import rtapps.app.messages.AddMessageActivity;

/**
 * Created by rtichauer on 8/13/16.
 */
public class InboxFragment extends InboxFragmentBase {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.inbox_fragment_add_message_item_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.add_message:
                AddMessageActivity.startActivity(getActivity());
                // do s.th.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
