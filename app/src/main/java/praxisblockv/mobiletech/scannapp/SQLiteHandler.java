package praxisblockv.mobiletech.scannapp;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.Nullable;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase("123");
        db.delete(TABLE_DEVICES, null, null);
        db.close();
    }

    public List<String> getAllData() {
        List<String> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase("123");
        String[] columns = {KEY_DEVICE_TIMESTAMP, KEY_DEVICE_TYPE, KEY_DEVICE_NAME, KEY_DEVICE_ADDRESS, KEY_DEVICE_LAT_COORD, KEY_DEVICE_LON_COORD};
        Cursor cursor = db.query(TABLE_DEVICES, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Check if the column exists in the cursor before accessing its value
                int timestampIndex = cursor.getColumnIndex(KEY_DEVICE_TIMESTAMP);
                int deviceTypeIndex = cursor.getColumnIndex(KEY_DEVICE_TYPE);
                int deviceNameIndex = cursor.getColumnIndex(KEY_DEVICE_NAME);
                int deviceAddressIndex = cursor.getColumnIndex(KEY_DEVICE_ADDRESS);
                int latCoordIndex = cursor.getColumnIndex(KEY_DEVICE_LAT_COORD);
                int lonCoordIndex = cursor.getColumnIndex(KEY_DEVICE_LON_COORD);

                if (timestampIndex >= 0) {
                    String timestamp = cursor.getString(timestampIndex);
                    String deviceType = cursor.getString(deviceTypeIndex);
                    String deviceName = cursor.getString(deviceNameIndex);
                    String deviceAddress = cursor.getString(deviceAddressIndex);
                    String latCoord = cursor.getString(latCoordIndex);
                    String lonCoord = cursor.getString(lonCoordIndex);

                    // Build a string representing the data and add it to the list
                    String dataEntry = timestamp + "," + deviceType + "," + deviceName + "," + deviceAddress + "," + latCoord + "," + lonCoord;
                    dataList.add(dataEntry);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return dataList;
    }

}
