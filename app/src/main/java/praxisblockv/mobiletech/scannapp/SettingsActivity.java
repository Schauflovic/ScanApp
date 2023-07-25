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

    private MySharedPreferences mySharedPreferences;


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
        mySharedPreferences = MySharedPreferences.getInstance(ChangePinActivity.this);
        oldPin = findViewById(R.id.oldPinTxt);
        oldPinText = oldPin.getText().toString();

        newPin = findViewById(R.id.newPinTxt);
        newPinText = newPin.getText().toString();

        if(oldPinText.equals(mySharedPreferences.getPIN())) {
            mySharedPreferences.savePIN(newPin.getText().toString());
            moveToMainActivity();
        }else{
            Toast.makeText(this, "old Pin is wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    void onExportDataButtonClick(){
        SQLiteHandler sqliteHandler = new SQLiteHandler(this);
        sqliteHandler.getAllData();
    }

    void moveToMainActivity(){
        Intent mainIntent = new Intent(ChangePinActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
}
