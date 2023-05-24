package com.example.contactappuz.activities.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.contactappuz.R;
import com.example.contactappuz.util.ContactFilter;
import com.example.contactappuz.util.enums.SortFieldEnum;

public class FilterDialog {
    public interface OnFilterAppliedListener {
        void onFilterApplied(ContactFilter contactFilter);
    }

    public static void show(Context context, ContactFilter contactFilter, OnFilterAppliedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.filter_dialog, null);
        builder.setView(view);

        final EditText firstNameEditText = view.findViewById(R.id.firstNameEditText);
        final EditText lastNameEditText = view.findViewById(R.id.lastNameEditText);
        final EditText addressEditText = view.findViewById(R.id.addressEditText);
        final EditText birthDateEditText = view.findViewById(R.id.birthDateEditText);
        final CheckBox ascendingSortCheckBox = view.findViewById(R.id.ascendingSortCheckBox);
        Spinner sortFieldSpinner = view.findViewById(R.id.sortFieldSpinner);

        firstNameEditText.setText(contactFilter.getFirstNameFilter());
        lastNameEditText.setText(contactFilter.getLastNameFilter());
        addressEditText.setText(contactFilter.getAddressFilter());
        birthDateEditText.setText(contactFilter.getBirthDateFilter());
        ascendingSortCheckBox.setChecked(contactFilter.isAscendingSortOrder());
        setupSortFieldSpinner(context, sortFieldSpinner, contactFilter);

        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                contactFilter.setFirstNameFilter(firstNameEditText.getText().toString().trim());
                contactFilter.setLastNameFilter(lastNameEditText.getText().toString().trim());
                contactFilter.setAddressFilter(addressEditText.getText().toString().trim());
                contactFilter.setBirthDateFilter(birthDateEditText.getText().toString().trim());
                contactFilter.setFieldSort((SortFieldEnum) sortFieldSpinner.getSelectedItem());
                contactFilter.setAscendingSortOrder(ascendingSortCheckBox.isChecked());

                if (listener != null) {
                    listener.onFilterApplied(contactFilter);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private static void setupSortFieldSpinner(Context context, Spinner sortFieldSpinner, ContactFilter contactFilter) {
        SortFieldEnum.SortFieldEnumAdapter sortAdapter = new SortFieldEnum.SortFieldEnumAdapter(context, android.R.layout.simple_spinner_item, SortFieldEnum.values());
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortFieldSpinner.setAdapter(sortAdapter);
        sortFieldSpinner.setSelection(sortAdapter.getPosition(contactFilter.getFieldSort()));

        int selectedIndex = contactFilter.getFieldSort() != null ? sortAdapter.getPosition(contactFilter.getFieldSort()) : 0;
        sortFieldSpinner.setSelection(selectedIndex);

        sortFieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SortFieldEnum selectedSortField = (SortFieldEnum) parent.getItemAtPosition(position);
                contactFilter.setFieldSort(selectedSortField);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}