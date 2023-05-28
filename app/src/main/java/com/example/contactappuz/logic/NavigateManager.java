package com.example.contactappuz.logic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * The NavigateManager class provides a method for launching Google Maps to
 * navigate to a specific location. The location is determined by latitude
 * and longitude coordinates.
 *
 * Usage:
 * NavigateManager.navigateTo(context, lat, lng);
 *
 * The navigateTo() method uses an Intent to start Google Maps, and sets the
 * destination to the location specified by the provided latitude and longitude.
 */
public class NavigateManager {

    /**
     * Launches Google Maps for navigation to a specific location.
     *
     * @param context The context from which this method is called.
     * @param lat The latitude of the destination location.
     * @param lng The longitude of the destination location.
     */
    public static void navigateTo(Context context, double lat, double lng) {
        // Check if coordinates are zero
        if (lat == 0.0 && lng == 0.0) {
            Toast.makeText(context, "Brak pełnego adresu", Toast.LENGTH_SHORT).show();
        } else {
            String uri = String.format("google.navigation:q=%f,%f", lat, lng);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }
    }
}
