package rtapps.app.inbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.raizlabs.android.dbflow.sql.language.Select;
import com.rtapps.kingofthejungle.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import rtapps.app.config.Configurations;
import rtapps.app.databases.MessagesTable;


/**
 * Created by tazo on 15/07/2016.
 */
public class InboxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    List<MessagesTable> messagesList;

    public InboxAdapter(Activity c, List<MessagesTable> newMessagesList) {
        this.context = c;
        this.messagesList = newMessagesList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_cell, parent, false);

        ///
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

//        if (position == 0) {
//            setMockData((ViewHolder) holder);
//            return;
//        }
        final MessagesTable message = messagesList.get(position);

        ((ViewHolder) holder).button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageContentActivity.class);
                intent.putExtra("123", message.getId());
                context.startActivity(intent);
            }
        });



        ((ViewHolder) holder).title.setText(message.getHeader());
        ((ViewHolder) holder).content.setText(message.getBody());
        final ImageView image = ((ViewHolder) holder).image;
        image.setImageResource(R.drawable.animal_king_logo);


        final String imageName = message.getId();

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/yourDirectory5");
        String name = imageName + ".jpg";
        myDir = new File(myDir, name);

        File file = new File(myDir.getPath());
        if (file.exists()) {
            Log.d("F", "exist");
            loadFileFromStorage(imageName , image);
        } else {
            Log.d("F", "not exist");
            loadFromNetwork(message , image);
        }


    }

    private void  loadFileFromStorage(final String imageName, final ImageView image){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/yourDirectory5");
        String name = imageName + ".jpg";
        myDir = new File(myDir, name);
        Uri uri = Uri.fromFile(myDir);
        Picasso.with(context).load(uri).resize(1024, 1024).centerCrop().into(image);
    }

    private void loadFromNetwork(MessagesTable message, final ImageView image) {
        final String imageName = message.getId();
        final String imageUrl = message.getFileServerHost() + Configurations.APPLICATION_ID + "/" + imageName + "/" + message.getFileName();

        Picasso.with(context).load(imageUrl).resize(1024,1024).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                saveFile(bitmap, imageName);
                image.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("Loade Image", "can load image from network");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d("Loade Image", "can load image from network");
            }
        });
    }

    public void setMockData(ViewHolder holder) {

        String title = "קולקציית ערב חדשה מבחר גדול של שמלות, חצאיות וגופיות ערב ב 50% הנחה!";
        String content = "שימו לב חברות יקרות!\n" +
                "נא לפתוח יומנים ולרשום בגדול-\n" +
                "בעוד שבוע זה קורה והולך להיות מטורף!!\n" +
                "לקראת החלפת הקולקצייה ובואה של קולקציית ערב חדשה מבחר גדול של שמלות, חצאיות וגופיות ערב ב50% הנחה!!\n" +
                "באופן חד פעמי ולשלושה ימים בלבד!\n" +
                "\uD83C\uDF1Fרביעי-שישי 28-30/10";

        String content2 = "שימו לב חברות יקרות!\n" +
                "נא לפתוח יומנים ולרשום בגדול-\n" +
                "בעוד שבוע זה קורה והולך להיות מטורף!!\n" +
                "לקראת החלפת הקולקצייה ובואה של קולקציית ערב חדשה מבחר גדול של שמלות, חצאיות וגופיות ערב ב50% הנחה!!\n" +
                "באופן חד פעמי ולשלושה ימים בלבד!\n" +
                "\uD83C\uDF1Fרביעי-שישי 28-30/10";

        holder.title.setText(title);
        holder.content.setText(content);
        holder.image.setImageResource(R.drawable.msg2);


    }

    public void saveFile(Bitmap b, String picName) {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/yourDirectory5");

            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            String name = picName + ".jpg";
            myDir = new File(myDir, name);
            FileOutputStream out = new FileOutputStream(myDir);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.d("SAVE IMAGE", "SAVE IMAGE " + picName);
        } catch (Exception e) {
            // some action
            Log.d("SAVE IMAGE", "CANT SAVE IMAGE" + e.toString());
        }

    }




    @Override
    public int getItemCount() {
        if (messagesList == null) {
            return 0;
        }
        return messagesList.size();
    }


    //Contacts View holder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public final TextView title;
        public final TextView content;
        public final ImageView image;
        public final View button;


        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.inbox_item_title);
            content = (TextView) view.findViewById(R.id.inbox_item_content);
            image = (ImageView) view.findViewById(R.id.inbox_item_image);
            button = view.findViewById(R.id.inbox_cell_button);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // context.startActivity(new Intent(context , adPage.class));
                }
            });

        }
    }



}
