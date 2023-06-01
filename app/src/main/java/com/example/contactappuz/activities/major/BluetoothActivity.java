package com.example.contactappuz.activities.major;

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

    Button  /*mbtnForward, mbtnBack, mbtnRight, mbtnLeft, mbtnStop, mbtnCollect, mbttSendDB,*/ mbtnDiscover, mbtnConnect;

    BluetoothManager bluetoothManager;
    byte[] buffer = new byte[1024];
    int bytes;
    String Data="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bluetoothManager.onActivityResult(requestCode, resultCode);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        bluetoothManager = new BluetoothManager(this);

        /*ApiClient klient = new ApiClient();
        mbtnForward = findViewById(R.id.btnforward);
        mbtnBack    = findViewById(R.id.btnBack);
        mbtnLeft    = findViewById(R.id.btnLeft);
        mbtnRight   = findViewById(R.id.btnRight);
        mbtnStop   = findViewById(R.id.btnStop);
        mbtnCollect = findViewById(R.id.btnCollect);
        mbttSendDB = findViewById(R.id.btnSendDB);*/
        mbtnDiscover   = findViewById(R.id.btnDiscover);
        mbtnConnect = findViewById(R.id.btnConnect);


/*        mbtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                klient.post("mleko");
                                klient.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });
            }
        });*/
        /*AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    klient.post("Inicjalizacja");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });*/

        //mbttSendDB.setOnClickListener(view -> startThread(Data));
        //mbtnCollect.setOnClickListener(view -> bluetoothManager.mbtnCollect());
//        mbtnConnect.setOnClickListener(view -> AsyncTask.execute());
        mbtnConnect.setOnClickListener(view -> bluetoothManager.mbtnConnect());
//        touchEventHelper.setButtonOnTouchListener(mbtnForward, 70, 83);
        mbtnDiscover.setOnClickListener(view -> bluetoothManager.Discover(new BluetoothSearchTypeFlag(BluetoothSearchTypeFlag.UNKNOWN_DEVICES | BluetoothSearchTypeFlag.ALL_PAIRED_DEVICES)));
        bluetoothManager.Discover(new BluetoothSearchTypeFlag(BluetoothSearchTypeFlag.AVAILABLE_PAIRED_DEVICES));

    }

    @Override
    protected void onDestroy() {
        bluetoothManager.CloseBluetoothSocket();
        bluetoothManager.CancelDiscovery();
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
