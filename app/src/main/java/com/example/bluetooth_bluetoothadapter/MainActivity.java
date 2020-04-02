package com.example.bluetooth_bluetoothadapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private BluetoothAdapter bluetoothAdapter;
    private ListView listView_dispocitivos;
    private Button boton1, boton2, boton3, boton4;
    private ArrayList<String> nombres_dispocitivos;
    private ArrayList<String> direcciones_dispocitivos;

    private void activarBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Toast.makeText(MainActivity.this, "Activando Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "El Bluetooth ya estaba activado", Toast.LENGTH_SHORT).show();
        }
    }

    private void dicoveringBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            Toast.makeText(MainActivity.this, "startDiscovery()", Toast.LENGTH_SHORT).show();
            bluetoothAdapter.startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter);
        } else {
            Toast.makeText(MainActivity.this, "El Bluetooth esta apagado", Toast.LENGTH_SHORT).show();
        }
        /*
        if (!bluetoothAdapter.isDiscovering()) {
            //Toast.makeText(MainActivity.this, "Iniciando discovering", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(intent, REQUEST_DISCOVER_BT);
        } else {
            Toast.makeText(MainActivity.this, "discovering ya estaba activado", Toast.LENGTH_SHORT).show();
        }
         */
    }

    private void apagarBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            Toast.makeText(MainActivity.this, "Apagando Bluetooth", Toast.LENGTH_SHORT).show();
            bluetoothAdapter.disable();
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
                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, R.layout.formato_list_view, nombres_dispocitivos);
                for (BluetoothDevice device : pairedDevices) {
                    nombres_dispocitivos.add(device.getName());
                    direcciones_dispocitivos.add(device.getAddress());
                }
                listView_dispocitivos.setAdapter(adaptador);
            } else {
                Toast.makeText(MainActivity.this, "No hay dispocitivos encontrados", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Debe activar Bluetooth primero", Toast.LENGTH_SHORT).show();
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            // TODO: SOLUCIONAR ESTO: NO FUNCIONA, NUNCA LO HIZO Y NO TENGO IDEA PORQUE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                //String deviceHardwareAddress = device.getAddress(); // MAC address
                Toast.makeText(MainActivity.this, deviceName+"!!!!!!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Bluetooth encendido!!!", Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(MainActivity.this, "Este dispocitivo no tiene Bluetooth", Toast.LENGTH_LONG).show();
            return;
        }

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activarBluetooth();
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dicoveringBluetooth();
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
}
