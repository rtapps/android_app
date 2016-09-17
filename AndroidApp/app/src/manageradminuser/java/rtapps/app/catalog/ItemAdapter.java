package rtapps.app.catalog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.rtapps.buisnessapp.R;
import com.squareup.picasso.Picasso;
import com.sw926.imagefileselector.ImageFileSelector;
import com.woxthebox.draglistview.DragItemAdapter;

import java.io.File;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import rtapps.app.imageSelection.ImageSelector;
import rtapps.app.network.responses.CatalogImage;

public class ItemAdapter extends DragItemAdapter<CatalogImageItem, ItemAdapter.ViewHolder> {

    private final int mLayoutId;

    private final int mGrabHandleId;

    private final Context context;

    ImageSelector mImageFileSelector;

    public ItemAdapter(ImageSelector imageFileSelector, final Context context, ArrayList<CatalogImageItem> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        super(dragOnLongPress);
        this.mImageFileSelector = imageFileSelector;
        this.context = context;
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        setHasStableIds(true);
        setItemList(list);

        mImageFileSelector.setCallback(new ImageSelector.Callback() {
            @Override
            public void onSuccess(String file) {
                File compressedImage = new Compressor.Builder(context).setMaxWidth(1920).setMaxHeight(1920).setQuality(95).build().compressToFile(new File(file));

                CatalogImageItem catalogImageItem = new CatalogImageItem(mItemList.size(), null, mItemList.size(), compressedImage, true, false );
                addItem(mItemList.size() -1, catalogImageItem);
                if (mItemList.size() == 8){
                    removeItem(mItemList.size() -1);
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final CatalogImageItem catalogImageItem = mItemList.get(position);

        holder.itemView.setTag(position);

        if(catalogImageItem.isAddNew()) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.add_image_ph));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                CatalogImageItem mCatalogImageItem = catalogImageItem;

                @Override
                public void onClick(View v) {
                    if ( mCatalogImageItem.isAddNew()){
                        mImageFileSelector.startImageSelection();
                    }
                }
            });
            holder.deleteImage.setVisibility(View.INVISIBLE);
            return;
        }
        holder.deleteImage.setVisibility(View.VISIBLE);
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {

            CatalogImageItem mCatalogImageItem = catalogImageItem;
            @Override
            public void onClick(View v) {
                for (int i=0; i< mItemList.size(); i++){
                    if (mItemList.get(i) == mCatalogImageItem){
                        removeItem(i);
                    }
                    if (!mItemList.get(mItemList.size() - 1).isAddNew()){
                        addItem(mItemList.size(), new CatalogImageItem(8, null, -1, null, false, true));
                    }
                }
            }
        });
        File file = mItemList.get(position).getImage();
        Picasso.with(context).load(file).fit().centerInside().into(holder.imageView);
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).getId();
    }

    public class ViewHolder extends DragItemAdapter<CatalogImageItem, ItemAdapter.ViewHolder>.ViewHolder {
        public ImageView imageView;
        public ImageView deleteImage;

        public ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId);
            imageView = (ImageView) itemView.findViewById(R.id.catalog_item_image);
            deleteImage = (ImageView) itemView.findViewById(R.id.catalog_item_delete);
        }

        @Override
        public void onItemClicked(View view) {
            Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_SHORT).show();
            int position = (int) view.getTag();
            if ( mItemList.get(position).isAddNew()){
                mImageFileSelector.startImageSelection();
            }
        }

        @Override
        public boolean onItemLongClicked(View view) {
            Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }



}