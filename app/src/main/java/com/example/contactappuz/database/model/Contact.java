package com.example.contactappuz.database.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.UUID;

public class Contact implements Serializable {
    private String contactId;
    private String category;
    private String firstName;
    private String lastName;
    private String address;
    private String birthDate;

    public Contact() {
        this.contactId = UUID.randomUUID().toString();
    }

    public Contact(String category, String firstName, String lastName, String address, String birthDate) {
        this.contactId = UUID.randomUUID().toString();
        this.category = category;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthDate = birthDate;
    }

    public String getContactId() { return contactId; }

    public void setContactId(String contactId) { this.contactId = contactId; }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @NonNull
    @Override
    public String toString() {
        return firstName + " " + lastName + " " + address;
    }
}