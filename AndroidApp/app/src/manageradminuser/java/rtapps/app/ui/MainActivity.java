package rtapps.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.rtapps.kingofthejungle.R;

import rtapps.app.account.AccountManager;

/**
 * Created by rtichauer on 8/13/16.
 */
public class MainActivity extends MainActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AccountManager.get().getUser() == null){
            finish();
            LoginActivity.startActivity(this);
            return;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("מנהל");
        setSupportActionBar(toolbar);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
