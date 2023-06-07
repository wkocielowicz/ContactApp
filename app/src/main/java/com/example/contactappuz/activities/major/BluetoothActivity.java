package com.example.contactappuz.activities.major;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.contactappuz.R;
import com.example.contactappuz.logic.BluetoothManager;
import com.example.contactappuz.util.enums.BluetoothSearchTypeFlag;

/**
 * The activity that shows the user bluetooth devices, and allows the user to send data to them through
 */
public class BluetoothActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothActivity";
    //static final UUID mUUID = UUID.fromString("");

    Button  /*mbtnForward, mbtnBack, mbtnRight, mbtnLeft, mbtnStop, mbtnCollect, mbttSendDB,*/ mbtnBroadcast, mbtnDiscover, mbtnConnect;

    BluetoothManager bluetoothManager;
    byte[] buffer = new byte[1024];
    int bytes;
    String Data="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bluetoothManager.onActivityResult(requestCode, resultCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(!bluetoothManager.onRequestPermissionsResult(requestCode, grantResults)){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        bluetoothManager = new BluetoothManager(this);

        /*ApiClient klient = new ApiClient();
        mbtnCollect = findViewById(R.id.btnCollect);
        mbttSendDB = findViewById(R.id.btnSendDB);*/
        mbtnBroadcast = findViewById(R.id.btnBroadcastBt);
        mbtnDiscover   = findViewById(R.id.btnDiscover);
        mbtnConnect = findViewById(R.id.btnConnect);

        //mbttSendDB.setOnClickListener(view -> startThread(Data));
        //mbtnCollect.setOnClickListener(view -> bluetoothManager.mbtnCollect());
//        mbtnConnect.setOnClickListener(view -> AsyncTask.execute());
        mbtnBroadcast.setOnClickListener(view -> bluetoothManager.startBluetoothServer());
        mbtnConnect.setOnClickListener(view -> bluetoothManager.mbtnConnect());
//        touchEventHelper.setButtonOnTouchListener(mbtnForward, 70, 83);
        mbtnDiscover.setOnClickListener(view -> bluetoothManager.Discover(new BluetoothSearchTypeFlag(BluetoothSearchTypeFlag.UNKNOWN_DEVICES | BluetoothSearchTypeFlag.ALL_PAIRED_DEVICES)));

        bluetoothManager.Discover(new BluetoothSearchTypeFlag(BluetoothSearchTypeFlag.AVAILABLE_PAIRED_DEVICES));
    }

    @Override
    protected void onDestroy() {
        bluetoothManager.CloseBluetooth();
        super.onDestroy();
    }

    public void startThread(String dane){
        Log.d(TAG, "przesłana wiadomość: " + dane);
    //    AsyncTask.execute(() -> {
    //        try {
    //            ApiClient klient = new ApiClient();
    //            klient.post(dane);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    });
    }
}
