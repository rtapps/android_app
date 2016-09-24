package rtapps.app.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.share.widget.LikeView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.CameraPosition;
import com.rtapps.buisnessapp.R;

import java.util.logging.Logger;

import rtapps.app.config.ApplicationConfigs;
import rtapps.app.config.Configurations;


import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class StoreInfoFragment extends Fragment implements OnMapReadyCallback{

    public static StoreInfoFragment newInstance(String param1) {
        StoreInfoFragment fragment = new StoreInfoFragment();
        return fragment;
    }

    public StoreInfoFragment() {
        // Required empty public constructor
    }

    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertDialog = new AlertDialog.Builder(this.getActivity() ,R.style.AlertDialogCustom)
//                .setTitle("נווט ל" + ApplicationConfigs.getBusinessName())
                .setMessage("נווט ל" + ApplicationConfigs.getBusinessName())
                .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        LatLng location = ApplicationConfigs.getBusinessLocation();

                        String uri = "geo:0,0?q="+location.latitude+","+location.longitude;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        intent.setData(Uri.parse(uri));

                        String title = "בחר אפליקציית ניווט";
                        Intent chooser = Intent.createChooser(intent, title);

                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(chooser);
                        }
                    }
                })
                .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
    }


    AlertDialog alertDialog;

    @Override
    public void onResume() {
        super.onResume();
        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null) {

                        googleMap.getUiSettings().setAllGesturesEnabled(true);

                        final LatLng location = ApplicationConfigs.getBusinessLocation();

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(15.0f).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.moveCamera(cameraUpdate);

                        googleMap.addMarker(new MarkerOptions().position(location).title(ApplicationConfigs.getBusinessName()));

                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                alertDialog.show();
                            }
                        });
                    }

                }
            });
        }
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


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}
