package com.example.contactappuz.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.contactappuz.R;
import com.example.contactappuz.util.PermissionChecker;

import androidx.recyclerview.widget.BatchingListUpdateCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class Bluetooth {
    private static final String TAG = "BluetoothService";
    private static final int REQUEST_ENABLE_BT = 1;

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    byte[] buffer = new byte[1024];
    int bytes;
    String Data="";

    PermissionChecker permissionChecker;// = new PermissionChecker(this);

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice hc05;
    BluetoothSocket bluetoothSocket = null;

    @SuppressLint("MissingPermission")
    public Bluetooth(Context context) {
        Activity activity = (Activity) context;
        permissionChecker = new PermissionChecker(context);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Urządzenie nie ma modułu Bluetooth", Toast.LENGTH_LONG).show();
            activity.finish();      //TODO warto zareagować na to lepiej
            return;
        }
        permissionChecker.checkPermissions();       //TODO do poprawy - potrzebna obsługa przypadku z niepowodzeniem
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);     //TODO do rozwiązania
        }
        hc05 = bluetoothAdapter.getRemoteDevice("");//TODO do poprawy - szukaj i sparuj urządzenia
    }

    @SuppressLint("MissingPermission")
    public void mbtnConnect(){
        try {
            permissionChecker.checkPermissions();
            bluetoothSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
            bluetoothSocket.connect();
//                    touchEventHelper = new TouchEventHelper(bluetoothSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void mbtnStop(){
        try {
            OutputStream outputStream = bluetoothSocket.getOutputStream();
            outputStream.write(83);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void mbtnCollect(){
        try {
            OutputStream outputStream = bluetoothSocket.getOutputStream();
            outputStream.write(67);   //C

            AsyncTask.execute(new Runnable() {

                @Override
                public void run() {
                    InputStream inputStream = null;
                    try {
                        inputStream = bluetoothSocket.getInputStream();
                        bytes = inputStream.read(buffer);
                        String incomingMessage = new String(buffer, 0, bytes);
                        Data= incomingMessage.replaceAll("(\\r|\\n)", "");
                        Log.d("Bluetooth_data", incomingMessage);
                        System.out.println(Data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
