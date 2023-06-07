package com.example.contactappuz.util;

        import static android.app.Activity.RESULT_OK;

        import android.Manifest;
        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.bluetooth.BluetoothAdapter;
        import android.content.Intent;
        import android.content.IntentSender;
        import android.content.pm.PackageManager;
        import android.util.Log;

        import com.google.android.gms.common.api.ApiException;
        import com.google.android.gms.common.api.ResolvableApiException;
        import com.google.android.gms.location.*;
        import com.google.android.gms.tasks.Task;

        import androidx.annotation.NonNull;
        import androidx.core.app.ActivityCompat;

        import java.util.ArrayList;

/**
 * Jest odpowiedzialne za wygodniejszą obsługę pozwoleń. <br />
 * Aby użyć - należy postawić obiekt w wybranej klasie i wywołać metodę w funkcji inicjalizacyjnej
 * Wystarczy zignorować błędy, lub dodać atrybut {@code @SuppressLint("MissingPermission")}
 * nad metodami z błędami, jednak nieprzewidziane są przypadki w których nie otrzymano zgody.
 */
public class PermissionChecker {
    private static final String TAG = "PermissionChecker";

    private Activity activity;
    /*private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };*/
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    private ArrayList<Runnable> bluetoothEnabledRunnable = new ArrayList<>();
    private ArrayList<Runnable> localisationEnabledRunnable = new ArrayList<>();

    private void runRunnableArrayList(ArrayList<Runnable> runnables){
        for (Runnable runnable : runnables) {
            runnable.run();
        }
        runnables.clear();
    }

    private static final int REQUEST_ENABLE_LOCATION = 103;
    private static final int REQUEST_ENABLE_BLUETOOTH = 102;
    private static final int REQUEST_LOCATION_PERMISSION = 101;

    public PermissionChecker(Activity activity) {
        this.activity = activity;
    }

    /**
     * Check Localtion permission and start the whole process of enabling bluetooth.
     *
     * @param runnables handles at most 2 Runnables that will be executed:
     *                  1. after permission is granted,
     *                  2. after permission is revoked.
     */
    public void checkBluetoothLocationPermission(Runnable... runnables) {
        String requestedPermissionName = Manifest.permission.BLUETOOTH_SCAN;
        int bluetoothPermission = ActivityCompat.checkSelfPermission(activity, requestedPermissionName);
        if (bluetoothPermission != PackageManager.PERMISSION_GRANTED) {
            if (runnables.length >= 1) {
                bluetoothEnabledRunnable.add(runnables[0]);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestedPermissionName)) {
                ShowBluetoothRequestDialog(
                        this::requestBluetoothPermission,
                        ()->{}//activity::finish
                );
            }else{
                requestBluetoothPermission();
            }
        } else {
            if (runnables.length >= 1) {
                bluetoothEnabledRunnable.add(runnables[0]);
            }
            requestBluetoothEnable(BluetoothAdapter.getDefaultAdapter());
        }
    }

    /**
     * Try to grant bluetooth localisation permission.
     * The request is handled by onRequestPermissionsResult
     */
    private void requestBluetoothPermission() {
        ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_LOCATION,
                REQUEST_LOCATION_PERMISSION
        );
    }

    private void ShowBluetoothRequestDialog(Runnable onYes, Runnable onNo) {
        new AlertDialog.Builder(activity)
                .setTitle("Request Bluetooth Permission")
                .setMessage("With Bluetooth turned on - you'll be able to share your contacts to another app user of your choice. Do you want to grant bluetooth permissions to the app?")
                //"Z włączonym Bluetooth - będziesz w stanie udostępnić swoje kontakty wybranemu innemu użytkownikowi aplikacji. Czy chcesz przyznać uprawnienia bluetooth aplikacji?"
                .setPositiveButton(android.R.string.yes, (dialog, which) -> onYes.run())
                .setNegativeButton(android.R.string.no, (dialog, which) -> onNo.run())
                .setIcon(android.R.drawable.stat_sys_data_bluetooth)
                .show();
    }

    /**
     * Try to enable localisation.
     * The request is handled by onActivityResult
     *
     * @param runnables if request is successfull the 1-st runnable will executed
     */
    public void requestLocationEnable(Runnable... runnables) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnCompleteListener(task1 -> {
            try {
                if (runnables.length >= 1) {
                    localisationEnabledRunnable.add(runnables[0]);
                }
                LocationSettingsResponse response = task1.getResult(ApiException.class);
                // Lokalizacja jest już włączona
                runRunnableArrayList(localisationEnabledRunnable);
            } catch (ApiException exception) {
                if (exception.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        // Poproś użytkownika o włączenie lokalizacji
                        ResolvableApiException resolvable = (ResolvableApiException) exception;
                        resolvable.startResolutionForResult(activity, REQUEST_ENABLE_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Requests for enabling bluetooth, if it have to. If it won't need to enable bluetooth, then it runs the action, that have to be executed, if bluetooth is enabled.
     * @param bluetoothAdapter the bluetoothAdapter, that will be enabled
     */
    @SuppressLint("MissingPermission")
    public void requestBluetoothEnable(BluetoothAdapter bluetoothAdapter, Runnable... alternativeRunnable){
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        }else{
            if(alternativeRunnable.length > 0){
                alternativeRunnable[0].run();
            }else {
                runRunnableArrayList(bluetoothEnabledRunnable);
            }
        }
    }

    /**
     * Handles, localisation permission, and executes additional tasks (tasks originating from checkBluetoothLocationPermission)
     *
     * @param requestCode the code of requested permission
     * @param grantResults the results (what is granted, from the pull of requested permissions)
     * @param bluetoothAdapter bluetooth Adapter used, to check if bluetooth is on
     * @return true, if requestCode is requested permission, or false otherwise.
     */
    public boolean onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults, BluetoothAdapter bluetoothAdapter) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // uprawnienia Lokalizacji zostały przyznane przez użytkownika
                requestBluetoothEnable(bluetoothAdapter);
                Log.e(TAG, "GPS granted");
            } else {
                // uprawnienia Lokalizacji zostały odrzucone przez użytkownika
                // activity.finish();
                requestBluetoothEnable(bluetoothAdapter);
                Log.e(TAG, "GPS-not granted");
            }
            return true;
        }
        return false;
    }

    // W przypadku włączenia funkcjonalności przez użytkownika, obsłuż wynik w onActivityResult():
    public void onActivityResult(int requestCode, int resultCode) {
        if (requestCode == REQUEST_ENABLE_LOCATION) {
            if (resultCode == RESULT_OK) {
                // Lokalizacja została włączona przez użytkownika
                runRunnableArrayList(localisationEnabledRunnable);

                Log.e(TAG, "GPS-enabled");
            } else {
                // Użytkownik odrzucił włączenie lokalizacji
                Log.e(TAG, "GPS-disabled");
            }
        } else if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                // Bluetooth został włączony przez użytkownika
                runRunnableArrayList(bluetoothEnabledRunnable);
                Log.e(TAG, "BT-enabled");
            } else {
                // Użytkownik odrzucił włączenie bluetooth
                Log.e(TAG, "BT-disabled");
            }
        }
    }
}