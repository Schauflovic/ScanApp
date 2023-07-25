package praxisblockv.mobiletech.scannapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Button changeBtn, goToMainBtn, deleteDataBtn, exportDataBtn;
    private EditText oldPin, newPin;
    private String oldPinText, newPinText;

    private MySharedPreferences mySharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_settings);

        changeBtn = findViewById(R.id.changePinBtn);
        goToMainBtn = findViewById(R.id.goBackToMainBtn);
        deleteDataBtn = findViewById(R.id.deleteDataButton);
        exportDataBtn = findViewById(R.id.exportDataButton);

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

        deleteDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDataFromDatabase();
            }
        });

        exportDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportDataToTxtFile();
            }
        });
    }

    private void proofOldPin(){
        mySharedPreferences = MySharedPreferences.getInstance(SettingsActivity.this);
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



    private void exportDataToTxtFile() {
        SQLiteHandler sqliteHandler = new SQLiteHandler(this);
        List<String> dataList = sqliteHandler.getAllData();

        try {
            FileOutputStream fos = openFileOutput("scanData.txt", Context.MODE_PRIVATE);
            for (String dataEntry : dataList) {
                fos.write(dataEntry.getBytes());
                fos.write("\n".getBytes()); // Add newline after each data entry
            }
            fos.close();
            Toast.makeText(this, "Data exported to " + getFilesDir() + "/scanData.txt", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error exporting data", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteDataFromDatabase(){
        SQLiteHandler sqLiteHandler = new SQLiteHandler(this);
        sqLiteHandler.deleteAllData();
    }


    void moveToMainActivity(){
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
}
