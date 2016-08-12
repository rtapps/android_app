package rtapps.app.inbox;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.rtapps.kingofthejungle.R;

import rtapps.app.config.Configurations;
import rtapps.app.network.AppAPI;
import rtapps.app.network.responses.AllMessagesResponse;

import retrofit.RestAdapter;

/**
 * Created by tazo on 21/07/2016.
 */
public class MessageContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_content);

        Intent intent = getIntent();

        int num = intent.getExtras().getInt("NUM");


        ImageView image = (ImageView) findViewById(R.id.content_image);


//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

//        NetworkAPI apiService = retrofit.create(NetworkAPI.class);
//
//        AllMessagesResponse response = apiService.getAllMessages();

//        Log.d("magic" , response.getId() + "");
        ///


        new RetrieveFeedTask().execute();


        image.setImageResource(R.drawable.msg2);




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
}
