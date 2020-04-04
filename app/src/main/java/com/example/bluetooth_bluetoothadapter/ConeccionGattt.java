package com.example.bluetooth_bluetoothadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ConeccionGattt extends AppCompatActivity {

    private String direccionMAC;

    public final static String KEY_BUNDLE_DIRECCION_MAC = "direccion_mac";

    public void Mensajito(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coneccion_gattt);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        direccionMAC = bundle.getString(KEY_BUNDLE_DIRECCION_MAC);
        Mensajito(direccionMAC);
    }
}


// BluetoothDevice device = bluetoothAdapter.getRemoteDevice(direcciones_dispocitivos.get(position));
// device.connectGatt(MainActivity.this, true, mBluetoothGattCallback);
// ParcelUuid[] moco = device.getUuids();
// mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
// Toast.makeText(MainActivity.this, direcciones_dispocitivos.get(position), Toast.LENGTH_SHORT).show();

    /*
    TODO: ESTA MIERDA NUNCA FUNCIONO!!!!
    BroadcastReceiver miReceptor = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, "onReceive!!!", Toast.LENGTH_SHORT).show();
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Mensajito("Encontramos un dispocitivo Bluetooth!!!!!!!");
            }
        }
    };
    */
