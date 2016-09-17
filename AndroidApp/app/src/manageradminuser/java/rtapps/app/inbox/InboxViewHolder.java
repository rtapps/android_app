package rtapps.app.inbox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.rtapps.buisnessapp.R;

import rtapps.app.account.AccountManager;
import rtapps.app.account.authentication.network.throwables.NetworkError;
import rtapps.app.config.Configurations;
import rtapps.app.messages.network.DeleteMessageAPI;
import rtapps.app.network.authentication.TokenServiceGenerator;

/**
 * Created by rtichauer on 8/19/16.
 */
public class InboxViewHolder extends InboxViewHolderBase{
    public Button deleteMessageButton;
    public InboxViewHolder(final View view, final Context context) {
        super(view);
        deleteMessageButton = (Button)view.findViewById(R.id.inbox_item_delete_button);

        deleteMessageButton.setVisibility(View.VISIBLE);
        deleteMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("מחיקת הודעה")
                        .setMessage("האם למחוק הודעה זו?")
                        .setPositiveButton("מחק", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteMessageTask deleteMessageTask = new DeleteMessageTask(messageId);
                                deleteMessageTask.execute();
                            }
                        })
                        .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)

                        .show();
            }
        });
    }

    private static class DeleteMessageTask extends AsyncTask<Void, Void, Void>{
        String messageId;

        public DeleteMessageTask(String messageId){
            this.messageId = messageId;
        }

        @Override
        protected Void doInBackground(Void... params) {
            DeleteMessageAPI deleteMessageAPI = TokenServiceGenerator.createService(DeleteMessageAPI.class, AccountManager.get().getUser().getAccessToken());
            try {
                deleteMessageAPI.deleteMessage(AccountManager.get().getUser().getApplicationId(), this.messageId);
            }
            catch (NetworkError networkError){
                Log.d("InboxViewHolder", "Delete failed with network error:" + networkError.getErrorCode());
                networkError.printStackTrace();
            }
            return null;
        }
    }
}
