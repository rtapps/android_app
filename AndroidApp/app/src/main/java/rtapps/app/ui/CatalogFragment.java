package rtapps.app.ui;

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
import com.rtapps.kingofthejungle.R;

import java.util.HashMap;


import rtapps.app.gcm.NotificationsManager;

import rtapps.app.databases.DataBaseHelper;
import rtapps.app.network.responses.AllMessagesResponse;




/**
 * Created by tazo on 29/07/2016.
 */
public class CatalogFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

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

    private void initSlider(){

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();

        file_maps.put("1",R.drawable.catalog1);
        file_maps.put("2",R.drawable.catalog2);
        file_maps.put("3", R.drawable.catalog3);
        file_maps.put("3", R.drawable.catalog4);

        for(String name : file_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());//new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            slider.addSlider(textSliderView);
        }

        slider.startAutoCycle(1000,2000,true);
//        slider.setPresetTransformer(SliderLayout.Transformer.Default);
//        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//
////        slider.setCustomIndicator((PagerIndicator) v.findViewById(R.id.custom_indicator));
//
//
//        slider.setCustomAnimation(new DescriptionAnimation());
//        slider.setDuration(2000);
//        slider.addOnPageChangeListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_custom_indicator:
//                slider.setCustomIndicator((PagerIndicator) v.findViewById(R.id.custom_indicator));
//                break;
//            case R.id.action_custom_child_animation:
//                slider.setCustomAnimation(new ChildAnimationExample());
//                break;
//            case R.id.action_restore_default:
//                slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//                slider.setCustomAnimation(new DescriptionAnimation());
//                break;
//            case R.id.action_github:
////                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia/AndroidImageSlider"));
////                startActivity(browserIntent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
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
