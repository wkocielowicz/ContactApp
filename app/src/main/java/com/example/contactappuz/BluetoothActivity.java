package com.example.contactappuz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.contactappuz.util.Bluetooth;

public class BluetoothActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothActivity";
    //static final UUID mUUID = UUID.fromString("");

    Button  /*mbtnForward, mbtnBack, mbtnRight, mbtnLeft, mbtnStop,*/ mbtnDiscover, mbtnConnect, mbtnCollect, mbttSendDB;

    Bluetooth bluetoothService;
    byte[] buffer = new byte[1024];
    int bytes;
    String Data="";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        bluetoothService = new Bluetooth(this);

        /*ApiClient klient = new ApiClient();
        mbtnForward = findViewById(R.id.btnforward);
        mbtnBack    = findViewById(R.id.btnBack);
        mbtnLeft    = findViewById(R.id.btnLeft);
        mbtnRight   = findViewById(R.id.btnRight);
        mbtnStop   = findViewById(R.id.btnStop);*/
        mbtnDiscover   = findViewById(R.id.btnDiscover);
        mbtnConnect = findViewById(R.id.btnConnect);
        mbtnCollect = findViewById(R.id.btnCollect);
        mbttSendDB = findViewById(R.id.btnSendDB);


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

        mbttSendDB.setOnClickListener(view -> startThread(Data));
//        mbtnConnect.setOnClickListener(view -> AsyncTask.execute());
        mbtnConnect.setOnClickListener(view -> bluetoothService.mbtnConnect());
//        touchEventHelper.setButtonOnTouchListener(mbtnForward, 70, 83);
        mbtnDiscover.setOnClickListener(view -> bluetoothService.mbtnDiscover(this));
        mbtnCollect.setOnClickListener(view -> bluetoothService.mbtnCollect());

    }

    @Override
    protected void onDestroy() {
        bluetoothService.closeBluetoothSocket();
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
