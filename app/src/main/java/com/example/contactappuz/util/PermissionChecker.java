package com.example.contactappuz.util;

        import static android.app.Activity.RESULT_OK;

        import android.Manifest;
        import android.app.Activity;
        import android.content.Context;
        import android.content.IntentSender;
        import android.content.pm.PackageManager;

        import com.google.android.gms.common.api.ApiException;
        import com.google.android.gms.common.api.ResolvableApiException;
        import com.google.android.gms.location.*;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;

        import androidx.core.app.ActivityCompat;

/**
 * Jest odpowiedzialne za wygodniejszą obsługę pozwoleń. <br />
 * Aby użyć - należy postawić obiekt w wybranej klasie i wywołać metodę w funkcji inicjalizacyjnej
 * Wystarczy zignorować błędy, lub dodać atrybut {@code @SuppressLint("MissingPermission")}
 * nad metodami z błędami, jednak nieprzewidziane są przypadki w których nie otrzymano zgody.
 * */
public class PermissionChecker {
    private Context context;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    private static final int REQUEST_ENABLE_LOCATION = 123;

    public PermissionChecker(Context context) {
        this.context = context;
    }

    public void checkPermissions() {
        int writePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int bluetoothPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN);
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    PERMISSIONS_STORAGE,
                    1
            );
        }
        if (bluetoothPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    PERMISSIONS_LOCATION,
                    1
            );
            requestLocationEnable();
        }else{
            requestLocationEnable();
        }
    }
    private void requestLocationEnable() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // Lokalizacja jest już włączona
                } catch (ApiException exception) {
                    if (exception.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            // Poproś użytkownika o włączenie lokalizacji
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            resolvable.startResolutionForResult((Activity)context, REQUEST_ENABLE_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

// W przypadku włączenia lokalizacji przez użytkownika, obsłuż wynik w onActivityResult():

    public void onActivityResult(int requestCode, int resultCode){
        if (requestCode == REQUEST_ENABLE_LOCATION) {
            if (resultCode == RESULT_OK) {
                // Lokalizacja została włączona przez użytkownika
            } else {
                // Użytkownik odrzucił włączenie lokalizacji
            }
        }
    }

}