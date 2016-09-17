package rtapps.app.imageSelection;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by rtichauer on 9/17/16.
 */
public class ImageSelector {

    Activity activity;

    int id;
    private Callback callback;


    public interface Callback{

        void onSuccess(String file);

        void onFail();

    }

    public ImageSelector(Activity activity, int id){
        this.activity = activity;
        this.id = id;
    }
    private void startSelectionActivity(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, id);
    }

    public void startImageSelection(){

        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("ImageSelector","Permission is granted");
                startSelectionActivity();
            }
            else{
                ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
        else{
            startSelectionActivity();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (Build.VERSION.SDK_INT >= 23 && activity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("ImageSelector","Permission is granted");
            startSelectionActivity();
        }
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode != 0 && requestCode == id && callback != null){
            final Uri imageUri = intent.getData();
            callback.onSuccess(getRealPathFromURI(imageUri));
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
