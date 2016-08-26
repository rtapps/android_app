package rtapps.app.messages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.rtapps.kingofthejungle.R;
import com.sw926.imagefileselector.ImageCropper;
import com.sw926.imagefileselector.ImageFileSelector;

import java.io.File;

import id.zelory.compressor.Compressor;
import retrofit.mime.TypedFile;
import rtapps.app.account.AccountManager;
import rtapps.app.account.authentication.BasicAuthorizationServiceGenerator;
import rtapps.app.account.user.User;
import rtapps.app.config.Configurations;
import rtapps.app.messages.network.AddMessageAPI;
import rtapps.app.messages.network.AuthFileUploadServiceGenerator;
import rtapps.app.network.AccessToken;

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
                    File compressedCroppedImage = Compressor.getDefault(AddMessageActivity.this).compressToFile(outFile);
                    File compressedOriginalImage = Compressor.getDefault(AddMessageActivity.this).compressToFile(srcFile);

                    uploadMessage(compressedOriginalImage, compressedCroppedImage);

                } else if (result == ImageCropper.CropperResult.error_illegal_input_file) {

                } else if (result == ImageCropper.CropperResult.error_illegal_out_file) {

                }
            }
        });
    }

    private void uploadMessage(File fullImage, File compressedCroppedImage) {
        AccessToken accessToken = AccountManager.get().getUser().getAccessToken();
        TypedFile typedFullImage = new TypedFile("multipart/form-data", fullImage);
        TypedFile typedCompressedCroppedImage = new TypedFile("multipart/form-data", compressedCroppedImage);

        String messageTag = ((EditText)findViewById(R.id.message_tag)).getText().toString();
        String messageHeader = ((EditText)findViewById(R.id.message_header)).getText().toString();
        String messageBody = ((EditText)findViewById(R.id.message_body)).getText().toString();
        boolean sendPush = ((CheckBox)findViewById(R.id.send_push)).isChecked();



        UploadNewMessageTask uploadNewMessageTask = new UploadNewMessageTask(this, accessToken,messageHeader,messageBody, sendPush, typedFullImage, typedCompressedCroppedImage);

        uploadNewMessageTask.execute();

    }

    private static class UploadNewMessageTask extends AsyncTask<Void, Void, Void>{

        AccessToken accessToken;
        String messageHeader;
        String messageBody;
        TypedFile typedFullImage;
        TypedFile typedCompressedCroppedImage;
        Context context;
        boolean isPush;

        public UploadNewMessageTask(Context context, AccessToken accessToken, String messageHeader, String messageBody,boolean isPush, TypedFile typedFullImage, TypedFile typedCompressedCroppedImage){
            this.context = context;
            this.accessToken = accessToken;
            this.messageHeader = messageHeader;
            this.messageBody = messageBody;
            this.isPush = isPush;
            this.typedFullImage = typedFullImage;
            this.typedCompressedCroppedImage = typedCompressedCroppedImage;
        }

        @Override
        protected Void doInBackground(Void... params) {
            AddMessageAPI addMessageAPI = AuthFileUploadServiceGenerator.createService(AddMessageAPI.class, accessToken);
            addMessageAPI.putMessage(Configurations.APPLICATION_ID, this.messageHeader, this.messageBody,this.isPush, this.typedFullImage, this.typedCompressedCroppedImage);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            ((AddMessageActivity)context).finish();
        }
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

        mImageCropper.setOutPutAspect(2, 1);
        mImageCropper.setOutPut(0, 180);
        mImageCropper.setScale(true);
        mImageCropper.cropImage(new File(file));
    }
}
