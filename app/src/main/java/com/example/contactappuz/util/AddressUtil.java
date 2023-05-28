package com.example.contactappuz.util;

import com.example.contactappuz.database.model.Address;
import com.google.android.libraries.places.api.model.Place;

/**
 * Utility class for handling Address related operations.
 * <p>
 * This class provides methods to perform conversions between Google Place objects
 * and the app's Address model.
 * <p>
 * Note: This is a utility class and not intended to be instantiated.
 */
public class AddressUtil {

    /**
     * Converts a Google Places API Place object to the application's Address model.
     * <p>
     * This method extracts the place ID, name (which is used as the address), and
     * LatLng object from the Place object and sets these values in a new Address
     * object which is then returned.
     *
     * @param place The Place object to be converted into an Address.
     * @return An Address object containing data extracted from the Place object.
     */
    public static Address placeToAddress(Place place) {
        Address result = new Address();

        result.setPlaceId(place.getId());
        result.setAddress(place.getName());
        result.setLat(place.getLatLng().latitude);
        result.setLng(place.getLatLng().longitude);

        return result;
    }
}

