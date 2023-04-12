package com.example.contactappuz.util;

        import android.Manifest;
        import android.app.Activity;
        import android.content.Context;
        import android.content.pm.PackageManager;

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

    public PermissionChecker(Context context) {
        this.context = context;
    }

    public void checkPermissions() {
        int permission1 = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    PERMISSIONS_STORAGE,
                    1
            );
        }
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    PERMISSIONS_LOCATION,
                    1
            );
        }
    }
}