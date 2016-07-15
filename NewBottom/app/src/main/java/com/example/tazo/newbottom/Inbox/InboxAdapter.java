package com.example.tazo.newbottom.Inbox;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tazo.newbottom.R;

import java.util.logging.Logger;

/**
 * Created by tazo on 15/07/2016.
 */
public class InboxAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }


    //Contacts View holder class
    public  class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public final TextView title;
        public final TextView content;
        public final ImageView image;


        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.contacts_fragment_list_cell_text);
            tvContactPic = (TextView) view.findViewById(R.id.contacts_profie_pic);
            presenceImageView = (PresenceImageView) view.findViewById(R.id.contacts_fragment_list_presence_image_view);
            ivFavoriteIcn = (ImageView) view.findViewById(R.id.contacts_fragment_favorite_icn);
            ivUploadIcn = (ImageView) view.findViewById(R.id.contacts_fragment_upload_icn);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Logger.get().debug(ILogger.eTag.ACTIVITIES, "ContactsAdapter.ViewHolder:onClick() invoked. Contact hash: " + contactHash);
                    new RetrieveContactDataTask().execute(contactHash);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongPress(contactHash , isFavorite, type, lookup);
                    return true;
                }
            });
        }
    }
}
