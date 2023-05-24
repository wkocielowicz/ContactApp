package com.example.contactappuz.util;

import com.example.contactappuz.database.model.Contact;
import com.example.contactappuz.util.enums.SortFieldEnum;

import java.util.Comparator;

public class ContactFilter {

    private String firstNameFilter;
    private String lastNameFilter;
    private String addressFilter;
    private String birthDateFilter;

    private SortFieldEnum fieldSort;
    private boolean ascendingSortOrder;

    public ContactFilter() {
    }

    public String getFirstNameFilter() {
        return firstNameFilter;
    }

    public void setFirstNameFilter(String firstNameFilter) {
        this.firstNameFilter = firstNameFilter;
    }

    public String getLastNameFilter() {
        return lastNameFilter;
    }

    public void setLastNameFilter(String lastNameFilter) {
        this.lastNameFilter = lastNameFilter;
    }

    public String getAddressFilter() {
        return addressFilter;
    }

    public void setAddressFilter(String addressFilter) {
        this.addressFilter = addressFilter;
    }

    public String getBirthDateFilter() {
        return birthDateFilter;
    }

    public void setBirthDateFilter(String birthDateFilter) {
        this.birthDateFilter = birthDateFilter;
    }

    public SortFieldEnum getFieldSort() {
        return fieldSort;
    }

    public void setFieldSort(SortFieldEnum fieldSort) {
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
                case FIRST_NAME:
                    comparator = Comparator.comparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER);
                    break;
                case SECOND_NAME:
                    comparator = Comparator.comparing(Contact::getLastName, String.CASE_INSENSITIVE_ORDER);
                    break;
                case ADDRESS:
                    comparator = Comparator.comparing(Contact::getAddress, String.CASE_INSENSITIVE_ORDER);
                    break;
                case BIRTH_DATE:
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
