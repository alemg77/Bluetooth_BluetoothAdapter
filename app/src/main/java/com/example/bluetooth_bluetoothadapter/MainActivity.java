package com.example.bluetooth_bluetoothadapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGattCallback mBluetoothGattCallback;
    private ListView listView_dispocitivos;
    private Button boton1, boton2, boton3, boton4;
    private ArrayList<String> nombres_dispocitivos;
    private ArrayList<String> arrayListListView;
    private ArrayList<String> direcciones_dispocitivos;

/*
TODO: HACER QUE FUNCIONE ESTO:
    private LeDeviceListAdapter leDeviceListAdapter;
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            leDeviceListAdapter.addDevice(device);
                            leDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

 */

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
            Mensajito("Esto no funciona por ahora....");
            /*
            TODO: Arreglar esto:
            // bluetoothAdapter.startLeScan(leScanCallback);
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(miReceptor, filter);
             */
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

        final BluetoothManager bluetoothManager =(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter  = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            Mensajito("Este dispocitivo no tiene Bluetooth");
            return;
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
}
