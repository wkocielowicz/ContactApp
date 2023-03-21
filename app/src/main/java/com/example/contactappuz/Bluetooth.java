package com.example.contactappuz;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Bluetooth extends AppCompatActivity {
    private static final String TAG = "Bluetooth";
    static final UUID mUUID = UUID.fromString("");

    Button  mbtnForward, mbtnBack, mbtnRight, mbtnLeft, mbtnStop, mbtnConnect, mbtnCollect, mbttSendDB;

    PermissionChecker permissionChecker = new PermissionChecker(this);

    BluetoothAdapter btAdapter;
    BluetoothDevice hc05;
    BluetoothSocket btSocket = null;

    byte[] buffer = new byte[1024];
    int bytes;
    String Data="";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        //ApiClient klient = new ApiClient();
        //mbtnForward = findViewById(R.id.btnforward);
        //mbtnBack    = findViewById(R.id.btnBack);
        //mbtnLeft    = findViewById(R.id.btnLeft);
        //mbtnRight   = findViewById(R.id.btnRight);
        mbtnStop   = findViewById(R.id.btnStop);
        mbtnConnect = findViewById(R.id.btnConnect);
        mbtnCollect = findViewById(R.id.btnCollect);
        mbttSendDB = findViewById(R.id.bttSendDB);


//        mbtnConnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    AsyncTask.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                klient.post("mleko");
//                                klient.close();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    });
//            }
//        });
        //AsyncTask.execute(new Runnable() {
        //    @Override
        //    public void run() {
        //        try {
        //            klient.post("Inicjalizacja");
        //
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //    }
        //
        //});




        mbttSendDB.setOnClickListener(view -> {
            startThread(Data);
        });


//        mbtnConnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AsyncTask.execute();
//            }
//        });
//







        btAdapter = BluetoothAdapter.getDefaultAdapter();
        permissionChecker.checkPermissions();
        hc05 = btAdapter.getRemoteDevice("");


        mbtnConnect.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                try {
                    permissionChecker.checkPermissions();
                    btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                    btSocket.connect();
//                    touchEventHelper = new TouchEventHelper(btSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

//        touchEventHelper.setButtonOnTouchListener(mbtnForward, 70, 83);
        mbtnForward.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    OutputStream outputStream;
                    try {
                        outputStream = btSocket.getOutputStream();
                        outputStream.write(70);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    OutputStream outputStream;
                    try {
                        outputStream = btSocket.getOutputStream();
                        outputStream.write(83);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        mbtnBack.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    OutputStream outputStream;
                    try {
                        outputStream = btSocket.getOutputStream();
                        outputStream.write(66);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    OutputStream outputStream;
                    try {
                        outputStream = btSocket.getOutputStream();
                        outputStream.write(83);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        mbtnLeft.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    OutputStream outputStream;
                    try {
                        outputStream = btSocket.getOutputStream();
                        outputStream.write(76);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    OutputStream outputStream;
                    try {
                        outputStream = btSocket.getOutputStream();
                        outputStream.write(83);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        mbtnRight.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    OutputStream outputStream;
                    try {
                        outputStream = btSocket.getOutputStream();
                        outputStream.write(82);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    OutputStream outputStream;
                    try {
                        outputStream = btSocket.getOutputStream();
                        outputStream.write(83);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        mbtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    OutputStream outputStream = btSocket.getOutputStream();
                    outputStream.write(83);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mbtnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    OutputStream outputStream = btSocket.getOutputStream();
                    outputStream.write(67);   //C

                    AsyncTask.execute(new Runnable() {

                        @Override
                        public void run() {
                            InputStream inputStream = null;
                            try {
                                inputStream = btSocket.getInputStream();
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
        });



    }


    public void startThread(String dane){
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
