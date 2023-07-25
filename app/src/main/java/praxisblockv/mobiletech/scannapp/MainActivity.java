package praxisblockv.mobiletech.scannapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button settingsButton, quitButton, startStopButton;
    private boolean isScanning = true;
    private ArrayList<Device> deviceList = new ArrayList<>();

    private ArrayList<String> bluetoothItemList = new ArrayList<>();
    private ArrayAdapter<String> bluetoothListViewAdapter;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;


    private ArrayList<String> wifiItemList = new ArrayList<>();
    private ArrayAdapter<String> wifiListViewAdapter;
    private WifiManager wifiManager;
    private List<ScanResult> scanResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startStopButton = findViewById(R.id.startStopButton);
        settingsButton = findViewById(R.id.settingsBtn);
        quitButton = findViewById(R.id.quitBtn);

        startStopButton.setText("Start");



        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isScanning) {
                    startStopButton.setText("Stop");
                    startBluetoothScan();
                    startWifiScan();
                    isScanning = false;
                } else if (!isScanning) {
                    startStopButton.setText("Start");
                    stopBluetoothScan();
                    stopWifiScan();
                    isScanning = true;
                }
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSettingsButtonClick();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onQuitButtonClick();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
    }

    //#region Bluetooth
    void startBluetoothScan() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            // Das Gerät unterstützt kein Bluetooth
            Log.e("Bluetooth", "Bluetooth wird nicht unterstützt");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            // Wenn Bluetooth deaktiviert ist, fordere den Benutzer auf, es zu aktivieren.
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                ;
            startActivityForResult(enableBtIntent, 0);
        } else {
            // Starte die Gerätesuche
            startDiscovery();
        }
    }

    void stopBluetoothScan() {
        bluetoothListViewAdapter.clear();
        bluetoothItemList.clear();
        bluetoothListViewAdapter.notifyDataSetChanged();
        unregisterReceiver(bluetoothReceiver);
    }

    private void startDiscovery() {
        // Registriere den BroadcastReceiver, um die Ergebnisse des Gerätesuchens zu erhalten
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothReceiver, filter);

        // Starte die Gerätesuche
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED);
        bluetoothAdapter.startDiscovery();
    }


    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Ein Bluetooth-Gerät wurde gefunden
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                    ;
                String deviceName = device.getName();
                String deviceAddress = device.getAddress();

                //Überprüfen, ob das Gerät bereits in der Liste ist (basierend auf dem Namen oder der Adresse)
                if (!bluetoothItemList.contains(deviceName) && !bluetoothItemList.contains(deviceAddress)) {
                    // Gerät ist noch nicht in der Liste, füge es hinzu
                    bluetoothItemList.add("Name: " + deviceName + "\nMAC: " + deviceAddress);
                }
            }
            setBluetoothScansToListView();
        }
    };

    void setBluetoothScansToListView() {
        bluetoothListViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bluetoothItemList);
        ListView listView = findViewById(R.id.bluetoothListView);
        listView.setAdapter(bluetoothListViewAdapter);
        bluetoothListViewAdapter.notifyDataSetChanged();
    }
    //#endregion

    //#region WLAN
    void startWifiScan() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        registerReceiver(wifiBroadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }

        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }

       //wifiManager.disconnect();
        wifiManager.startScan();

    }

    private final BroadcastReceiver wifiBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                scanResults = wifiManager.getScanResults();


                setWifiScansToListView();

            }
        }
    };

    void setWifiScansToListView() {
        wifiItemList.clear();
        for (ScanResult wifiAPResult : scanResults) {
            String deviceName = wifiAPResult.SSID;
            String deviceAddress = wifiAPResult.BSSID;
            wifiItemList.add("Name: " + deviceName + "\nMAC: " + deviceAddress);
        }

        wifiListViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wifiItemList);
        ListView listView = findViewById(R.id.wifiListView);
        listView.setAdapter(wifiListViewAdapter);
        wifiListViewAdapter.notifyDataSetChanged();
    }


    void stopWifiScan(){
        wifiListViewAdapter.clear();
        wifiItemList.clear();
        wifiListViewAdapter.notifyDataSetChanged();
        unregisterReceiver(wifiBroadcastReceiver);
    }
    //#endregion


    void onSettingsButtonClick(){
        Intent mainIntent = new Intent(MainActivity.this, ChangePinActivity.class);
        startActivity(mainIntent);
    }

    void onQuitButtonClick(){
        finishAffinity();
        System.exit(0);
    }




}