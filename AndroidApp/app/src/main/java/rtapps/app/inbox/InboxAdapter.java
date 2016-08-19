package rtapps.app.inbox;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rtapps.kingofthejungle.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        return new InboxViewHolder(view, this.context);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

//        if (position == 0) {
//            setMockData((ViewHolder) holder);
//            return;
//        }
        final MessagesTable message = messagesList.get(position);

        ((InboxViewHolder) holder).button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageContentActivity.class);
                intent.putExtra(MessageContentActivity.EXTRA_FILE_NAME, message.getFullImageName());
                context.startActivity(intent);
            }
        });



        ((InboxViewHolder) holder).title.setText(message.getHeader());
        ((InboxViewHolder) holder).content.setText(message.getBody());
        ((InboxViewHolder) holder).setMessageId(message.getId());
        final ImageView image = ((InboxViewHolder) holder).image;
        image.setImageResource(R.drawable.animal_king_logo);


        final String imageName = message.getPreviewImageName();

        //String root = Environment.getExternalStorageDirectory().toString();


        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(Configurations.IMAGE_LIBRARY_PATH, Context.MODE_PRIVATE);
        File file =new File(directory, imageName);

        Log.d("InboxAdapter" , "Load Image from" + file.getPath());


        if (file.exists()) {
            Log.d("InboxAdapter", "file exist load from internal storage");
            loadImageFromStoragePicasso(imageName , image);
        } else {
            Log.d("InboxAdapter", "file not exist load from network" + file.getPath() );
            loadFromNetwork(message , image);
        }


    }

//    private void loadImageFromStorage(final String imageName, final ImageView image)
//    {
//        try {
//            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
//            File directory = cw.getDir(Configurations.IMAGE_LIBRARY_PATH, Context.MODE_PRIVATE);
//            File file =new File(directory, imageName);
//            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
//            image.setImageBitmap(b);
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//
//    }


    private void loadImageFromStoragePicasso(final String imageName, final ImageView image)
    {
        try {
            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            File directory = cw.getDir(Configurations.IMAGE_LIBRARY_PATH, Context.MODE_PRIVATE);
            File file =new File(directory, imageName);

            Picasso.with(context).load(file).placeholder(R.drawable.animal_king_logo).into(image);
            //Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
        //    image.setImageBitmap(b);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    private void loadFromNetwork(MessagesTable message, final ImageView image) {
        final String imageName = message.getId();
        final String imageUrl = message.getFileServerHost() + Configurations.APPLICATION_ID + "/" + imageName + "/" + message.getPreviewImageName();
        Picasso.with(context).load(imageUrl).placeholder(R.drawable.animal_king_logo).into(image);
    }

    public void setMockData(InboxViewHolder holder) {

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

    @Override
    public int getItemCount() {
        if (messagesList == null) {
            return 0;
        }
        return messagesList.size();
    }
}
