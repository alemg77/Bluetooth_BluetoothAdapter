package com.example.bluetooth_bluetoothadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CaracteristicasGatt extends AppCompatActivity {

    public final static String KEY_BUNDLE_UUID = "uuid";
    private ListView listViewCaracteristicas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caracteristicas_gatt);
        listViewCaracteristicas = findViewById(R.id.idListViewCaracteristicas);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();


        List<String> listaCaracteristicasGatt = new ArrayList<>();
        Set<String> UUIDs = bundle.keySet();
        for (String uuid : UUIDs) {
            String propiedades = bundle.get(uuid).toString();
            Integer properties = Integer.parseInt(propiedades);
            String stringListView = uuid;

            if ((properties & BluetoothGattCharacteristic.PROPERTY_WRITE) != 0x0) {
                String texto = "\n   PROPERTY_WRITE";
                stringListView = stringListView + texto;
            }
            if ((properties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0x0) {
                String texto = "\n   PROPERTY_NOTIFY";
                stringListView = stringListView + texto;
            }
            if ((properties & BluetoothGattCharacteristic.PERMISSION_READ) != 0x0) {
                String texto = "\n   PROPERTY_READ";
                stringListView = stringListView + texto;
            }
            if ((properties & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0x0) {
                String texto = "\n   PROPERTY_INDICATE";
                stringListView = stringListView + texto;
            }
            if ((properties & BluetoothGattCharacteristic.PROPERTY_BROADCAST) != 0x0) {
                String texto = "\n   PROPERTY_BROADCAST";
                stringListView = stringListView + texto;
            }
            if ((properties & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) != 0x0) {
                String texto = "\n   PROPERTY_WRITE_NO_RESPONSE";
                stringListView = stringListView + texto;
            }
            if ((properties & BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE) != 0x0) {
                String texto = "\n   PROPERTY_SIGNED_WRITE";
                stringListView = stringListView + texto;
            }
            if ((properties & BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS) != 0x0) {
                String texto = "\n   PROPERTY_EXTENDED_PROPS";
                stringListView = stringListView + texto;
            }
            if ((properties & BluetoothGattCharacteristic.PERMISSION_READ) != 0x0) {
                String texto = "\n   PERMISSION_READ";
                stringListView = stringListView + texto;
            }
            if ((properties & BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT) != 0x0) {
                String texto = "\n   WRITE_TYPE_DEFAULT";
                stringListView = stringListView + texto;
            }
            if ((properties & BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE) != 0x0) {
                String texto = "\n   WRITE_TYPE_NO_RESPONSE";
                stringListView = stringListView + texto;
            }
            if ((properties & BluetoothGattCharacteristic.WRITE_TYPE_SIGNED) != 0x0) {
                String texto = "\n   WRITE_TYPE_NO_RESPONSE";
                stringListView = stringListView + texto;
            }


            /*
            PERMISSION_READ_ENCRYPTED
            PERMISSION_READ_ENCRYPTED_MITM
            PERMISSION_WRITE
            PERMISSION_WRITE_ENCRYPTED
            PERMISSION_WRITE_ENCRYPTED_MITM
            PERMISSION_WRITE_SIGNED
            PERMISSION_WRITE_SIGNED_MITM
            */

            listaCaracteristicasGatt.add(stringListView);
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(CaracteristicasGatt.this, R.layout.formato_list_view, listaCaracteristicasGatt);
        listViewCaracteristicas.setAdapter(adaptador);


    }
}




