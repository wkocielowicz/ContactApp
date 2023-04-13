package com.example.contactappuz.activities.util;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ActivityUtil {

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
}
