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
            new Tag(R.drawable.tag_pessy_eng,"tag_pessy_eng"),  //todo - per business
            new Tag(R.drawable.tag_pessy_heb,"tag_pessy_heb"),  //todo - per business

            new Tag(R.drawable.tag_10,"10_percent"),
            new Tag(R.drawable.tag_20,"20_percent"),
            new Tag(R.drawable.tag_30,"30_percent"),
            new Tag(R.drawable.tag_40,"40_percent"),
            new Tag(R.drawable.tag_50,"50_percent"),
            new Tag(R.drawable.tag_60,"60_percent"),
            new Tag(R.drawable.tag_70,"70_percent"),
            new Tag(R.drawable.tag_80,"80_percent"),
            new Tag(R.drawable.tag_90,"90_percent"),
            new Tag(R.drawable.tag_1_1,"tag_1_1"),
            new Tag(R.drawable.tag_2_1,"tag_2_1"),
            new Tag(R.drawable.tag_3_1,"tag_3_1"),
            new Tag(R.drawable.tag_4_1,"tag_4_1"),
            new Tag(R.drawable.tag_5_1,"tag_5_1"),

            new Tag(R.drawable.tag_discount_heb,"tag_discount_heb"),
            new Tag(R.drawable.tag_new_eng,"tag_new_eng"),
            new Tag(R.drawable.tag_new_heb,"tag_new_heb"),

    };

}