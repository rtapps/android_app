package rtapps.app.config;

import com.rtapps.kingofthejungle.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rtichauer on 7/26/16.
 */
public class Configurations {

    public static final String BASE_URL = "http://54.175.240.176:8080";

    public static final String APPLICATION_ID = "1234";

    public static final String OS_TYPE = "android";

    public static final String DEVICE_MODEL_TYPE = android.os.Build.MODEL;

    public static final String APPLICATION_NAME = "פסי בוטיק";

    public static final String STORE_FACEBOOK_PAGE_URL = "https://www.facebook.com/%D7%91%D7%95%D7%98%D7%99%D7%A7-%D7%A4%D7%A1%D7%99-Pessy-136203589781897/?fref=ts";


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

//    public static final Map<Tag> tagCollection = new HashMap<Tag>() {{
//        put("Up",    R.drawable.tag_ic);
//        put("Up",    R.drawable.tag_ic);
//        put("Up",    R.drawable.tag_ic);
//    }};


    public static class Tag{
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

    }

}
