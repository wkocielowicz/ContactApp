package com.example.contactappuz.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.contactappuz.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Bluetooth {
    private static final String TAG = "BluetoothService";
    private static final int REQUEST_ENABLE_BT = 1;

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    byte[] buffer = new byte[1024];
    int bytes;
    String Data="";

    //todo Send
    /*
    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> deviceListAdapter;
    private ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
    private ListView deviceListView;
    //private String uuidString = "INSERT_YOUR_UUID_HERE";
    private BluetoothDevice selectedDevice;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private volatile boolean stopWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceListView = findViewById(R.id.deviceListView);
        deviceListView.setAdapter(deviceListAdapter);
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDevice = deviceList.get(position);
                connectToDevice();
            }
        });
    }

    public void send(View view) {
        String message = "To jest testowy tekst";

        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDiscoverClick(View view) {
        if (!bluetoothAdapter.isDiscovering()) {
            deviceList.clear();
            deviceListAdapter.clear();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(broadcastReceiver, filter);
            bluetoothAdapter.startDiscovery();
        }
    }
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceList.add(device);
                deviceListAdapter.add(device.getName() + "\n" + device.getAddress());
                deviceListAdapter.notifyDataSetChanged();
            }
        }
    };

    public void connectToDevice() {
        socket = null;
        try {
            socket = selectedDevice.createRfcommSocketToServiceRecord(mUUID);
            socket.connect();
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            beginListenForData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
    //todo Recive
    /*
    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver broadcastReceiver;
    private AcceptThread accept;
    private ConnectedThread connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Zainicjuj BroadcastReceiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Znaleziono urządzenie Bluetooth
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Toast.makeText(context, "Znaleziono urządzenie: " + device.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Uruchom wątek nasłuchujący
        accept = new AcceptThread();
        accept.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accept.cancel();
        if (connected != null) {
            connected.cancel();
        }
    }

    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;
        private BluetoothSocket socket;
        private InputStream inputStream;
        public AcceptThread() {
            try {
                // Utwórz serwer Bluetooth
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Nazwa usługi", mUUID); //TODO uuid
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                // Oczekuj na połączenie z urządzeniem
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();

                // Odczytuj dane z wejścia
                byte[] buffer = new byte[1024];
                int bytes;
                while (true) {
                    bytes = inputStream.read(buffer);
                    String message = new String(buffer, 0, bytes);
                    //runOnUiThread(() -> {
                    //    textViewReceived.setText(message);
                    //    Toast.makeText(ReceiveActivity.this, "Otrzymano wiadomość: " + message, Toast.LENGTH_SHORT).show();
                    //});
                    Toast.makeText(ReceiveActivity.this, "Otrzymano wiadomość: " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectedThread extends Thread {
        private BluetoothSocket socket;
        private OutputStream outputStream;

        public ConnectedThread(BluetoothSocket socket) {
            try {
                this.socket = socket;
                this.outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                String message = "Wiadomość testowa";
                outputStream.write(message.getBytes());
                outputStream.flush();
                runOnUiThread(() -> Toast.makeText(ReceiveActivity.this, "Wysłano wiadomość: " + message, Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onSendClick(View view) {
        if (bluetoothAdapter.isEnabled()) {
            // Sprawdź, czy urządzenie jest sparowane
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                BluetoothDevice device = pairedDevices.iterator().next();
                try {
                    // Utwórz socket Bluetooth i nawiąż połączenie z urządzeniem
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(mUUID));  //TODO uuid
                    socket.connect();

                    // Uruchom wątek do wysyłania wiadomości
                    connected = new ConnectedThread(socket);
                    connected.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onDiscoverClick(View view) {
            if (!bluetoothAdapter.isDiscovering()) {
// Wyszukaj urządzenia Bluetooth
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(broadcastReceiver, filter);
                bluetoothAdapter.startDiscovery();
            }
        }

        public void onCancelDiscoveryClick(View view) {
            if (bluetoothAdapter.isDiscovering()) {
// Przerwij wyszukiwanie urządzeń Bluetooth
                bluetoothAdapter.cancelDiscovery();
                unregisterReceiver(broadcastReceiver);
            }
        }
*/
    //todo END

    PermissionChecker permissionChecker;// = new PermissionChecker(this);

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice selectedDevice;
    private BluetoothSocket bluetoothSocket = null;
    private ArrayAdapter<String> deviceListAdapter;
    private ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
    private ListView deviceListView;

    //private String uuidString = "INSERT_YOUR_UUID_HERE";
    private OutputStream outputStream;
    private InputStream inputStream;
    private volatile boolean stopWorker;


    @SuppressLint("MissingPermission")
    public Bluetooth(Context context) {
        Activity activity = (Activity) context;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Urządzenie nie ma modułu Bluetooth", Toast.LENGTH_LONG).show();
            activity.finish();      //TODO warto zareagować na to lepiej
            return;
        }
        permissionChecker = new PermissionChecker(context);
        permissionChecker.checkPermissions();       //TODO do poprawy - potrzebna obsługa przypadku z niepowodzeniem


        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);     //TODO do rozwiązania
        }
        //selectedDevice = bluetoothAdapter.getRemoteDevice("");//TODO do poprawy - szukaj i sparuj urządzenia


        deviceListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        deviceListView = activity.findViewById(R.id.deviceListView);
        deviceListView.setAdapter(deviceListAdapter);
        deviceListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedDevice = deviceList.get(position);

            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getTheme();
            theme.resolveAttribute(android.R.attr.colorControlHighlight, typedValue, true);
            int colorAccent = typedValue.data;
            theme.resolveAttribute(android.R.attr.colorControlNormal, typedValue, true);
            int colorBackground = typedValue.data;

            for(int i=0; i<parent.getChildCount(); i++) {
                if(i != position) {
                    parent.getChildAt(i).setBackgroundColor(colorBackground);
                }
            }
            view.setBackgroundColor(colorAccent);
            //wyzerowanie koloru tła dla innych elementów
            //connectToDevice();
            //mbtnConnect();        //todo pora na wciśnięcie połączenia
        });

    }

    @SuppressLint("MissingPermission")
    public void mbtnDiscover(Activity activity) {
        if (!bluetoothAdapter.isDiscovering()) {
            deviceList.clear();
            deviceListAdapter.clear();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                activity.registerReceiver(broadcastReceiver, filter);
                bluetoothAdapter.startDiscovery();
        }
    }


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceList.add(device);
                deviceListAdapter.add(device.getName() + "\n" + device.getAddress());
                deviceListAdapter.notifyDataSetChanged();
            }
        }
    };

    public void closeBluetoothSocket(){
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public void mbtnConnect(){
        //bluetoothSocket = null;   //todo nie wiem, co to zmieni (sprawdzę późńiej)
        try {
            permissionChecker.checkPermissions();
            bluetoothSocket = selectedDevice.createRfcommSocketToServiceRecord(mUUID);
        //    bluetoothSocket.connect();
//                    touchEventHelper = new TouchEventHelper(bluetoothSocket);
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
