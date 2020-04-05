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
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ConeccionGattt extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String direccionMAC;


    public final static UUID UUID_A6_1 = UUID.fromString("00000002-0001-1111-2222-333333333333");

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothDevice mBluetoothDevice;

    public final static String KEY_BUNDLE_DIRECCION_MAC = "direccion_mac";

    private Button boton;


    public void Mensajito(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }


    private BluetoothProfile.ServiceListener profileListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Mensajito("onServiceConnected");
        }

        public void onServiceDisconnected(int profile) {
            Mensajito("onServiceDisconnected");
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coneccion_gattt);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        direccionMAC = bundle.getString(KEY_BUNDLE_DIRECCION_MAC);


        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter  = bluetoothManager.getAdapter();
        mBluetoothAdapter.getProfileProxy(this, profileListener, 0 );

        boton = findViewById(R.id.idbutton);

        if (mBluetoothAdapter.isEnabled()) {
            // mBluetoothAdapter.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        } else {
            Mensajito("ERROR: El Bluetooth esta desactivado.");
        }

        BluetoothLeService mBluetoothLeService = new BluetoothLeService(direccionMAC);
        mBluetoothLeService.conectarGatt();


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mensajito("Nada para ti");
                /*
                ParcelUuid[] parcelUuids = mBluetoothDevice.getUuids();
                if (parcelUuids == null) {
                } else {
                    Mensajito("Algo paso");
                }
                 */
            }
        });
    }
}
