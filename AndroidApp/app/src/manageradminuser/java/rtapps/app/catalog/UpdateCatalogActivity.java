package rtapps.app.catalog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Update;
import com.rtapps.kingofthejungle.R;
import com.sw926.imagefileselector.ImageFileSelector;
import com.woxthebox.draglistview.DragListView;

import java.security.PublicKey;
import java.util.ArrayList;

import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import rtapps.app.account.AccountManager;
import rtapps.app.account.user.User;
import rtapps.app.catalog.network.ExistingCatalogImages;
import rtapps.app.catalog.network.ExistingCatalogImages.ExistingCatalogImage;
import rtapps.app.catalog.network.NewCatalogImage;
import rtapps.app.catalog.network.UpdateCatalogAPI;
import rtapps.app.config.ApplicationConfigs;
import rtapps.app.config.Configurations;
import rtapps.app.inbox.MessageContentActivity;
import rtapps.app.messages.network.AddMessageAPI;
import rtapps.app.messages.network.AuthFileUploadServiceGenerator;
import rtapps.app.network.AccessToken;
import rtapps.app.network.responses.CatalogImage;
import rtapps.app.services.CatalogSynchronizer;
import rtapps.app.services.SyncDataService;

/**
 * Created by rtichauer on 8/26/16.
 */
public class UpdateCatalogActivity extends Activity{

    public static final String EXTRA_CATALOG_IMAGES = "EXTRA_CATALOG_IMAGES";
    DragListView mDragListView;

    ArrayList<CatalogImageItem> catalogImageItems;
    ImageFileSelector mImageFileSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_catalog);

        mImageFileSelector = new ImageFileSelector(this);
        mDragListView = (DragListView) findViewById(R.id.update_catalog_list_view);
        mDragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
                Toast.makeText(UpdateCatalogActivity.this, "Start - position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {

            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    Toast.makeText(UpdateCatalogActivity.this, "End - position: " + toPosition, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = getIntent();
        catalogImageItems = intent.getExtras().getParcelableArrayList(UpdateCatalogActivity.EXTRA_CATALOG_IMAGES);

        if (catalogImageItems.size() < 7) {
            catalogImageItems.add(new CatalogImageItem(10, null, -1, null, false, true));
        }
        mDragListView.setLayoutManager(new LinearLayoutManager(UpdateCatalogActivity.this));


        ItemAdapter listAdapter = new ItemAdapter(mImageFileSelector, this, catalogImageItems, R.layout.uptate_catalog_item, R.id.catalog_item_image, true);


        mDragListView.setAdapter(listAdapter, false);
        mDragListView.setCanDragHorizontally(false);
    }

    public static void startActivity(Context context, ArrayList<CatalogImageItem> catalogImageItems) {
        Intent intent = new Intent(context, UpdateCatalogActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_CATALOG_IMAGES, catalogImageItems);
        context.startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mImageFileSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mImageFileSelector.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageFileSelector.onRestoreInstanceState(savedInstanceState);
    }

    // Android 6.0的动态权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mImageFileSelector.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    ProgressDialog mProgressDialog;


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Save Changes")
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                commitChanges();
                                mProgressDialog = new ProgressDialog(UpdateCatalogActivity.this);
                                mProgressDialog.setMessage("Updating...");
                                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                mProgressDialog.setCancelable(false);
                                mProgressDialog.show();
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                            }
                        }
                )
                .show();
    }

    private void commitChanges() {
        ArrayList<ExistingCatalogImage> existingCatalogImageList = new ArrayList<>();
        ArrayList<Integer> newCatalogImages = new ArrayList<>();
        TypedFile [] newCatalogImageFiles = new TypedFile [7];

        for (int i=0; i< catalogImageItems.size(); i++){
            CatalogImageItem catalogImageItem = catalogImageItems.get(i);
            if (catalogImageItem.isNew()){
                TypedFile typedFile = new TypedFile("multipart/form-data", catalogImageItem.getImage());
                newCatalogImageFiles[newCatalogImages.size()] = typedFile;
                newCatalogImages.add(i);
            }
            else if (!catalogImageItem.isAddNew()){
                existingCatalogImageList.add(new ExistingCatalogImage(catalogImageItem.getImageId(), i));
            }
        }

        ExistingCatalogImages existingCatalogImages = new ExistingCatalogImages(existingCatalogImageList);
        User user = AccountManager.get().getUser();

        UpdateCatalogTask  uploadCatalogTask = new UpdateCatalogTask (this, user.getAccessToken(), ApplicationConfigs.getApplicationId(), existingCatalogImages, newCatalogImages, newCatalogImageFiles);

        uploadCatalogTask.execute();
    }

    private static class UpdateCatalogTask extends AsyncTask{

        private Context context;
        private AccessToken accessToken;
        private String applicationId;
        private ExistingCatalogImages existingCatalogImages;
        private ArrayList<Integer> newCatalogImages;
        private TypedFile [] newCatalogImageFiles;

        public UpdateCatalogTask(Context context, AccessToken accessToken, String applicationId, ExistingCatalogImages existingCatalogImages, ArrayList<Integer> newCatalogImages, TypedFile [] newCatalogImageFiles){
            this.context = context;
            this.accessToken = accessToken;
            this.applicationId = applicationId;
            this.existingCatalogImages = existingCatalogImages;
            this.newCatalogImages = newCatalogImages;
            this.newCatalogImageFiles = newCatalogImageFiles;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            UpdateCatalogAPI updateCatalogAPI = AuthFileUploadServiceGenerator.createService(UpdateCatalogAPI.class, accessToken);
            updateCatalogAPI.updateCatalog(this.applicationId, this.existingCatalogImages, this.newCatalogImages,
                    this.newCatalogImageFiles[0],
                    this.newCatalogImageFiles[1],
                    this.newCatalogImageFiles[2],
                    this.newCatalogImageFiles[3],
                    this.newCatalogImageFiles[4],
                    this.newCatalogImageFiles[5],
                    this.newCatalogImageFiles[6]);

            CatalogSynchronizer catalogSynchronizer = new CatalogSynchronizer(context);
            catalogSynchronizer.syncCatalog();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ((Activity)context).finish();
        }
    }
}
