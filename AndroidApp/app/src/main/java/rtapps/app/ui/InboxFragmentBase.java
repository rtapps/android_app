package rtapps.app.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.rtapps.buisnessapp.R;
import com.squareup.otto.Subscribe;

import java.util.List;

import rtapps.app.databases.MessagesTable;
import rtapps.app.inbox.InboxAdapter;
import rtapps.app.infrastructure.BusHolder;
import rtapps.app.services.MessagesSynchronizer;
import rtapps.app.services.MessagesSynchronizer.MessagesSyncEvent;


public abstract class InboxFragmentBase extends Fragment {
    protected RecyclerView recyclerView;

    private InboxAdapter inboxAdapter;

    private List<MessagesTable> allMessages;



    public InboxFragmentBase() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        BusHolder.get().getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        BusHolder.get().getBus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        new ReadMessagesFromDb().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inbox, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.inbox_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManger = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManger);

        return v;
    }

    public class ReadMessagesFromDb extends AsyncTask<Void, Void, List<MessagesTable>> {

        @Override
        protected List<MessagesTable> doInBackground(Void... params) {
            NameAlias nameAlias = NameAlias.builder("creationDate").withTable("MessagesTable").build();
            List<MessagesTable> allMessages = new Select().from(MessagesTable.class).orderBy(nameAlias, false).queryList();
            return allMessages;
        }

        @Override
        protected void onPostExecute(List<MessagesTable> newMessagesList) {
            super.onPostExecute(newMessagesList);
            allMessages = newMessagesList;
            Parcelable recyclerViewState;
            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            if (inboxAdapter == null) {
                recyclerView.setAdapter(new InboxAdapter(getActivity(), newMessagesList));
            }
            else{
                inboxAdapter.setMessageList(newMessagesList);
            }

            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }
}

