package rtapps.app.data_bases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import rtapps.app.network.responses.AllMessagesResponse;

/**
 * Created by tazo on 30/07/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "rtAppsDB.db";

    private static final int DATABASE_VERSION = 3;
    private static final String SALES_TABLE_NAME = "sales";

    private static final String SALES_MESSAGE_ID = "_message_id";
    private static final String SALES_CREATION_DATE = "_creation_date";
    private static final String SALES_TITLE = "_message_title";
    private static final String SALES_BODY = "_message_body";
    private static final String SALES_IMAGE_ID = "_image_id";
    private static final String SALES_LAST_UPDATED_TIME= "_last_updated_time";
    private static final String SALES_EXIST = "_exist";

    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + SALES_TABLE_NAME + " (" +
                    SALES_MESSAGE_ID + "  TEXT PRIMARY KEY NOT NULL," +
                    SALES_CREATION_DATE + "DOUBLE," +
                    SALES_TITLE + " TEXT," +
                    SALES_BODY + " TEXT," +
                    SALES_LAST_UPDATED_TIME + " DOUBLE," +
                    SALES_EXIST + " BOOLEAN," +
                    SALES_IMAGE_ID + " TEXT" +
                    ");";

    String[] COLUMNS = {SALES_MESSAGE_ID , SALES_TITLE , SALES_BODY , SALES_LAST_UPDATED_TIME , SALES_EXIST, SALES_IMAGE_ID};




    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNewMessage( AllMessagesResponse.Message message){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SALES_MESSAGE_ID, message.getId());
     //   values.put(SALES_CREATION_DATE, message.getCreationDate());
        values.put(SALES_TITLE, message.getHeader());
        values.put(SALES_BODY, message.getBody());
        values.put(SALES_IMAGE_ID, message.getFileUrl());
        values.put(SALES_LAST_UPDATED_TIME, message.getLastUpdateDate());
        values.put(SALES_EXIST, message.getExist());

        long newRowId;
        newRowId = db.insertWithOnConflict(SALES_TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("DAtaBaseHelper" , "Insert new Message row id = " + newRowId);
    }

    public Cursor getAllMessagesFromDB(){
        SQLiteDatabase db = getWritableDatabase();
         return db.query(
                SALES_TABLE_NAME,  // The table to query
                COLUMNS,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                // SALES_CREATION_DATE + " DESC"                                 // The sort order
                 null
        );


    }


}
