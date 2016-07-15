package Inbox;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.logging.Logger;

import rtapps.androidapp.R;

/**
 * Created by tazo on 15/07/2016.
 */
public class InboxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        switch (position){

            case 1:
                ((ViewHolder) holder).title.setText(R.string.title1);
                ((ViewHolder) holder).content.setText(R.string.content1);
                ((ViewHolder) holder).image.setImageResource(R.drawable.item1);
                break;
            case 2:
                ((ViewHolder) holder).title.setText(R.string.title2);
                ((ViewHolder) holder).content.setText(R.string.content2);
                ((ViewHolder) holder).image.setImageResource(R.drawable.animal_king_logo);
                break;
            case 3:
                ((ViewHolder) holder).title.setText(R.string.title1);
                ((ViewHolder) holder).content.setText(R.string.content3);
                ((ViewHolder) holder).image.setImageResource(R.drawable.food);
                break;
            case 0:
            default:
                ((ViewHolder) holder).title.setText(R.string.default2);
                ((ViewHolder) holder).content.setText(R.string.default3);
                ((ViewHolder) holder).image.setImageResource(R.drawable.animal_king_logo);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 30;
    }


    //Contacts View holder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public final TextView title;
        public final TextView content;
        public final ImageView image;


        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.inbox_item_title);
            content = (TextView) view.findViewById(R.id.inbox_item_content);
            image = (ImageView) view.findViewById(R.id.inbox_item_image);

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Logger.get().debug(ILogger.eTag.ACTIVITIES, "ContactsAdapter.ViewHolder:onClick() invoked. Contact hash: " + contactHash);
//                    new RetrieveContactDataTask().execute(contactHash);
//                }
//            });


        }
    }
}
