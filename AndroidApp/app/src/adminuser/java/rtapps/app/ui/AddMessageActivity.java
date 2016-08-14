package rtapps.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rtapps.kingofthejungle.R;
import com.sw926.imagefileselector.ImageCropper;
import com.sw926.imagefileselector.ImageFileSelector;

import java.io.File;

/**
 * Created by rtichauer on 8/13/16.
 */
public class AddMessageActivity extends Activity{

    ImageFileSelector mImageFileSelector;
    ImageCropper mImageCropper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        button = (Button)findViewById(R.id.activity_add_message_image_picker);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImageFileSelector.selectImage(AddMessageActivity.this);
            }
        });
        mImageFileSelector = new ImageFileSelector(AddMessageActivity.this);

        mImageFileSelector.setCallback(new ImageFileSelector.Callback() {
            @Override
            public void onSuccess(final String file) {
                cropImage(file);
            }

            @Override
            public void onError() {

            }
        });

        mImageCropper = new ImageCropper(this);
        mImageCropper.setCallback(new ImageCropper.ImageCropperCallback() {
            @Override
            public void onCropperCallback(ImageCropper.CropperResult result, File srcFile, File outFile) {
                if (result == ImageCropper.CropperResult.success) {

                } else if (result == ImageCropper.CropperResult.error_illegal_input_file) {

                } else if (result == ImageCropper.CropperResult.error_illegal_out_file) {

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mImageFileSelector.onActivityResult(requestCode, resultCode, data);
        mImageCropper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mImageFileSelector.onSaveInstanceState(outState);
        mImageCropper.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageFileSelector.onRestoreInstanceState(savedInstanceState);
        mImageCropper.onRestoreInstanceState(savedInstanceState);
    }

    // Android 6.0的动态权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mImageFileSelector.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    Button button;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddMessageActivity.class);
        context.startActivity(intent);


    }

    private void cropImage(final String file){


        mImageCropper.setOutPutAspect(3, 1);
        mImageCropper.setOutPut(0, 180);
        mImageCropper.setScale(true);

        mImageCropper.cropImage(new File(file));
    }
}
