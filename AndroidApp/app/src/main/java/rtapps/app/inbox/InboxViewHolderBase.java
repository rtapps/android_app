package rtapps.app.inbox;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.share.widget.ShareButton;
import com.rtapps.kingofthejungle.R;

//Contacts View holder class
public abstract class InboxViewHolderBase extends RecyclerView.ViewHolder {
    public final TextView title;
    public final TextView content;
    public final ImageView image;
    public final ImageView tagIcon;
    public final RelativeLayout shareButton;
    public final View button;

    protected String messageId;


    public InboxViewHolderBase(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.inbox_item_title);
        content = (TextView) view.findViewById(R.id.inbox_item_content);
        image = (ImageView) view.findViewById(R.id.inbox_item_image);
        tagIcon = (ImageView)view.findViewById(R.id.inbox_item_tag);
        button = view.findViewById(R.id.inbox_cell_button);
        shareButton = (RelativeLayout)view.findViewById(R.id.share_button);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // context.startActivity(new Intent(context , adPage.class));
            }
        });
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return this.messageId;
    }
}