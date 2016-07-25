package inbox;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import network.NetworkAPI;
import network.responses.AllMessagesResponse;
import rtapps.androidapp.R;

/**
 * Created by tazo on 15/07/2016.
 */
public class InboxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;

    public InboxAdapter(Activity c){
        this.context = c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_cell, parent, false);

        ///

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , MessageContentActivity.class );
                intent.putExtra("NUM" , position);
                context.startActivity(intent);
            }
        });

        switch (position){

            case 0:
                ((ViewHolder) holder).title.setText(R.string.title1);
                ((ViewHolder) holder).content.setText(R.string.content1);
                ((ViewHolder) holder).image.setImageResource(R.drawable.item1);
                break;
            case 1:
                ((ViewHolder) holder).title.setText(R.string.title2);
                ((ViewHolder) holder).content.setText(R.string.content2);
                ((ViewHolder) holder).image.setImageResource(R.drawable.animal_king_logo);
                break;
            case 2:
                ((ViewHolder) holder).title.setText(R.string.title1);
                ((ViewHolder) holder).content.setText(R.string.content3);
                ((ViewHolder) holder).image.setImageResource(R.drawable.food);
                break;
            case 3:
                ((ViewHolder) holder).title.setText(R.string.title1);
                ((ViewHolder) holder).content.setText(R.string.content33);
                ((ViewHolder) holder).image.setImageResource(R.drawable.mvza1);
                break;
            case 4:
                ((ViewHolder) holder).title.setText(R.string.title1);
                ((ViewHolder) holder).content.setText(R.string.content3);
                ((ViewHolder) holder).image.setImageResource(R.drawable.mvza2);
                break;
            case 5:
                ((ViewHolder) holder).title.setText(R.string.title1);
                ((ViewHolder) holder).content.setText(R.string.content3);
                ((ViewHolder) holder).image.setImageResource(R.drawable.mvza3);
                break;
            default:
                ((ViewHolder) holder).title.setText(R.string.default2);
                ((ViewHolder) holder).content.setText(R.string.default3);
                ((ViewHolder) holder).image.setImageResource(R.drawable.animal_king_logo);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 6;
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
