package com.example.contactappuz.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import com.example.contactappuz.logic.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class ConnectBluetoothThread extends Thread{
    private static final String TAG = "ConnectBtThread";
    private final UUID mUUID;

    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter bluetoothAdapter;

    @SuppressLint("MissingPermission")
    public ConnectBluetoothThread(BluetoothDevice device, BluetoothAdapter bluetoothAdapter) {
// Use a temporary object that is later assigned to mmSocket
// because mmSocket is final.
        mUUID = BluetoothManager.mUUID;
        this.bluetoothAdapter = bluetoothAdapter;

        BluetoothSocket tmp = null;
        mmDevice = device;

        try {
// Get a BluetoothSocket to connect with the given BluetoothDevice.
// mUUID is the app's UUID string, also used in the server code.
            tmp = device.createRfcommSocketToServiceRecord(mUUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    @SuppressLint("MissingPermission")
    public void run() {
// Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter.cancelDiscovery();
        try {
// Connect to the remote device through the socket. This call blocks
// until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
// Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }
// The connection attempt succeeded. Perform work associated with
// the connection in a separate thread.
        //TODO manageMyConnectedSocket(mmSocket);
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}