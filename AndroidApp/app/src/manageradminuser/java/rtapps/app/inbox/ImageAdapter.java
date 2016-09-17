package rtapps.app.inbox;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.rtapps.buisnessapp.R;

import rtapps.app.config.Configurations;

/**
 * Created by tazo on 03/09/2016.
 */
public class ImageAdapter extends BaseAdapter {

    Context context;

    public ImageAdapter(Context context){
      //  super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return Tag.tagCollection.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            imageView = (ImageView)((Activity) context).getLayoutInflater().inflate(R.layout.tag_image, null);
            //imageView = new ImageView(context);
            //imageView.setLayoutParams(new GridView.LayoutParams(285, 285));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).setResult(position);
                ((Activity)context).finish();
            }
        });

        imageView.setImageResource(Tag.tagCollection[position].getTagId());
        return imageView;
    }


}
