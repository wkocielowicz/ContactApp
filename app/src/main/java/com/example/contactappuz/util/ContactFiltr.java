package com.example.contactappuz.util;

import com.example.contactappuz.model.Contact;

import java.util.Comparator;

public class ContactFiltr {

    private String firstNameFiltr;
    private String lastNameFiltr;
    private String addressFiltr;
    private String birthDateFiltr;

    private String fieldSort;
    private boolean ascendingSortOrder;

    public ContactFiltr() {
    }

    public String getFirstNameFiltr() {
        return firstNameFiltr;
    }

    public void setFirstNameFiltr(String firstNameFiltr) {
        this.firstNameFiltr = firstNameFiltr;
    }

    public String getLastNameFiltr() {
        return lastNameFiltr;
    }

    public void setLastNameFiltr(String lastNameFiltr) {
        this.lastNameFiltr = lastNameFiltr;
    }

    public String getAddressFiltr() {
        return addressFiltr;
    }

    public void setAddressFiltr(String addressFiltr) {
        this.addressFiltr = addressFiltr;
    }

    public String getBirthDateFiltr() {
        return birthDateFiltr;
    }

    public void setBirthDateFiltr(String birthDateFiltr) {
        this.birthDateFiltr = birthDateFiltr;
    }

    public String getFieldSort() {
        return fieldSort;
    }

    public void setFieldSort(String fieldSort) {
        this.fieldSort = fieldSort;
    }

    public boolean isAscendingSortOrder() {
        return ascendingSortOrder;
    }

    public void setAscendingSortOrder(boolean ascendingSortOrder) {
        this.ascendingSortOrder = ascendingSortOrder;
    }

    public Comparator<Contact> getComparator() {
        Comparator<Contact> comparator;

        if (fieldSort == null) {
            comparator = Comparator.comparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER);
        } else {
            switch (fieldSort) {
                case "FirstName":
                    comparator = Comparator.comparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER);
                    break;
                case "LastName":
                    comparator = Comparator.comparing(Contact::getLastName, String.CASE_INSENSITIVE_ORDER);
                    break;
                case "Address":
                    comparator = Comparator.comparing(Contact::getAddress, String.CASE_INSENSITIVE_ORDER);
                    break;
                case "BirthDate":
                    comparator = Comparator.comparing(Contact::getBirthDate);
                    break;
                default:
                    comparator = Comparator.comparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER);
                    break;
            }
        }

        if (!isAscendingSortOrder()) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
