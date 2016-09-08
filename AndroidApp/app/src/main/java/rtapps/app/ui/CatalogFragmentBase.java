package rtapps.app.ui;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.rtapps.kingofthejungle.R;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import rtapps.app.databases.CatalogTable;
import rtapps.app.databases.MessagesTable;
import rtapps.app.gcm.NotificationsManager;

import rtapps.app.databases.DataBaseHelper;
import rtapps.app.infrastructure.BusHolder;
import rtapps.app.network.responses.AllMessagesResponse;
import rtapps.app.services.CatalogUpdatedEvent;


/**
 * Created by tazo on 29/07/2016.
 */
public abstract class CatalogFragmentBase extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    public static int i = 0;
    private SliderLayout slider;
    View v;


    //https://github.com/daimajia/AndroidImageSlider/wiki/Slider-view


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         v =  inflater.inflate(R.layout.fragment_catalog, container, false);

        slider = (SliderLayout)v.findViewById(R.id.slider);

        initSlider();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initSlider();

        BusHolder.get().getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusHolder.get().getBus().unregister(this);
    }

    @Subscribe
    public void catalogUpdated(CatalogUpdatedEvent event) {
        initSlider();
    }

    private void initSlider(){


        List <CatalogTable> catalogTables = getCatalogFromDB();

        if (catalogTables == null || catalogTables.isEmpty() || !catalogHasBeenSynced(catalogTables)){
            Log.d("CatalogFragment", "Set default slider ");
            setDefaultSlider();
        }
        else{
            Log.d("CatalogFragment", "Set slider from catalog");
            setSliderFromCatalog(catalogTables);
        }
        slider.startAutoCycle(1500,1500,true);
    }

    private void setSliderFromCatalog(List<CatalogTable> catalogTables) {
        ContextWrapper cw = new ContextWrapper(getContext().getApplicationContext());
        File directory = cw.getDir("catalog", Context.MODE_PRIVATE);
        DefaultSliderView textSliderView;
        for (CatalogTable catalogTable: catalogTables){
            File file = new File(directory, catalogTable.getId() + "/" + catalogTable.getFullImageName());
            textSliderView = new DefaultSliderView(getActivity());
            textSliderView.image(file).setScaleType(BaseSliderView.ScaleType.CenterCrop).setOnSliderClickListener(this);
            slider.addSlider(textSliderView);
        }
    }

    private void setDefaultSlider() {
        DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
        textSliderView.image(R.drawable.catalog1).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);
        slider.addSlider(textSliderView);

        textSliderView = new DefaultSliderView(getActivity());
        textSliderView.image(R.drawable.catalog2).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);
        slider.addSlider(textSliderView);

        textSliderView = new DefaultSliderView(getActivity());
        textSliderView.image(R.drawable.catalog3).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);
        slider.addSlider(textSliderView);

        textSliderView = new DefaultSliderView(getActivity());
        textSliderView.image(R.drawable.catalog4).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);
        slider.addSlider(textSliderView);
    }

    protected List<CatalogTable> getCatalogFromDB(){
        NameAlias nameAlias = NameAlias.builder("index").withTable("CatalogTable").build();
        List<CatalogTable> catalogTables = new Select().from(CatalogTable.class).orderBy(nameAlias, true).queryList();
        Log.d("CatalogFragment", "Retrieved catalog table = " + catalogTables);
        return catalogTables;
    }

    private boolean catalogHasBeenSynced(List<CatalogTable> catalogTables) {
        ContextWrapper cw = new ContextWrapper(getContext().getApplicationContext());
        File directory = cw.getDir("catalog", Context.MODE_PRIVATE);
        for (CatalogTable catalogTable: catalogTables){
            File file = new File(directory, catalogTable.getId() + "/" + catalogTable.getFullImageName());
            if (!file.exists()){
                Log.d("CatalogFragment", "Retrieved file doesn't exist = " + file.getName());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
