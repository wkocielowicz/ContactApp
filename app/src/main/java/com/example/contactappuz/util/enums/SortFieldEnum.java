package com.example.contactappuz.util.enums;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Enum representing sorting fields for contacts.
 */
public enum SortFieldEnum {
    FIRST_NAME("First name"),
    SECOND_NAME("Second name"),
    ADDRESS("Address"),
    BIRTH_DATE("Birth date");

    private String name;

    /**
     * Constructs a SortFieldEnum with the specified name.
     *
     * @param name The name of the sorting field.
     */
    private SortFieldEnum(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the sorting field.
     *
     * @return The name of the sorting field.
     */
    public String getName() {
        return name;
    }

    /**
     * Custom ArrayAdapter for SortFieldEnum to be used with spinners.
     */
    public static class SortFieldEnumAdapter extends ArrayAdapter<SortFieldEnum> {

        /**
         * Constructs a SortFieldEnumAdapter with the specified context, resource ID, and SortFieldEnum array.
         *
         * @param context  The current context.
         * @param resource The resource ID for a layout file containing a TextView to use when instantiating views.
         * @param objects  The objects to represent in the Spinner.
         */
        public SortFieldEnumAdapter(@NonNull Context context, int resource, @NonNull SortFieldEnum[] objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position, convertView, parent);
        }

        private View initView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
            }

            TextView textView = convertView.findViewById(android.R.id.text1);
            SortFieldEnum currentItem = getItem(position);

            if (currentItem != null) {
                textView.setText(currentItem.getName());
            }

            return convertView;
        }
    }
}
