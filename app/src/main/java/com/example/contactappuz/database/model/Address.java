package com.example.contactappuz.database.model;

/**
 * The Address class represents an address for a contact in the ContactAppUZ application.
 * Each address has a unique place id (placeId) assigned by the Google Place API,
 * an address in text format, and a location in the form of latitude and longitude.
 */
public class Address {

    private String placeId;
    private String address;
    private double lat;
    private double lng;

    /**
     * Creates a new instance of the Address class.
     */
    public Address() {}

    /**
     * Creates a new instance of the Address class.
     *
     * @param placeId A unique identifier of the place assigned by the Google Place API.
     * @param address The address of the place in text format.
     * @param lat The latitude of the place.
     * @param lng The longitude of the place.
     */
    public Address(String placeId, String address, double lat, double lng) {
        this.placeId = placeId;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * Returns the placeId of this Address.
     *
     * @return A string representing the unique identifier of this place.
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * Returns the address of this Address in text format.
     *
     * @return A string representing the address of this place.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the latitude of this Address.
     *
     * @return A double representing the latitude of this place.
     */
    public double getLat() {
        return lat;
    }

    /**
     * Returns the longitude of this Address.
     *
     * @return A double representing the longitude of this place.
     */
    public double getLng() {
        return lng;
    }

    /**
     * Sets the placeId for this Address.
     *
     * @param placeId The unique identifier of the place.
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * Sets the address for this Address in text format.
     *
     * @param address The address of the place.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the latitude for this Address.
     *
     * @param lat The latitude of the place.
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Sets the longitude for this Address.
     *
     * @param lng The longitude of the place.
     */
    public void setLng(double lng) {
        this.lng = lng;
    }
}
