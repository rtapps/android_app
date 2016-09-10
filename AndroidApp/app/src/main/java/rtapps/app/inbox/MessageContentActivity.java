package rtapps.app.inbox;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.rtapps.kingofthejungle.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import rtapps.app.config.Configurations;
import rtapps.app.network.AppAPI;


import retrofit.RestAdapter;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by tazo on 21/07/2016.
 */
public class MessageContentActivity extends AppCompatActivity {
    SubsamplingScaleImageView mImageView;
    PhotoViewAttacher mAttacher;

    public static final String EXTRA_FILE_NAME = "extraFileName";
    public static final String EXTRA_ID = "extraId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_content);

        Intent intent = getIntent();

        String filename = intent.getExtras().getString(MessageContentActivity.EXTRA_FILE_NAME);
        String id = intent.getExtras().getString(MessageContentActivity.EXTRA_ID);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("messages", Context.MODE_PRIVATE);
        File file = new File(directory, id +"/" +filename);

        GestureImageView fimg =(GestureImageView)findViewById(R.id.g_image);

        Picasso.with(this).load(file).into(fimg);
    }


}
