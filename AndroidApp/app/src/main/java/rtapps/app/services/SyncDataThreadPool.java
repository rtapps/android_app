package rtapps.app.services;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rtapps.app.databases.MessagesTable;
import rtapps.app.network.responses.AllMessagesResponse;
import rtapps.app.network.responses.CatalogImage;

import static android.os.SystemClock.sleep;

/**
 * Created by tazo on 19/08/2016.
 */
public class SyncDataThreadPool {

    public static void downloadAndSaveAllMessageImages(List<MessagesTable> messageList, Context context) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (MessagesTable message : messageList) {
            //Download regular image
            LoadImageAndSaveThread downloadImageThread = new LoadImageAndSaveThread(message.getId(), message.getFileServerHost(), "messages", message.getFullImageName(), context);
            executor.execute(downloadImageThread);
            //Download preview image
            LoadImageAndSaveThread downloadImagePreviewThread = new LoadImageAndSaveThread(message.getId(), message.getFileServerHost(),"messages", message.getPreviewImageName(), context);
            executor.execute(downloadImagePreviewThread);
            Log.d("SyncDataThreadPool", "SyncDataThreadPool- Download nad save message:" + message.toString());
        }
        executor.shutdown();

        while (!executor.isTerminated()){
            sleep(1000);
        }
    }

    public static void downloadAndSaveAllCatalogImages(List<CatalogImage> catalogImages, Context context) {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (CatalogImage catalogImage: catalogImages) {
            //Download regular image
            LoadImageAndSaveThread downloadImageThread = new LoadImageAndSaveThread(catalogImage.getId(), catalogImage.getFileServerHost(), "catalog", catalogImage.getFullImageName(), context);
            executor.execute(downloadImageThread);
            Log.d("SyncDataThreadPool", "SyncDataThreadPool- Download nad save message:" + catalogImage.toString());
        }
        executor.shutdown();

        while (!executor.isTerminated()){
            sleep(1000);
        }
    }
}
