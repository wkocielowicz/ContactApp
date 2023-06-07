package com.example.contactappuz.util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.contactappuz.logic.BluetoothManager;

import java.io.IOException;
import java.util.UUID;

@SuppressLint("MissingPermission")
public class AcceptBluetoothThread extends Thread{
    private static final String TAG = "AcceptBtThread";
    private final UUID mUUID;
    private final String NAME = "Nazwa usługi";

    private final BluetoothServerSocket mmServerSocket;

    public AcceptBluetoothThread(BluetoothAdapter bluetoothAdapter) {
        mUUID = BluetoothManager.mUUID;
// Use a temporary object that is later assigned to mmServerSocket
// because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        try {
// mUUID is the app's UUID string, also used by the client code.
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, mUUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
// Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
                break;
            }
            if (socket != null) {
// A connection was accepted. Perform work associated with
// the connection in a separate thread.
                //TODO manageMyConnectedSocket(socket);
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
