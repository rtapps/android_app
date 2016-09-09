package rtapps.app.catalog;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by rtichauer on 8/26/16.
 */
public class CatalogImageItem implements Parcelable {

    private int index;

    private File image;

    private String imageId;

    private int id;

    private boolean isNew;

    private boolean isAddNew;

    public CatalogImageItem (int id, String imageId, int index, File image, boolean isNew, boolean isAddNew){
        this.id = id;
        this.imageId = imageId;
        this.index = index;
        this.image = image;
        this.isNew = isNew;
        this.isAddNew = isAddNew;
    }

    public boolean isAddNew() {
        return isAddNew;
    }

    public void setAddNew(boolean addNew) {
        isAddNew = addNew;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeSerializable(this.image);
        dest.writeString(this.imageId);
        dest.writeInt(this.id);
        dest.writeByte(this.isNew ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAddNew ? (byte) 1 : (byte) 0);
    }

    protected CatalogImageItem(Parcel in) {
        this.index = in.readInt();
        this.image = (File) in.readSerializable();
        this.imageId = in.readString();
        this.id = in.readInt();
        this.isNew = in.readByte() != 0;
        this.isAddNew = in.readByte() != 0;
    }

    public static final Creator<CatalogImageItem> CREATOR = new Creator<CatalogImageItem>() {
        @Override
        public CatalogImageItem createFromParcel(Parcel source) {
            return new CatalogImageItem(source);
        }

        @Override
        public CatalogImageItem[] newArray(int size) {
            return new CatalogImageItem[size];
        }
    };
}
