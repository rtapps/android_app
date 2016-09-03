package rtapps.app.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.rtapps.kingofthejungle.R;

import rtapps.app.config.Configurations;
import rtapps.app.messages.AddMessageActivity;

/**
 * Created by tazo on 04/09/2016.
 */
public class ActivityPreviewMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        setContentView(R.layout.activity_preview_message);

        int tagIndex = intent.getIntExtra("1", -1);
        ImageView imageTag = (ImageView) findViewById(R.id.inbox_item_tag);
        ((TextView) findViewById(R.id.inbox_item_title)).setText(intent.getStringExtra("2"));
        ((TextView) findViewById(R.id.inbox_item_content)).setText(intent.getStringExtra("3"));

        ImageView image = (ImageView) findViewById(R.id.inbox_item_image);
        Bitmap bitmap = ((BitmapDrawable)AddMessageActivity.previewImage.getDrawable()).getBitmap();
        
        image.setImageBitmap(bitmap);
        if (tagIndex != -1) {
            imageTag.setImageResource(Configurations.tagCollection[tagIndex].getTagId());
        }
    }
}
