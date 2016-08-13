package rtapps.app.inbox;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.rtapps.kingofthejungle.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import rtapps.app.config.Configurations;
import rtapps.app.network.AppAPI;
import rtapps.app.network.responses.AllMessagesResponse;

import retrofit.RestAdapter;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by tazo on 21/07/2016.
 */
public class MessageContentActivity extends AppCompatActivity {
    ImageView mImageView;
    PhotoViewAttacher mAttacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_content);

        Intent intent = getIntent();

        String filename = intent.getExtras().getString("123");

        mImageView = (ImageView) findViewById(R.id.content_image);

       // Drawable bitmap = getResources().getDrawable(R.drawable.msg2);

        loadFileFromStorage(filename   , mImageView);
        //mImageView.setImageDrawable(bitmap);

        mAttacher = new PhotoViewAttacher(mImageView);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

//        NetworkAPI apiService = retrofit.create(NetworkAPI.class);
//
//        AllMessagesResponse response = apiService.getAllMessages();

//        Log.d("magic" , response.getId() + "");
        ///


      //  new RetrieveFeedTask().execute();


      //  image.setImageResource(R.drawable.msg2);






    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, AllMessagesResponse> {

        private Exception exception;


        @Override
        protected AllMessagesResponse doInBackground(Void... params) {

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Configurations.BASE_URL)
                    .build();


            final AppAPI yourUsersApi = restAdapter.create(AppAPI.class);


            AllMessagesResponse dd = yourUsersApi.getAllMessages(Configurations.APPLICATION_ID, 0);
            Log.d("magic", dd.toString());
            
            return dd;
        }

        protected void onPostExecute(AllMessagesResponse allMessages) {
            //new AsyncSyncMessagesToDb().execute(allMessages.getMessagesList());
        }
    }

    private void  loadFileFromStorage(final String imageName, final ImageView image){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/yourDirectory5");
        String name = imageName + ".jpg";
        myDir = new File(myDir, name);
        Uri uri = Uri.fromFile(myDir);
        Picasso.with(this).load(uri).resize(1024, 1024).centerCrop().into(image);
    }
}
