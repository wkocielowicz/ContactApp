package com.example.contactappuz.logic;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.contactappuz.R;
import com.example.contactappuz.util.ConnectedBluetoothThread;
import com.example.contactappuz.util.PermissionChecker;
import com.example.contactappuz.util.enums.BluetoothSearchTypeFlag;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class BluetoothManager {
    private static final String TAG = "BluetoothManager";
    private static final int REQUEST_ENABLE_BT = 1;

    public interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;
    }
    private ConnectedBluetoothThread connectedThread;

    public static final UUID mUUID = UUID.fromString("69135e98-da96-4fce-9219-5da65a4909ea");
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
    //todo Receive
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

    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice selectedDevice;
    private BluetoothSocket bluetoothSocket = null;
    private ArrayAdapter<String> deviceListAdapter;
    private final ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
    private ListView deviceListView;

    //private String uuidString = "INSERT_YOUR_UUID_HERE";
    private OutputStream outputStream;
    private InputStream inputStream;
    private volatile boolean stopWorker;

    private static int previousDeviceListSize;
    private Handler handler = new Handler();
    private Activity bluetoothActivity;

    @SuppressLint("MissingPermission")
    public BluetoothManager(Context context) {
        bluetoothActivity = (Activity) context;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Urządzenie nie ma modułu Bluetooth", Toast.LENGTH_LONG).show();
            bluetoothActivity.finish();      //TODO warto zareagować na to lepiej
            return;
        }
        permissionChecker = new PermissionChecker(context);
        permissionChecker.checkPermissions();       //TODO do poprawy - potrzebna obsługa przypadku z niepowodzeniem


        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            bluetoothActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);     //TODO do rozwiązania
        }

        //selectedDevice = bluetoothAdapter.getRemoteDevice("");//TODO do poprawy - szukaj i sparuj urządzenia


        deviceListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        deviceListView = bluetoothActivity.findViewById(R.id.deviceListView);
        deviceListView.setAdapter(deviceListAdapter);
        deviceListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedDevice = deviceList.get(position);

            colorAccent(parent, view, position, context);

            //wyzerowanie koloru tła dla innych elementów
            //connectToDevice();
            //mbtnConnect();        //todo pora na wciśnięcie połączenia
        });
        previousDeviceListSize = 0;

    }

    /**
     * Colorise the selected device element from ListView.
     * @param parent Every element of the device list.
     * @param view The view of the selected element.
     * @param position The index of the selected element.
     * @param context The activity context.
     */
    private void colorAccent(AdapterView parent, View view, int position, Context context) {
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
    }
    /**
     * Checks the localisation is turned on.
     * @return True if localisation is turned on, false if localisation is turned off, or if localisation isn't avaible.
     */
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) bluetoothActivity.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = false;
        try {
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isGpsEnabled;
    }

    /**
     * Checks if bluetooth is turned on.
     * @return True if bluetooth is turned on, false if bluetooth is turned off, or if there's no bluetooth avaible.
     */
    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    /**
     * Starts the discovery of the devices.
     * broadcastReceiver onReceive() updates the list of devices.
     * It's purging the deviceList.
     * Found devices will be accessable through deviceList.
     * @param availableModes The modes representing the set of devices you want to show.
     */
    @SuppressLint("MissingPermission")
    public void Discover(BluetoothSearchTypeFlag availableModes) {
        deviceList.clear();
        deviceListAdapter.clear();
        previousDeviceListSize = 0;

        if(availableModes.containFlag(BluetoothSearchTypeFlag.UNKNOWN_DEVICES)){
            discoverUnknownDevices();
        }
        if(availableModes.containFlag(BluetoothSearchTypeFlag.ALL_PAIRED_DEVICES)){
            discoverPairedDevices(false);
        }else if(availableModes.containFlag(BluetoothSearchTypeFlag.AVAILABLE_PAIRED_DEVICES)) {
            discoverPairedDevices(true);    //It doesn't work properly
        }
    }

    @SuppressLint("MissingPermission")
    private void UpdateDeviceListUI(){
        int currentSize = deviceList.size();
        // Sprawdź, czy rozmiar listy urządzeń wzrósł
        if (currentSize > previousDeviceListSize) {
            // Dodaj nowe urządzenia do adaptera i zaktualizuj widok listy
            for (int i = previousDeviceListSize; i < currentSize; i++) {
                BluetoothDevice device = deviceList.get(i);
                deviceListAdapter.add(device.getName() + " - " + device.getAddress());
            }
            previousDeviceListSize = currentSize;
        }
    }

    /**
     * It's listening for found devices.
     * Puts them into deviceList
     */
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceList.add(device);
                UpdateDeviceListUI();
            }
        }
    };

    public void CloseBluetooth(){
        CloseBluetoothSocket();
        CancelDiscovery();
    }
    public void CloseBluetoothSocket(){
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public void CancelDiscovery(){
        if (bluetoothAdapter.isDiscovering()) {
            // Przerwij wyszukiwanie urządzeń Bluetooth
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothActivity.unregisterReceiver(broadcastReceiver);
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
    public void onActivityResult(int requestCode, int resultCode){
        permissionChecker.onActivityResult(requestCode, resultCode);
    }
    @SuppressLint("MissingPermission")
    private void discoverUnknownDevices(){
        if (!bluetoothAdapter.isDiscovering()) {
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            bluetoothActivity.registerReceiver(broadcastReceiver, filter);
            bluetoothAdapter.startDiscovery();
        }
    }

    /**
     * Adds paired devices to deviceList.
     * @param isSelectOnlyAvaible choose, if you want to add only devices, you can access(True), or from every paired device(False).
     */
    @SuppressLint("MissingPermission")
    private void discoverPairedDevices(boolean isSelectOnlyAvaible){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (isSelectOnlyAvaible) {
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED) {   //TODO - getBondState doesn't fix it.
                        deviceList.add(device);
                    }
                } else {
                    deviceList.add(device);
                }
                UpdateDeviceListUI();
            }
        }
    }
}
