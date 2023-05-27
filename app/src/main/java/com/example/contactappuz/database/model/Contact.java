package com.example.contactappuz.database.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a Contact in the application.
 */
public class Contact implements Serializable {
    private String contactId;
    private String category;
    private String firstName;
    private String lastName;
    private String address;
    private String birthDate;
    private String photoUrl;
    private String photoPath;

    /**
     * Constructs a new Contact object with a randomly generated contactId.
     */
    public Contact() {
        this.contactId = UUID.randomUUID().toString();
    }

    /**
     * Constructs a new Contact object with the provided details.
     *
     * @param category   The category of the contact.
     * @param firstName  The first name of the contact.
     * @param lastName   The last name of the contact.
     * @param address    The address of the contact.
     * @param birthDate  The birth date of the contact.
     */
    public Contact(String category, String firstName, String lastName, String address, String birthDate) {
        this.contactId = UUID.randomUUID().toString();
        this.category = category;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthDate = birthDate;
    }

    /**
     * Returns the unique ID of the contact.
     *
     * @return The contact ID.
     */
    public String getContactId() {
        return contactId;
    }

    /**
     * Sets the contact ID.
     *
     * @param contactId The contact ID to set.
     */
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    /**
     * Returns the category of the contact.
     *
     * @return The contact's category.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the contact.
     *
     * @param category The category to set.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the first name of the contact.
     *
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the contact.
     *
     * @param firstName The first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the contact.
     *
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the contact.
     *
     * @param lastName The last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the address of the contact.
     *
     * @return The contact's address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the contact.
     *
     * @param address The address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the birth date of the contact.
     *
     * @return The contact's birth date.
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date of the contact.
     *
     * @param birthDate The birth date to set.
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Returns the photo URL of the contact.
     *
     * @return The contact's photo URL.
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * Sets the photo URL of the contact.
     *
     * @param photoUrl The photo URL to set.
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * Returns the photo path of the contact.
     *
     * @return The contact's photo path.
     */
    public String getPhotoPath() {
        return photoPath;
    }

    /**
     * Sets the photo path of the contact.
     *
     * @param photoPath The photo path to set.
     */
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
