package rtapps.app.ui;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rtapps.kingofthejungle.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rtapps.app.catalog.CatalogImageItem;
import rtapps.app.catalog.UpdateCatalogActivity;
import rtapps.app.databases.CatalogTable;
import rtapps.app.messages.AddMessageActivity;

/**
 * Created by rtichauer on 8/26/16.
 */
public class CatalogFragment extends CatalogFragmentBase {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);


        Button editButton = (Button) v.findViewById(R.id.catalog_edit_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CatalogImageItem> catalogImageItems = getCataLogImageItems();
                UpdateCatalogActivity.startActivity(getContext(), catalogImageItems);
            }
        });
        return v;
    }

//    @Override
//    public void onCreateOptionsMenu(
//            Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.catalog_fragment_update_catalog_item_detail, menu);
//    }

    private ArrayList<CatalogImageItem> getCataLogImageItems(){
        ArrayList<CatalogImageItem> catalogImageItems = new ArrayList<>();

        List<CatalogTable> catalogTables = getCatalogFromDB();

        ContextWrapper cw = new ContextWrapper(getContext().getApplicationContext());
        File directory = cw.getDir("catalog", Context.MODE_PRIVATE);

        for (int i=0; i< catalogTables.size(); i++){
            CatalogTable catalogTable = catalogTables.get(i);
            File file = new File(directory, catalogTable.getId() + "/" + catalogTable.getFullImageName());
            catalogImageItems.add(new CatalogImageItem(i, catalogTable.getId(), catalogTable.getIndex(), file, false, false));
        }
        return catalogImageItems;

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // handle item selection
//        switch (item.getItemId()) {
//            case R.id.update_catalog:
//                ArrayList<CatalogImageItem> catalogImageItems = getCataLogImageItems();
//                UpdateCatalogActivity.startActivity(getContext(), catalogImageItems);
//                // do s.th.
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
