package praxisblockv.mobiletech.scannapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PinActivity extends AppCompatActivity {

    private EditText pinTextField;
    private String pin;
    private Button submitPinButton;
    private SharedPreferences sharedPreferences;
    private MySharedPreferences mySharedPreferences;

    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_pin);

        submitPinButton = findViewById(R.id.submitPinBtn);

        submitPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proofPin();
            }
        });
    }

    void proofPin(){
        mySharedPreferences = MySharedPreferences.getInstance(PinActivity.this);
        pinTextField = findViewById(R.id.PinTextField);
        pin = pinTextField.getText().toString();

        if(mySharedPreferences.getPIN().equals("")){
            mySharedPreferences.savePIN(pin);
            Intent mainIntent = new Intent(PinActivity.this, MainActivity.class);
            startActivity(mainIntent);
        }else if(pin.equals(mySharedPreferences.getPIN())){
            Intent mainIntent = new Intent(PinActivity.this, MainActivity.class);
            startActivity(mainIntent);
        }else{
            Toast.makeText(this, "you entered the wrong PIN!", Toast.LENGTH_SHORT).show();
        }
        /*
        if(getPinFromSharedPreferences().equals("")){
            setPinToSharedPreferences(pin);
            Intent mainIntent = new Intent(PinActivity.this, MainActivity.class);
            startActivity(mainIntent);
        }else if(pin.equals(getPinFromSharedPreferences())){
            Intent mainIntent = new Intent(PinActivity.this, MainActivity.class);
            startActivity(mainIntent);
        }else{
            Toast.makeText(this, "you entered the wrong PIN!", Toast.LENGTH_SHORT).show();
        }*/
    }

    private String getPinFromSharedPreferences(){
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        String pinValue = sharedPreferences.getString(getString(R.string.PinPrefsKey), "");
        return pinValue;
    }

    private void setPinToSharedPreferences(String pin){
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(getString(R.string.PinPrefsKey),pin);
        edit.apply();
    }
}
