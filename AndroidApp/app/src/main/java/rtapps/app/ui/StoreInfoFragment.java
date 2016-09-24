package rtapps.app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.share.widget.LikeView;
import com.rtapps.buisnessapp.R;

import java.util.logging.Logger;

import rtapps.app.config.ApplicationConfigs;
import rtapps.app.config.Configurations;


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


        TextView storeName = (TextView) v.findViewById(R.id.store_info_store_name);
        TextView storeAddress = (TextView) v.findViewById(R.id.store_info_store_address);
        TextView storeAddressCity = (TextView) v.findViewById(R.id.store_info_address_city);
        TextView storePhoneNumber = (TextView) v.findViewById(R.id.store_info_phone_number);

        ImageView youtubeButton = (ImageView) v.findViewById(R.id.youtube_img_button);
        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("asd", "asd");
            }
        });


        storeName.setText(R.string.app_name);
        storeAddress.setText(R.string.info_store_address);
        storeAddressCity.setText(R.string.info_store_address_city);
        storePhoneNumber.setText(R.string.info_store_phone_number);

        LikeView likeView = (LikeView) v.findViewById(R.id.like_view);
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setObjectIdAndType(
                ApplicationConfigs.getFacebookUrl(),
                LikeView.ObjectType.PAGE);


        //addStores(v, inflater);

        ImageView bPhone = (ImageView) v.findViewById(R.id.button_phone);
        bPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "048251881"));
                startActivity(intent);
            }
        });


        ImageView bFacebook = (ImageView) v.findViewById(R.id.info_button_facebook);
        bFacebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/%D7%91%D7%95%D7%98%D7%99%D7%A7-%D7%A4%D7%A1%D7%99-Pessy-136203589781897/?fref=ts";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));

                startActivity(i);
            }
        });


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

        View ll3 = inflater.inflate(R.layout.seprator_line, storeInfoContainer, false);
        storeInfoContainer.addView(ll3);

        LinearLayout ll1 = (LinearLayout) inflater.inflate(R.layout.store_info, storeInfoContainer, false);

        ((TextView) ll1.findViewById(R.id.sore_info_name)).setText(R.string.info_store2_name);
        ((TextView) ll1.findViewById(R.id.sore_info_address)).setText(R.string.info_store2_address);

        storeInfoContainer.addView(ll1);

        View ll5 = inflater.inflate(R.layout.seprator_line, storeInfoContainer, false);
        storeInfoContainer.addView(ll5);

        LinearLayout ll9 = (LinearLayout) inflater.inflate(R.layout.store_info, storeInfoContainer, false);

        ((TextView) ll9.findViewById(R.id.sore_info_name)).setText(R.string.info_store2_name);
        ((TextView) ll9.findViewById(R.id.sore_info_address)).setText(R.string.info_store2_address);

        storeInfoContainer.addView(ll9);

        View ll83 = inflater.inflate(R.layout.seprator_line, storeInfoContainer, false);
        storeInfoContainer.addView(ll83);

        LinearLayout ll14 = (LinearLayout) inflater.inflate(R.layout.store_info, storeInfoContainer, false);

        ((TextView) ll14.findViewById(R.id.sore_info_name)).setText(R.string.info_store2_name);
        ((TextView) ll14.findViewById(R.id.sore_info_address)).setText(R.string.info_store2_address);

        storeInfoContainer.addView(ll14);
        //}


    }


}
