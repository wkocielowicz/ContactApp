package com.example.contactappuz.activities;

/**
 * The IActivity interface defines the common methods for initializing components and attaching listeners in activities.
 */
public interface IActivity {

    /**
     * Initializes the components in the activity.
     */
    void initializeComponents();

    /**
     * Attaches listeners to the components in the activity.
     */
    void attachListeners();
}
