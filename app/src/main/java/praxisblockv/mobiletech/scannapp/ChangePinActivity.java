package praxisblockv.mobiletech.scannapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePinActivity extends AppCompatActivity {

    private Button changeBtn, goToMainBtn;
    private EditText oldPin, newPin;
    private String oldPinText, newPinText;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_settings);

        changeBtn = findViewById(R.id.changePinBtn);
        goToMainBtn = findViewById(R.id.goBackToMainBtn);

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proofOldPin();
            }
        });

        goToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToMainActivity();
            }
        });
    }

    private void proofOldPin(){
        oldPin = findViewById(R.id.oldPinTxt);
        oldPinText = oldPin.getText().toString();

        newPin = findViewById(R.id.newPinTxt);
        newPinText = newPin.getText().toString();

        if(oldPinText.equals(getPinFromSharedPreferences())) {
            setPinToSharedPreferences(newPin.getText().toString());
            moveToMainActivity();
        }else{
            Toast.makeText(this, "old Pin is wrong!", Toast.LENGTH_SHORT).show();
        }
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

    void moveToMainActivity(){
        Intent mainIntent = new Intent(ChangePinActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
}
