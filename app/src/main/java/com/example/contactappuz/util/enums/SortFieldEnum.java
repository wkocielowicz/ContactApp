package com.example.contactappuz.util.enums;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public enum SortFieldEnum {
    FIRST_NAME("First name"),

    SECOND_NAME("Second name"),

    ADDRESS("Address"),

    BIRTH_DATE("Birth date");

    private String name;

    private SortFieldEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }



    public static class SortFieldEnumAdapter extends ArrayAdapter<SortFieldEnum> {

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