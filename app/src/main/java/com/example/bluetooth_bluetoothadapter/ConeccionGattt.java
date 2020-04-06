package com.example.bluetooth_bluetoothadapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConeccionGattt extends AppCompatActivity {

    public final static String KEY_BUNDLE_DIRECCION_MAC = "direccion_mac";
    private static final String TAG = "ConeccionGatt";
    private String direccionMAC;
    private Button botonActualizarListaSericiosGatt;
    private ListView listViewServiciosGatt;

    // public final static UUID UUID_A6_1 = UUID.fromString("00000002-0001-1111-2222-333333333333");

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeService mBluetoothLeService;


    public void Mensajito(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private BluetoothProfile.ServiceListener profileListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int profile, BluetoothProfile proxy) { Mensajito("onServiceConnected"); }

        public void onServiceDisconnected(int profile) { Mensajito("onServiceDisconnected"); }
    };

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Mensajito("Se ejecuto mBroadcastReceiver1!!!");
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Mensajito(" BroadcastReceiver onReceive");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
    }

    private void ActualizarListaServicios() {
        List<String> listaServiciosGatt = new ArrayList<>();
        List<BluetoothGattService> gattServices = mBluetoothLeService.getServiciosGatt();

        if (gattServices == null) {
            Mensajito("Aun no se encontraron servicios");
            return;
        }
        for (BluetoothGattService gattService : gattServices) {
            listaServiciosGatt.add(gattService.getUuid().toString());
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(ConeccionGattt.this, R.layout.formato_list_view, listaServiciosGatt);
        listViewServiciosGatt.setAdapter(adaptador);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coneccion_gattt);
        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        direccionMAC = bundle.getString(KEY_BUNDLE_DIRECCION_MAC);

        botonActualizarListaSericiosGatt = findViewById(R.id.idBotonActualizarListaGatt);
        listViewServiciosGatt = findViewById(R.id.idListaServiciosGatt);

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothAdapter.getProfileProxy(this, profileListener, 0);

        if (mBluetoothAdapter.isEnabled()) {
            // mBluetoothAdapter.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        } else {
            Mensajito("ERROR: El Bluetooth esta desactivado.");
        }
        mBluetoothLeService = new BluetoothLeService(direccionMAC);
        mBluetoothLeService.conectarGatt();

        botonActualizarListaSericiosGatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarListaServicios();
            }
        });

        listViewServiciosGatt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<BluetoothGattService> gattServices = mBluetoothLeService.getServiciosGatt();
                BluetoothGattService bluetoothGattService = gattServices.get(position);
                List<BluetoothGattCharacteristic> gattCharacteristics = bluetoothGattService.getCharacteristics();
                Bundle bundle = new Bundle();
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    UUID uuid1 = gattCharacteristic.getUuid();
                    Integer properties = gattCharacteristic.getProperties();
                    bundle.putString(uuid1.toString(),properties.toString());
                }
                Intent intent = new Intent(ConeccionGattt.this, CaracteristicasGatt.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
