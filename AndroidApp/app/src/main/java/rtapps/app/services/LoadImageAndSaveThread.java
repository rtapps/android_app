package rtapps.app.services;

import android.app.DownloadManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

import rtapps.app.config.Configurations;
import rtapps.app.databases.MessagesTable;
import rtapps.app.network.responses.AllMessagesResponse;

/**
 * Created by tazo on 19/08/2016.
 */
public class LoadImageAndSaveThread implements Runnable {
    String id;
    String fileServerHost;
    Context context;
    String imageName;
    String internalDirectory;

    public static Set<String> downloadedItems = new HashSet<>();

    public LoadImageAndSaveThread(String id, String fileServerHost, String internalDirectory, String imageName , Context context) {
        this.id = id;
        this.imageName = imageName;
        this.fileServerHost = fileServerHost;
        this.internalDirectory = internalDirectory;
        this.context = context;
    }

    @Override
    public void run() {
        if (!isImageExist(imageName)) {
            final String imageUrl = fileServerHost + Configurations.APPLICATION_ID + "/" + id + "/" + imageName;
            if (!downloadedItems.contains(imageUrl)) {
                downloadedItems.add(imageUrl);

                try {
                    Bitmap bitmap = loadImageFromNetwork(imageUrl);
                    saveToInternalStorage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                downloadedItems.remove(imageUrl);
            }
            else{
                Log.d("LoadImageAndSaveThread", "Image is already being downloaded: " + imageUrl);
            }

        }

    }

    private boolean isImageExist(String imageName) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getDir(internalDirectory, Context.MODE_PRIVATE);
        File file = new File(directory, id +"/"+ imageName);
        return file.exists();
    }

    private Bitmap loadImageFromNetwork(String imageUrl) throws IOException {

        Log.d("load image", "loading image from url - !!! " + imageUrl);
        Bitmap bitmap = Picasso.with(context).load(imageUrl).get();
        Log.d("load image", "downloaded!!! #Byte: " + bitmap.getByteCount());
        return bitmap;
    }


//    private void loadImageFromNetwork(){
//        try {
//
//            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
//
//            File directory = context.getApplicationContext().getDir(internalDirectory, Context.MODE_PRIVATE);
//            // Create imageDir
//
//            File f = new File(directory, id);
//            f.mkdirs();
//            Log.d("saveToInternalStorage", "create directory" + f.mkdirs());
//
//            File file = new File(f, imageName);
//
//            final String imageUrl = fileServerHost + Configurations.APPLICATION_ID + "/" + id + "/" + imageName;
//
//            String buffer;
//            URLConnection conn = new URL(imageUrl).openConnection();
//            conn.setUseCaches(false);
//            conn.connect();
//            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
//            BufferedReader br = new BufferedReader(isr);
//            Log.d("LoadImageAndSaveThread", "Downloading file to:" + file.getAbsolutePath());
//            Log.d("LoadImageAndSaveThread", "Downloading file to:" + file.getCanonicalPath());
//            FileOutputStream fos = new FileOutputStream(file);
//
//            while ((buffer = br.readLine()) != null) {
//                fos.write(buffer.getBytes());
//            }
//
//            fos.close();
//            br.close();
//            isr.close();
//
//        } catch (IOException ioe) {
//
//        }
//    }

    private String saveToInternalStorage(Bitmap bitmapImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());

         File directory = context.getDir(internalDirectory, Context.MODE_PRIVATE);
        // Create imageDir

        File f = new File(directory, id);
        Log.d("saveToInternalStorage", "create directory" + f.mkdirs());

        File file = new File(f, imageName);

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
