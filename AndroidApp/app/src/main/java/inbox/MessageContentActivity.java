package inbox;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.logging.Logger;

import javax.security.auth.callback.Callback;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import network.NetworkAPI;
import network.responses.AllMessagesResponse;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import rtapps.androidapp.R;

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

        //String BASE_URL = "http://54.175.240.176:8080/";
        String BASE_URL = "http://www.mocky.io/v2/57967c27260000f80517f995";


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

    class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;


        @Override
        protected Void doInBackground(Void... params) {
            String BASE_URL = "http://54.175.240.176:8080";


            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .build();


            final NetworkAPI yourUsersApi = restAdapter.create(NetworkAPI.class);


            AllMessagesResponse dd = yourUsersApi.getAllMessages();
            Log.d("magic", dd.getLastUpdateTime() + "");


            return null;
        }

        protected void onPostExecute(Void feed) {

        }
    }
}