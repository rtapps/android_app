package rtapps.app.messages;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rtapps.kingofthejungle.R;
import com.sw926.imagefileselector.ImageCropper;
import com.sw926.imagefileselector.ImageFileSelector;

import java.io.File;

import id.zelory.compressor.Compressor;
import retrofit.mime.TypedFile;
import rtapps.app.account.AccountManager;
import rtapps.app.account.authentication.network.throwables.NetworkError;
import rtapps.app.config.ApplicationConfigs;
import rtapps.app.inbox.Tag;
import rtapps.app.messages.network.AddMessageAPI;
import rtapps.app.messages.network.AuthFileUploadServiceGenerator;
import rtapps.app.network.AccessToken;
import rtapps.app.services.MessagesSynchronizer;
import rtapps.app.ui.ActivityPreviewMessage;
import rtapps.app.ui.SelectTagActivity;

/**
 * Created by rtichauer on 8/13/16.
 */
public class AddMessageActivity extends Activity implements TextWatcher {

    public ImageFileSelector mImageFileSelector;
    public ImageCropper mImageCropper;
    private EditText messageHeaderEditText;
    private ImageView addTagButton;
    private LinearLayout selectTagButton;
    private EditText messageBodyEditText;
    private String messageTag;
    private CheckBox sendPushCheckBox;
    private Button sendButton;
    private File compressedfullImage;
    private File compressedCroppedImage;
    private int tagIndex = -1;

    private Boolean imageSelected = false;
    public static ImageView previewImage;
    public static final int SELECT_PHOTO = 200;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        toolbar.setNavigationIcon(R.drawable.btn_back_black);
        toolbar.setNavigationContentDescription("CLOSE");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        previewImage = (ImageView) findViewById(R.id.inbox_edit_item_image);
        messageHeaderEditText = (EditText) findViewById(R.id.inbox_edit_item_title);
        messageBodyEditText = (EditText) findViewById(R.id.inbox_edit_item_content);
        sendPushCheckBox = (CheckBox) findViewById(R.id.inbox_edit_send_push);
        sendButton = (Button) findViewById(R.id.inbox_edit_send_button);
        addTagButton = (ImageView) findViewById(R.id.add_tag_image);
        selectTagButton = (LinearLayout) findViewById(R.id.show_preview_button);


        selectTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddMessageActivity.this, ActivityPreviewMessage.class);
                intent.putExtra("1", tagIndex);
                intent.putExtra("2", messageHeaderEditText.getText().toString());
                intent.putExtra("3", messageBodyEditText.getText().toString());
//                Bitmap bitmap = ((BitmapDrawable)previewImage.getDrawable()).getBitmap();
//                intent.putExtra("3" , bitmap);
                //intent.putExtra("4" , tagIndex);
                startActivity(intent);
            }
        });


        messageBodyEditText.addTextChangedListener(this);

        TextView label = (TextView) findViewById(R.id.inbox_edit_checkbox_label);
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPushCheckBox.toggle();
            }
        });


        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMessageActivity.this, SelectTagActivity.class);
                startActivityForResult(intent, SelectTagActivity.SELECT_TAG);
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

                Log.d("AddMessageActivity", "error on loading message");
            }
        });

        previewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mImageFileSelector.selectImage(AddMessageActivity.this);
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        mImageCropper = new ImageCropper(AddMessageActivity.this);

        mImageCropper.setCallback(new ImageCropper.ImageCropperCallback() {
            @Override
            public void onCropperCallback(ImageCropper.CropperResult result, File srcFile, File outFile) {
                if (result == ImageCropper.CropperResult.success) {
                    compressedCroppedImage = Compressor.getDefault(AddMessageActivity.this).compressToFile(outFile);
                    compressedfullImage = new Compressor.Builder(AddMessageActivity.this).setMaxWidth(1920).setMaxHeight(1920).setQuality(95).build().compressToFile(srcFile);


                    //Set image for preview
                    Bitmap myBitmap = BitmapFactory.decodeFile(compressedCroppedImage.getAbsolutePath());
                    previewImage.setImageBitmap(myBitmap);
//                    File compressedOriginalImage = Compressor.getDefault(AddMessageActivity.this).compressToFile(outFile);

                    //uploadMessage(srcFile, compressedCroppedImage);

                } else if (result == ImageCropper.CropperResult.error_illegal_input_file) {
                    Log.d("s", "s");

                } else if (result == ImageCropper.CropperResult.error_illegal_out_file) {
                    Log.d("s", "s");
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMessage();
            }
        });


//        mImageCropper = new ImageCropper(this);
//        mImageCropper.setCallback(new ImageCropper.ImageCropperCallback() {
//            @Override
//            public void onCropperCallback(ImageCropper.CropperResult result, File srcFile, File outFile) {
//                if (result == ImageCropper.CropperResult.success) {
//                    File compressedCroppedImage = Compressor.getDefault(AddMessageActivity.this).compressToFile(outFile);
////                    File compressedOriginalImage = Compressor.getDefault(AddMessageActivity.this).compressToFile(outFile);
//
//                    uploadMessage(srcFile, compressedCroppedImage);
//
//                } else if (result == ImageCropper.CropperResult.error_illegal_input_file) {
//
//                } else if (result == ImageCropper.CropperResult.error_illegal_out_file) {
//
//                }
//            }
//        });


//        NewMessageFragment nmf  = new NewMessageFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.new_message_fragment_placeholder, nmf).commit();


    }

    private void uploadMessage() {
        mProgressDialog = new ProgressDialog(AddMessageActivity.this);
        mProgressDialog.setMessage("Uploading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        AccessToken accessToken = AccountManager.get().getUser().getAccessToken();
        TypedFile typedCompressedImage = new TypedFile("multipart/form-data", compressedfullImage);
        TypedFile typedCompressedCroppedImage = new TypedFile("multipart/form-data", compressedCroppedImage);

        //String messageTag = messageBodyEditText.getText().toString();
        String messageHeader = messageHeaderEditText.getText().toString();
        String messageBody = messageBodyEditText.getText().toString();
        boolean sendPush = sendPushCheckBox.isChecked();

        UploadNewMessageTask uploadNewMessageTask = new UploadNewMessageTask(AddMessageActivity.this, accessToken, messageHeader, messageBody, sendPush, messageTag , typedCompressedImage, typedCompressedCroppedImage);
        uploadNewMessageTask.execute();

    }


    /// Methods to listen to text changed
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean enableSendButton;
        enableSendButton = !messageHeaderEditText.getText().toString().equals("");
        enableSendButton &= !messageBodyEditText.getText().toString().equals("");

        sendButton.setEnabled(enableSendButton);
    }

