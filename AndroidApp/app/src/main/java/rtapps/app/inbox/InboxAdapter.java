package rtapps.app.inbox;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.rtapps.kingofthejungle.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rtapps.app.config.ApplicationConfigs;
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

        return new InboxViewHolder(view, this.context);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final MessagesTable message = messagesList.get(position);

        ((InboxViewHolder) holder).button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageContentActivity.class);
                intent.putExtra(MessageContentActivity.EXTRA_FILE_NAME, message.getFullImageName());
                intent.putExtra(MessageContentActivity.EXTRA_ID, message.getId());

                context.startActivity(intent);
            }
        });


        ((InboxViewHolder) holder).title.setText(message.getHeader());
        ((InboxViewHolder) holder).content.setText(message.getBody());

        int tagResourceId = getTagImageResourceId(message.getTag());
        ((InboxViewHolder) holder).tagIcon.setImageResource(tagResourceId);

        final ImageView image = ((InboxViewHolder) holder).image;
        ((InboxViewHolder) holder).setMessageId(message.getId());

        final String imageName = message.getPreviewImageName();

        setImageBitmap(message, imageName, image);


        ((InboxViewHolder) holder).shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("messages", Context.MODE_PRIVATE);
                File file = new File(directory, message.getId() + "/" + message.getFullImageName());
                Bitmap b = null;
                try {
                    b = BitmapFactory.decodeStream(new FileInputStream(file));

                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(b)
                            .build();
//                final ShareContent shareContent = new ShareMediaContent.Builder()
//                        .addMedium(photo).build();

//                    final SharePhotoContent shareContent = new SharePhotoContent.Builder()
//                            .addPhoto(photo).build();
                    final String imageUrl = message.getFileServerHost() + ApplicationConfigs.getApplicationId() + "/" + message.getId() + "/" + message.getFullImageName();
                    Uri myUri = Uri.parse(imageUrl);
                    ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                            .setContentTitle(message.getHeader())
                            .setContentDescription(message.getBody())
                            .setContentUrl(Uri.parse(ApplicationConfigs.getFacebookUrl()))
                            .setImageUrl(myUri)
                            .build();

                    ShareDialog shareDialog = new ShareDialog(context);
                    shareDialog.show(shareLinkContent, ShareDialog.Mode.AUTOMATIC);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private int getTagImageResourceId(String tagName){
        if (tagName == null || tagName == ""){
            return Tag.tagCollection[0].getTagId();
        }

        for(Tag curTag : Tag.tagCollection){
            if(curTag.tagName().equals(tagName)){
                return curTag.getTagId();
            }
        }

        return Tag.tagCollection[0].getTagId();
    }

    private void setImageBitmap(MessagesTable message, String imageName, ImageView image) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("messages", Context.MODE_PRIVATE);
        File file = new File(directory,message.getId() + "/" +imageName);

        Log.d("InboxAdapter", "Load Image from" + file.getPath());


        if (file.exists()) {
            Log.d("InboxAdapter", "file exist load from internal storage");
            loadImageFromStoragePicasso(message.getId(), imageName, image);
        } else {
            Log.d("InboxAdapter", "file not exist load from network" + file.getPath());
            loadFromNetwork(message, image);
        }
    }


    private void loadImageFromStoragePicasso(String messageId, final String imageName, final ImageView image) {
        try {
            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            File directory = cw.getDir("messages", Context.MODE_PRIVATE);
            File file = new File(directory, messageId + "/" +imageName);

            Picasso.with(context).load(file).fit().into(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void loadFromNetwork(MessagesTable message, final ImageView image) {
        final String imageName = message.getId();
        final String imageUrl = message.getFileServerHost() + ApplicationConfigs.getApplicationId() + "/" + imageName + "/" + message.getPreviewImageName();
        Picasso.with(context).load(imageUrl).into(image);
    }

    @Override
    public int getItemCount() {
        if (messagesList == null) {
            return 0;
        }
        return messagesList.size();
    }


}
