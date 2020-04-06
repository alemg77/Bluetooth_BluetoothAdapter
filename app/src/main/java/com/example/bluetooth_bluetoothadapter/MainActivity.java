package com.example.bluetooth_bluetoothadapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAGMainActivity";
    private static final int REQUEST_ENABLE_BT = 0;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner mLeScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;

    private Handler mHandler;

    private ListView listView_dispocitivos;
    private Button boton1, boton2, boton3, boton4;
    private ArrayList<String> nombres_dispocitivos;
    private ArrayList<String> arrayListListView;
    private ArrayList<String> direcciones_dispocitivos;


    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Integer rssi = result.getRssi();
            Mensajito(" RSSI: "+rssi.toString());
            BluetoothDevice device = result.getDevice();
            String address = device.getAddress();

            // fixme: Falta registrar el dispocitivo encontrado
            /*
            BluetoothLeService mBluetoothLeService = new BluetoothLeService(address);
            mBluetoothLeService.conectarGatt();
            */

            String name = device.getName();
            Mensajito("Encontramos:  "+name);
            Log.d(TAG, "onScanResult: "+name);
            Log.d(TAG, "Address: "+address);
            Log.d(TAG, "Rssi: "+rssi.toString());
            // mRecyclerViewAdapter.addDevice(result.getDevice().getAddress());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Mensajito("onBatchScanResult");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            if ( ScanCallback.SCAN_FAILED_ALREADY_STARTED == errorCode )
            {
                Log.d(TAG,"El scan ya estaba activado");
                Mensajito("Ya estaba activo el scan!!");
            }
        }
    };

    public MainActivity() {
    }

    public void Mensajito(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void activarBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Mensajito("El Bluetooth ya estaba activado");
        }
    }

    private void buscarDispocitivosBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "Iniciando Bluetooth SCAN...");
            /*
            settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
            filters = new ArrayList<ScanFilter>();
            mLeScanner.startScan(filters, settings, mScanCallback);
             */
            mLeScanner.startScan(mScanCallback);
        } else {
            Mensajito("El Bluetooth esta apagado");
        }
    }

    private void apagarBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            Toast.makeText(MainActivity.this, "Apagando Bluetooth", Toast.LENGTH_SHORT).show();
            bluetoothAdapter.disable();
            arrayListListView = new ArrayList<>();
            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, R.layout.formato_list_view, arrayListListView);
            listView_dispocitivos.setAdapter(adaptador);
        } else {
            Toast.makeText(MainActivity.this, "El Bluetooth ya estaba apagado", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarDispocitivosBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                nombres_dispocitivos = new ArrayList<>();
                direcciones_dispocitivos = new ArrayList<>();
                arrayListListView = new ArrayList<>();
                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, R.layout.formato_list_view, arrayListListView);
                for (BluetoothDevice device : pairedDevices) {
                    String nombreDispocitivo = device.getName();
                    String direccionDispocitivo = device.getAddress().toString();
                    nombres_dispocitivos.add(device.getName());
                    direcciones_dispocitivos.add(device.getAddress());
                    arrayListListView.add(nombreDispocitivo + "\n" + direccionDispocitivo);
                }
                listView_dispocitivos.setAdapter(adaptador);
            } else {
                Mensajito("No hay dispocitivos encontrados");
            }
        } else {
            activarBluetooth();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    // Se activo el Bluetooth!!, hay que mostrar la lista actual de dispocitivos!!
                    actualizarDispocitivosBluetooth();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(MainActivity.this, "Cancelado", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "ERROR!!!", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                Toast.makeText(MainActivity.this, "?????", Toast.LENGTH_SHORT).show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boton1 = findViewById(R.id.button1);
        boton2 = findViewById(R.id.button2);
        boton3 = findViewById(R.id.button3);
        boton4 = findViewById(R.id.button4);
        listView_dispocitivos = findViewById(R.id.idListView);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        mLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        Log.d(TAG, "onCreate MainActivity");

        if (bluetoothAdapter == null) {
            Mensajito("Este dispocitivo no tiene Bluetooth");
            return;
        }

        // Use this check to determine whether BLE is supported on the device. Then you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Mensajito("Este dispocitivo no sopota BLE");
            finish();
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            actualizarDispocitivosBluetooth();
        }

        listView_dispocitivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( mLeScanner != null ) {
                    mLeScanner.stopScan(mScanCallback);
                }
                Bundle bundle = new Bundle();
                bundle.putString(ConeccionGattt.KEY_BUNDLE_DIRECCION_MAC, direcciones_dispocitivos.get(position));
                Intent intent = new Intent(MainActivity.this, ConeccionGattt.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activarBluetooth();
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarDispocitivosBluetooth();
            }
        });

        boton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apagarBluetooth();
            }
        });

        boton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDispocitivosBluetooth();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( mLeScanner != null) {
            mLeScanner.stopScan(mScanCallback);
        }


        // textView.setText(Html.fromHtml(newString));
    }
}
