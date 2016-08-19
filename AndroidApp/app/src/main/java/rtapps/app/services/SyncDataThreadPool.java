package rtapps.app.services;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rtapps.app.databases.MessagesTable;
import rtapps.app.network.responses.AllMessagesResponse;

/**
 * Created by tazo on 19/08/2016.
 */
public class SyncDataThreadPool {

    public static void downloadAndSaveAllImages(List<MessagesTable> messageList, Context context) {

        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (MessagesTable message : messageList) {
            //Download regular image
            LoadImageAndSaveThread downloadImageThread = new LoadImageAndSaveThread(message.getId(), message.getFileServerHost(), message.getFullImageName(), context);
            executor.execute(downloadImageThread);
            //Download preview image
            LoadImageAndSaveThread downloadImagePreviewThread = new LoadImageAndSaveThread(message.getId(), message.getFileServerHost(), message.getPreviewImageName(), context);
            executor.execute(downloadImagePreviewThread);
            Log.d("SyncDataThreadPool", "SyncDataThreadPool- Download nad save message:" + message.toString());
        }
        executor.shutdown();

        while (!executor.isShutdown()) ;
    }
}
