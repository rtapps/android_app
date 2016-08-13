package rtapps.app.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.rtapps.kingofthejungle.R;
import com.squareup.otto.Subscribe;

import java.util.List;

import rtapps.app.databases.MessagesTable;
import rtapps.app.inbox.AsyncGetAllMessages;
import rtapps.app.inbox.AsyncGetAllMessages.MessagesSyncEvent;
import rtapps.app.inbox.InboxAdapter;
import rtapps.app.infrastructure.BusHolder;


public abstract class InboxFragmentBase extends Fragment {
    protected RecyclerView recyclerView;


    public InboxFragmentBase() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusHolder.get().getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusHolder.get().getBus().unregister(this);
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

        //Toolbar toolbar = (Toolbar) v.findViewById(R.id.mainToolBar);
        //set toolbar appearance

        recyclerView = (RecyclerView) v.findViewById(R.id.inbox_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManger = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManger);

        //recyclerView.setAdapter(new InboxAdapter(getActivity()));

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

            //messagesList = newMessagesList;
            recyclerView.setAdapter(new InboxAdapter(getActivity(), newMessagesList));
        }
    }


    @Subscribe
    public void onMessagesSync(MessagesSyncEvent event) {
        new ReadMessagesFromDb().execute();
    }
}

