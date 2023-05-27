package com.example.contactappuz.activities.util;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * The ActivityUtil class provides utility methods for activities.
 */
public class ActivityUtil {

    /**
     * Initializes a spinner with category items from an enum class.
     *
     * @param spinner     The spinner to be initialized.
     * @param enumClass   The enum class containing the category items.
     * @param context     The context of the activity.
     * @param additionalItems Additional items to be added to the spinner (optional).
     *                        These items will be added before the enum items.
     * @param <T>         The type of the enum.
     */
    public static <T extends Enum<T>> void initializeCategorySpinner(Spinner spinner, Class<T> enumClass, Context context, String... additionalItems) {
        List<String> categoryItems = new ArrayList<>();

        // Add additional items, if any
        if (additionalItems != null) {
            for (String item : additionalItems) {
                categoryItems.add(item);
            }
        }

        // Add items from the enum
        for (T category : enumClass.getEnumConstants()) {
            categoryItems.add(category.toString());
        }

        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoryItems);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categorySpinnerAdapter);
    }

    /**
     * Returns the current user ID.
     *
     * @return The current user ID, or null if the user is not authenticated.
     */
    public static String getUserId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
    }
}
