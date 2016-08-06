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
import rtapps.app.network.NetworkAPI;
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

        TextView title = (TextView) findViewById(R.id.content_header);
        TextView content = (TextView) findViewById(R.id.content_body);
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

        switch (num) {
            case 0:
                title.setText(R.string.title1);
                content.setText(R.string.content1);
                image.setImageResource(R.drawable.item1);
                break;
            case 1:
                title.setText(R.string.title2);
                content.setText(R.string.content2);
                image.setImageResource(R.drawable.animal_king_logo);
                break;
            case 2:
                title.setText(R.string.title1);
                content.setText(R.string.content3);
                image.setImageResource(R.drawable.food);
                break;
            case 3:
                title.setText(R.string.title1);
                content.setText(R.string.content33);
                image.setImageResource(R.drawable.mvza1);
                break;
            case 4:
                title.setText(R.string.title1);
                content.setText(R.string.content3);
                image.setImageResource(R.drawable.mvza2);
                break;
            case 5:
                title.setText(R.string.title1);
                content.setText(R.string.content3);
                image.setImageResource(R.drawable.mvza3);
                break;
            default:
                title.setText(R.string.default2);
                content.setText(R.string.default3);
                image.setImageResource(R.drawable.animal_king_logo);
                break;
        }


    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, AllMessagesResponse> {

        private Exception exception;


        @Override
        protected AllMessagesResponse doInBackground(Void... params) {

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Configurations.BASE_URL)
                    .build();


            final NetworkAPI yourUsersApi = restAdapter.create(NetworkAPI.class);


            AllMessagesResponse dd = yourUsersApi.getAllMessages(Configurations.APPLICATION_ID, 0);
            Log.d("magic", dd.toString());
            
            return dd;
        }

        protected void onPostExecute(AllMessagesResponse allMessages) {
            //new AsyncSyncMessagesToDb().execute(allMessages.getMessagesList());
        }
    }
}
