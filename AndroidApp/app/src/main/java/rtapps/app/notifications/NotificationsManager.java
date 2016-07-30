package rtapps.app.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.rtapps.kingofthejungle.R;

import rtapps.app.inbox.MessageContentActivity;
import rtapps.app.ui.MainActivity;

/**
 * Created by tazo on 30/07/2016.
 */
public class NotificationsManager {

    public final String NOTIFICATION_SALE_ID = "NOTIFICATION_SALE_ID";

    Context context;
    public static int i = 0;

    public NotificationsManager(Context context)
    {
        this.context = context;
    }


    public void createNotification()
    {


        NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentTitle("My notification")
                .setContentText("Hello World!");;


        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(33, mBuilder.build());
       // mNotificationManager.notifyAll();
    }

    public void createNewSaleNotification(int saleId , String title , String content)
    {
        NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setTicker(title)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setDefaults(android.support.v4.app.NotificationCompat.DEFAULT_LIGHTS | android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(android.support.v4.app.NotificationCompat.PRIORITY_HIGH)
                .setContentText(content);;


        Intent resultIntent = new Intent(context, MessageContentActivity.class);
        resultIntent.putExtra("NUM" , i++);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(33, mBuilder.build());
    }

}
