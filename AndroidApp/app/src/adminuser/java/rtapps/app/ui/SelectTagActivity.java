package rtapps.app.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.rtapps.kingofthejungle.R;

import rtapps.app.inbox.ImageAdapter;

/**
 * Created by tazo on 03/09/2016.
 */
public class SelectTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);

        GridView grid = (GridView)findViewById(R.id.tag_grid);

        grid.setAdapter(new ImageAdapter(this));
    }
}
