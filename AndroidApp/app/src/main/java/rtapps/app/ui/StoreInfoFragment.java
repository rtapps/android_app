package rtapps.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rtapps.kingofthejungle.R;


public class StoreInfoFragment extends Fragment {

    public static StoreInfoFragment newInstance(String param1) {
        StoreInfoFragment fragment = new StoreInfoFragment();
        return fragment;
    }

    public StoreInfoFragment() {
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
        View v = inflater.inflate(R.layout.fragment_store_info, container, false);

        TextView title = (TextView) v.findViewById(R.id.info_title);
        title.setText(R.string.info_store_name);

        TextView subtitle = (TextView) v.findViewById(R.id.info_subtitle);
        subtitle.setText(R.string.info_subtitle);


        addStores(v, inflater);


        return v;
    }

    private void addStores(View v, LayoutInflater inflater) {

        LinearLayout storeInfoContainer = (LinearLayout) v.findViewById(R.id.stores_info);

        //String[] storeName = {R.string.info_store1_name , ""};

        //for(int i = 0 ; i < 2 ; i++){
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.store_info, storeInfoContainer, false);

        ((TextView) ll.findViewById(R.id.sore_info_name)).setText(R.string.info_store1_name);
        ((TextView) ll.findViewById(R.id.sore_info_address)).setText(R.string.info_store1_address);

        storeInfoContainer.addView(ll);

        View ll3 =  inflater.inflate(R.layout.seprator_line, storeInfoContainer, false);
        storeInfoContainer.addView(ll3);

        LinearLayout ll1 = (LinearLayout) inflater.inflate(R.layout.store_info, storeInfoContainer, false);

        ((TextView) ll1.findViewById(R.id.sore_info_name)).setText(R.string.info_store2_name);
        ((TextView) ll1.findViewById(R.id.sore_info_address)).setText(R.string.info_store2_address);

        storeInfoContainer.addView(ll1);

        View ll5 =  inflater.inflate(R.layout.seprator_line, storeInfoContainer, false);
        storeInfoContainer.addView(ll5);

        LinearLayout ll9 = (LinearLayout) inflater.inflate(R.layout.store_info, storeInfoContainer, false);

        ((TextView) ll9.findViewById(R.id.sore_info_name)).setText(R.string.info_store2_name);
        ((TextView) ll9.findViewById(R.id.sore_info_address)).setText(R.string.info_store2_address);

        storeInfoContainer.addView(ll9);

        View ll83 =  inflater.inflate(R.layout.seprator_line, storeInfoContainer, false);
        storeInfoContainer.addView(ll83);

        LinearLayout ll14 = (LinearLayout) inflater.inflate(R.layout.store_info, storeInfoContainer, false);

        ((TextView) ll14.findViewById(R.id.sore_info_name)).setText(R.string.info_store2_name);
        ((TextView) ll14.findViewById(R.id.sore_info_address)).setText(R.string.info_store2_address);

        storeInfoContainer.addView(ll14);
        //}


    }


}
