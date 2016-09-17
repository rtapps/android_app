package rtapps.app.inbox;

import com.rtapps.buisnessapp.R;

public class Tag{

    private  int tagId;
    private  String tagName;

    public Tag(int tagId , String tagName){
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public int getTagId(){
            return  tagId;
        }

    public String tagName(){
            return tagName;
        }

    public static final Tag[] tagCollection = {
            new Tag(R.drawable.tag_10,"10_percent"),
            new Tag(R.drawable.tag_20,"20_percent"),
            new Tag(R.drawable.tag_30,"30_percent"),
            new Tag(R.drawable.tag_40,"40_percent"),
            new Tag(R.drawable.tag_50,"50_percent"),
            new Tag(R.drawable.tag_60,"60_percent"),
            new Tag(R.drawable.tag_70,"70_percent"),
            new Tag(R.drawable.tag_80,"80_percent"),
            new Tag(R.drawable.tag_90,"90_percent"),
    };

}