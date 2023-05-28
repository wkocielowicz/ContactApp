package com.example.contactappuz.util;

import com.example.contactappuz.database.model.Contact;
import com.example.contactappuz.util.enums.SortFieldEnum;

import java.util.Comparator;

/**
 * Utility class for filtering and sorting contacts.
 */
public class ContactFilter {

    private String firstNameFilter;
    private String lastNameFilter;
    private String addressFilter;
    private String birthDateFilter;

    private SortFieldEnum fieldSort;
    private boolean ascendingSortOrder;

    /**
     * Constructs a ContactFilter object.
     */
    public ContactFilter() {
    }

    /**
     * Returns the filter for the first name.
     *
     * @return The first name filter.
     */
    public String getFirstNameFilter() {
        return firstNameFilter;
    }

    /**
     * Sets the filter for the first name.
     *
     * @param firstNameFilter The first name filter to set.
     */
    public void setFirstNameFilter(String firstNameFilter) {
        this.firstNameFilter = firstNameFilter;
    }

    /**
     * Returns the filter for the last name.
     *
     * @return The last name filter.
     */
    public String getLastNameFilter() {
        return lastNameFilter;
    }

    /**
     * Sets the filter for the last name.
     *
     * @param lastNameFilter The last name filter to set.
     */
    public void setLastNameFilter(String lastNameFilter) {
        this.lastNameFilter = lastNameFilter;
    }

    /**
     * Returns the filter for the address.
     *
     * @return The address filter.
     */
    public String getAddressFilter() {
        return addressFilter;
    }

    /**
     * Sets the filter for the address.
     *
     * @param addressFilter The address filter to set.
     */
    public void setAddressFilter(String addressFilter) {
        this.addressFilter = addressFilter;
    }

    /**
     * Returns the filter for the birth date.
     *
     * @return The birth date filter.
     */
    public String getBirthDateFilter() {
        return birthDateFilter;
    }

    /**
     * Sets the filter for the birth date.
     *
     * @param birthDateFilter The birth date filter to set.
     */
    public void setBirthDateFilter(String birthDateFilter) {
        this.birthDateFilter = birthDateFilter;
    }

    /**
     * Returns the sorting field.
     *
     * @return The sorting field.
     */
    public SortFieldEnum getFieldSort() {
        return fieldSort;
    }

    /**
     * Sets the sorting field.
     *
     * @param fieldSort The sorting field to set.
     */
    public void setFieldSort(SortFieldEnum fieldSort) {
        this.fieldSort = fieldSort;
    }

    /**
     * Returns the sort order (ascending or descending).
     *
     * @return The sort order.
     */
    public boolean isAscendingSortOrder() {
        return ascendingSortOrder;
    }

    /**
     * Sets the sort order (ascending or descending).
     *
     * @param ascendingSortOrder The sort order to set.
     */
    public void setAscendingSortOrder(boolean ascendingSortOrder) {
        this.ascendingSortOrder = ascendingSortOrder;
    }

    /**
     * Returns a comparator based on the sorting field and sort order.
     *
     * @return The comparator for sorting contacts.
     */
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
                    comparator = Comparator.comparing(contact -> contact.getAddress().getAddress(), String.CASE_INSENSITIVE_ORDER);
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