//    private void uploadMessage(File compressedfullImage, File compressedCroppedImage) {
//        AccessToken accessToken = AccountManager.get().getUser().getAccessToken();
//        TypedFile typedFullImage = new TypedFile("multipart/form-data", compressedfullImage);
//        TypedFile typedCompressedCroppedImage = new TypedFile("multipart/form-data", compressedCroppedImage);
//
//        String messageTag = "";//((EditText)findViewById(R.id.message_tag)).getText().toString();
//        String messageHeader = "";//((EditText)findViewById(R.id.message_header)).getText().toString();
//        String messageBody = "";//((EditText)findViewById(R.id.message_body)).getText().toString();
//        boolean sendPush = true;//((CheckBox)findViewById(R.id.send_push)).isChecked();
//
//
//
//        UploadNewMessageTask uploadNewMessageTask = new UploadNewMessageTask(this, accessToken,messageHeader,messageBody, sendPush, typedFullImage, typedCompressedCroppedImage);
//
//        uploadNewMessageTask.execute();
//
//    }

    private static class UploadNewMessageTask extends AsyncTask<Void, Void, Void> {

        AccessToken accessToken;
        String messageHeader;
        String messageBody;
        String messageTag;
        TypedFile typedCompressedFullImage;
        TypedFile typedCompressedCroppedImage;
        Context context;
        boolean isPush;

        public UploadNewMessageTask(Context context, AccessToken accessToken, String messageHeader, String messageBody, boolean isPush,String messageTag, TypedFile typedCompressedFullImage, TypedFile typedCompressedCroppedImage) {
            this.context = context;
            this.accessToken = accessToken;
            this.messageHeader = messageHeader;
            this.messageBody = messageBody;
            this.isPush = isPush;
            this.messageTag = messageTag;
            this.typedCompressedFullImage = typedCompressedFullImage;
            this.typedCompressedCroppedImage = typedCompressedCroppedImage;
        }

        @Override
        protected Void doInBackground(Void... params) {
            AddMessageAPI addMessageAPI = AuthFileUploadServiceGenerator.createService(AddMessageAPI.class, accessToken);
            try {
                addMessageAPI.putMessage(ApplicationConfigs.getApplicationId(), this.messageHeader, this.messageBody, this.isPush, this.messageTag, this.typedCompressedFullImage, this.typedCompressedCroppedImage);
            }
            catch (NetworkError networkError){
                networkError.printStackTrace();
            }

            MessagesSynchronizer messagesSynchronizer = new MessagesSynchronizer(this.context.getApplicationContext());
            messagesSynchronizer.syncAllMessages();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            ((AddMessageActivity) context).finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case SELECT_PHOTO:
//                try {
                if(resultCode != 0){
                    final Uri imageUri = intent.getData();
//                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                    previewImage.setImageBitmap(selectedImage);
                    String filePath = (getRealPathFromURI(imageUri));
                    cropImage(filePath);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                }
                return;
            case SelectTagActivity.SELECT_TAG:
                int drawId = Tag.tagCollection[resultCode].getTagId();
                addTagButton.setImageResource(drawId);
                messageTag = Tag.tagCollection[resultCode].tagName();
                return;
            default:
                mImageCropper.onActivityResult(requestCode, resultCode, intent);
                return;
        }

        // mImageFileSelector.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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

    //  Button button;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddMessageActivity.class);
        context.startActivity(intent);
    }

    private void cropImage(final String file) {
        mImageCropper.setOutPutAspect(2, 1);
        mImageCropper.setOutPut(0, 180);
        mImageCropper.setScale(true);
        mImageCropper.cropImage(new File(file));
    }


}
