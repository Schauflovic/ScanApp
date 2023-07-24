package praxisblockv.mobiletech.scannapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreferences {

    private static MySharedPreferences instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_KEY_PIN = "PinKey";

    private MySharedPreferences(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public static MySharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new MySharedPreferences(context);
        }
        return instance;
    }

    public void savePIN(String pin){
        editor.putString(PREF_KEY_PIN, pin);
        editor.apply();
    }

    public String getPIN(){
        return sharedPreferences.getString(PREF_KEY_PIN, "");
    }
}
