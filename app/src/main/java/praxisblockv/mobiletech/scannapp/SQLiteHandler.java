package praxisblockv.mobiletech.scannapp;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.Nullable;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

public class SQLiteHandler extends SQLiteOpenHelper implements DatabaseHandler{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "devices_db";
    private static final String TABLE_DEVICES = "devices";
    private static final String KEY_ID = "id";
    private static final String KEY_DEVICE_TIMESTAMP = "device_timestamp";
    private static final String KEY_DEVICE_TYPE = "device_type";
    private static final String KEY_DEVICE_NAME = "device_name";
    private static final String KEY_DEVICE_ADDRESS = "device_address";
    private static final String KEY_DEVICE_LAT_COORD = "device_lat";
    private static final String KEY_DEVICE_LON_COORD = "device_lon";

    public SQLiteHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase.loadLibs(context);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_DEVICES_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_DEVICES + "("
                        + KEY_ID + " INTEGER PRIMARY KEY,"
                        + KEY_DEVICE_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                        + KEY_DEVICE_TYPE + " TEXT,"
                        + KEY_DEVICE_NAME + " TEXT,"
                        + KEY_DEVICE_ADDRESS + " TEXT,"
                        + KEY_DEVICE_LAT_COORD + " REAL, " // oder INTEGER, je nach Präzision
                        + KEY_DEVICE_LON_COORD + " REAL"  // oder INTEGER, je nach Präzision
                        + ")";
        sqLiteDatabase.execSQL(CREATE_DEVICES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void writeData(String type, String name, String address, Long lat, Long lon) {
        SQLiteDatabase db = this.getWritableDatabase("123");
        ContentValues values = new ContentValues();

        values.put(KEY_DEVICE_TYPE, type);
        values.put(KEY_DEVICE_NAME, name);
        values.put(KEY_DEVICE_ADDRESS, address);
        values.put(KEY_DEVICE_LAT_COORD, lat);
        values.put(KEY_DEVICE_LON_COORD, lon);

        db.insert(TABLE_DEVICES, null, values);
        db.close();
    }

    @Override
    public void updateData() {

    }

    @Override
    public Device readData() {
        return null;
    }

    @Override
    public void deleteData() {

    }
}
