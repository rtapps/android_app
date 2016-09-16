package rtapps.app.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
//        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, container, savedInstanceState);

        View addMessageButton = v.findViewById(R.id.inbox_add_message_button);

        addMessageButton.setVisibility(View.VISIBLE);

        addMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMessageActivity.startActivity(getActivity());
            }
        });



        return v;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.inbox_fragment_add_message_item_detail, menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // handle item selection
//        switch (item.getItemId()) {
//            case R.id.add_message:
//                // do s.th.
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
