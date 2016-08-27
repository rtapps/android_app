package rtapps.app.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.rtapps.kingofthejungle.R;
import com.sw926.imagefileselector.ImageCropper;
import com.sw926.imagefileselector.ImageFileSelector;

import java.io.File;

import id.zelory.compressor.Compressor;
import retrofit.mime.TypedFile;
import rtapps.app.account.AccountManager;
import rtapps.app.config.Configurations;
import rtapps.app.messages.AddMessageActivity;
import rtapps.app.messages.network.AddMessageAPI;
import rtapps.app.messages.network.AuthFileUploadServiceGenerator;
import rtapps.app.network.AccessToken;

/**
 * Created by tazo on 20/08/2016.
 */
public class NewMessageFragment extends Fragment {
    /// private ImageFileSelector mImageFileSelector;
    //private ImageCropper mImageCropper;
    private EditText messageHeaderEditText;
    private EditText messageTagEditText;
    private EditText messageBodyEditText;
    private CheckBox sendPushCheckBox;
    private Button sendButton;
    private File fullImage;
    private File compressedCroppedImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_message, container, false);


        final ImageView previewImage = (ImageView) v.findViewById(R.id.inbox_edit_item_image);
        messageTagEditText = (EditText) v.findViewById(R.id.inbox_edit_tag);
        messageHeaderEditText = (EditText) v.findViewById(R.id.inbox_edit_item_title);
        messageBodyEditText = (EditText) v.findViewById(R.id.inbox_edit_item_content);
        sendPushCheckBox = (CheckBox) v.findViewById(R.id.inbox_edit_send_push);
        sendButton = (Button) v.findViewById(R.id.inbox_edit_send_button);

        previewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddMessageActivity) getActivity()).mImageFileSelector.selectImage(getActivity());
            }
        });

        ((AddMessageActivity) getActivity()).mImageFileSelector = new ImageFileSelector(getActivity());

        ((AddMessageActivity) getActivity()).mImageFileSelector.setCallback(new ImageFileSelector.Callback() {
            @Override
            public void onSuccess(final String file) {
                cropImage(file);
            }

            @Override
            public void onError() {

            }
        });


        ((AddMessageActivity) getActivity()).mImageCropper = new ImageCropper(getActivity());
        ((AddMessageActivity) getActivity()).mImageCropper.setCallback(new ImageCropper.ImageCropperCallback() {
            @Override
            public void onCropperCallback(ImageCropper.CropperResult result, File srcFile, File outFile) {
                if (result == ImageCropper.CropperResult.success) {
                    compressedCroppedImage = Compressor.getDefault(getActivity()).compressToFile(outFile);
                    fullImage = srcFile;
                    //Set image for preview
                    Bitmap myBitmap = BitmapFactory.decodeFile(compressedCroppedImage.getAbsolutePath());
                    previewImage.setImageBitmap(myBitmap);
//                    File compressedOriginalImage = Compressor.getDefault(AddMessageActivity.this).compressToFile(outFile);

                    //uploadMessage(srcFile, compressedCroppedImage);

                } else if (result == ImageCropper.CropperResult.error_illegal_input_file) {

                } else if (result == ImageCropper.CropperResult.error_illegal_out_file) {

                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMessage();
                getActivity().finish();
            }
        });

        return v;
    }

    private void uploadMessage() {
        AccessToken accessToken = AccountManager.get().getUser().getAccessToken();
        TypedFile typedFullImage = new TypedFile("multipart/form-data", fullImage);
        TypedFile typedCompressedCroppedImage = new TypedFile("multipart/form-data", compressedCroppedImage);

        String messageTag = messageBodyEditText.getText().toString();
        String messageHeader = messageHeaderEditText.getText().toString();
        String messageBody = messageBodyEditText.getText().toString();
        boolean sendPush = sendPushCheckBox.isChecked();

        UploadNewMessageTask uploadNewMessageTask = new UploadNewMessageTask(getActivity(), accessToken, messageHeader, messageBody, sendPush, typedFullImage, typedCompressedCroppedImage);
        uploadNewMessageTask.execute();

    }

    private void cropImage(final String file) {

        ((AddMessageActivity) getActivity()).mImageCropper.setOutPutAspect(2, 1);
        ((AddMessageActivity) getActivity()).mImageCropper.setOutPut(0, 180);
        ((AddMessageActivity) getActivity()).mImageCropper.setScale(true);
        ((AddMessageActivity) getActivity()).mImageCropper.cropImage(new File(file));
    }


    private static class UploadNewMessageTask extends AsyncTask<Void, Void, Void> {

        AccessToken accessToken;
        String messageHeader;
        String messageBody;
        TypedFile typedFullImage;
        TypedFile typedCompressedCroppedImage;
        Context context;
        boolean isPush;

        public UploadNewMessageTask(Context context, AccessToken accessToken, String messageHeader, String messageBody, boolean isPush, TypedFile typedFullImage, TypedFile typedCompressedCroppedImage) {
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
            addMessageAPI.putMessage(Configurations.APPLICATION_ID, this.messageHeader, this.messageBody, this.isPush, this.typedFullImage, this.typedCompressedCroppedImage);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            ((AddMessageActivity) context).finish();
        }
    }


}
