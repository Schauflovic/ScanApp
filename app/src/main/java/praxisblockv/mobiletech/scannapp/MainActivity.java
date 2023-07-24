package praxisblockv.mobiletech.scannapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
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
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        // Starte das WLAN-Scannen
        wifiManager.startScan();
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    private final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED);
                List<ScanResult> scanResults = wifiManager.getScanResults();
                Log.v("WifiScanTest", "" + scanResults);
                for (ScanResult scanResult : scanResults) {
                    String deviceName = scanResult.SSID;
                    String deviceAddress = scanResult.BSSID;

                    //Überprüfen, ob das Gerät bereits in der Liste ist (basierend auf dem Namen oder der Adresse)
                    if (!wifiItemList.contains(deviceName) && !wifiItemList.contains(deviceAddress)) {
                        // Gerät ist noch nicht in der Liste, füge es hinzu
                        wifiItemList.add("Name: " + deviceName + "\nMAC: " + deviceAddress);
                        Log.v("WifiScanTest", "Name: " + deviceName + "MAC: " + deviceAddress);
                    }
                }
                setWifiScansToListView();
            }
        }
    };

    void setWifiScansToListView() {
        wifiListViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wifiItemList);
        ListView listView = findViewById(R.id.wifiListView);
        listView.setAdapter(wifiListViewAdapter);
        wifiListViewAdapter.notifyDataSetChanged();
    }


    void stopWifiScan(){
        wifiListViewAdapter.clear();
        wifiItemList.clear();
        wifiListViewAdapter.notifyDataSetChanged();
    }
    //#endregion



    /*
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //PermissionsDenied = -1; PermissionGranted = 0
        //int hasBluetoothPermissions = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH);

        requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);


        if (requestCode == PERMISSION_REQUEST) {
            boolean accessGranted = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Log.w("onRequestPermissionsResult", permissions[i] +
                            " not Granted");
                    accessGranted = false;
                } else {
                    Log.w("onRequestPermissionsResult", "OK");
                }
            }
            if (accessGranted) {
                Log.w("onRequestPermissionsResult", "Starting Bluetooth Scan");
                checkAnStartBluetoothScan();
            }
        }
    }

    void checkAnStartBluetoothScan() {
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        proofIfBluetoothAdapterExists();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) return;
        bluetoothManager.getAdapter().startDiscovery();

        IntentFilter bluetoothFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothBroadcastReceiver, bluetoothFilter);
    }


    void proofIfBluetoothAdapterExists(){
        if (bluetoothManager.getAdapter().getState() != BluetoothAdapter.STATE_ON) {
            Log.i("MainActivity", "Bluetooth is deactivated");
            // Create an AlertDialogBuilder object.
            AlertDialog.Builder blDisableNotificationBuilder = new AlertDialog.Builder(this);
            // Add a message for the alert dialog
            blDisableNotificationBuilder.setMessage(R.string.bluetoothWarning);
            // Set the positive button only. Its just a notification.
            // So user choice ist not necessary
            blDisableNotificationBuilder.setPositiveButton(R.string.bluetoothOK,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            // Create and show the alert dialog
            AlertDialog blDisableNotification = blDisableNotificationBuilder.create();
            blDisableNotification.show();
        }
    }

    private final BroadcastReceiver bluetoothBroadcastReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action == BluetoothDevice.ACTION_FOUND) {
                        Log.d("bluetoothBroadcastReceiver", "Action Found triggered");
                        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(
                                BluetoothDevice.EXTRA_DEVICE);
                        if (bluetoothDevice != null) {
                            String deviceName = bluetoothDevice.getName();
                            String deviceAddress = bluetoothDevice.getAddress();
                            int deviceType = bluetoothDevice.getType();
                            String deviceTypeString = "Unknown";
                            switch (deviceType) {
                                case BluetoothDevice.DEVICE_TYPE_CLASSIC :
                                    deviceTypeString = "Classic"; break;
                                case BluetoothDevice.DEVICE_TYPE_LE:
                                    deviceTypeString = "LE"; break;
                                case BluetoothDevice.DEVICE_TYPE_DUAL:
                                    deviceTypeString = "DUAL"; break;
                                default:deviceTypeString = "Unknown";
                            }
                            Log.d("bluetoothBroadcastReceiver","Found Bluetooth Device: " + deviceName + "," + deviceAddress + ", Type: " + deviceTypeString);
                        }
                    } else if (action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {
                        bluetoothAdapter.startDiscovery();
                        //if(blScanActive) {}
                    }
                }
            };

    */


    void onSettingsButtonClick(){
        Intent mainIntent = new Intent(MainActivity.this, ChangePinActivity.class);
        startActivity(mainIntent);
    }

    void onQuitButtonClick(){
        finishAffinity();
        System.exit(0);
    }




}