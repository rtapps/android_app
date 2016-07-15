package rtapps.androidapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import Inbox.InboxAdapter;


public class InboxFragment extends Fragment {
    protected RecyclerView recyclerView;


    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_inbox, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.mainToolBar);
        //set toolbar appearance

        recyclerView = (RecyclerView) v.findViewById(R.id.inbox_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManger = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManger);

        recyclerView.setAdapter(new InboxAdapter());

        return v;
    }






}
