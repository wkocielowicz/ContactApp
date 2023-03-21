package com.example.contactappuz.model;

import androidx.annotation.NonNull;

import com.example.contactappuz.util.ContactCategory;

public class Contact {
    private ContactCategory category;
    private String firstName;
    private String lastName;
    private String address;
    private String birthDate;

    public Contact() {
    }

    public Contact(ContactCategory category, String firstName, String lastName, String address, String birthDate) {
        this.category = category;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthDate = birthDate;
    }

    public ContactCategory getCategory() {
        return category;
    }

    public void setCategory(ContactCategory category) {
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