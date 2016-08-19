package rtapps.app.services;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rtapps.app.config.Configurations;
import rtapps.app.databases.MessagesTable;
import rtapps.app.network.responses.AllMessagesResponse;

/**
 * Created by tazo on 19/08/2016.
 */
public class LoadImageAndSaveThread implements Runnable {
    String messageId;
    String fileServerHost;
    Context context;
    String imageName;

    public LoadImageAndSaveThread(String messageId, String fileServerHost ,String imageName , Context context) {
        this.messageId = messageId;
        this.imageName = imageName;
        this.fileServerHost = fileServerHost;
        this.context = context;
    }

    @Override
    public void run() {
        if (!isImageExist(imageName)) {
            try {
                Bitmap b = loadImageFromNetwork(messageId, fileServerHost , imageName);
                saveToInternalStorage(b, imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean isImageExist(String imageName) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getDir(Configurations.IMAGE_LIBRARY_PATH, Context.MODE_PRIVATE);
        File file = new File(directory, imageName);
        return file.exists();
    }

    private Bitmap loadImageFromNetwork(String fileName, String fileServerHost , String imageName) throws IOException {
        final String imageUrl = fileServerHost + Configurations.APPLICATION_ID + "/" + fileName + "/" + imageName;
        Log.d("load image", "loading image from url - !!! " + imageUrl);
        Bitmap bitmap = Picasso.with(context).load(imageUrl).resize(700, 700).get();
        Log.d("load image", "downloaded!!! #Byte: " + bitmap.getByteCount());
        return bitmap;
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String imageName) throws IOException {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());

        File directory = cw.getDir(Configurations.IMAGE_LIBRARY_PATH, Context.MODE_PRIVATE);
        // Create imageDir
        File file = new File(directory, imageName);

        Log.d("saveToInternalStorage", "Save image to" + file);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }
}
