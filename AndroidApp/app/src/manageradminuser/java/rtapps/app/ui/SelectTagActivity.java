package rtapps.app.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.rtapps.buisnessapp.R;

import rtapps.app.inbox.ImageAdapter;

/**
 * Created by tazo on 03/09/2016.
 */
public class SelectTagActivity extends AppCompatActivity {

    public static final int SELECT_TAG = 100;
    public static final String SELECTED_INDEX = "SELECTED_INDEX";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);

        GridView grid = (GridView)findViewById(R.id.tag_grid);

        grid.setAdapter(new ImageAdapter(this));
    }
}
