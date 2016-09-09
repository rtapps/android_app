package rtapps.app.inbox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;

import rtapps.app.account.AccountManager;
import rtapps.app.config.Configurations;
import rtapps.app.messages.network.DeleteMessageAPI;
import rtapps.app.network.authentication.TokenServiceGenerator;

/**
 * Created by rtichauer on 8/19/16.
 */
public class InboxViewHolder extends InboxViewHolderBase{
    public InboxViewHolder(final View view, final Context context) {
        super(view);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteMessageTask deleteMessageTask = new DeleteMessageTask(messageId);
                                deleteMessageTask.execute();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
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
            deleteMessageAPI.deleteMessage(AccountManager.get().getUser().getApplicationId(), this.messageId);
            return null;
        }
    }
}
