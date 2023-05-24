package com.example.contactappuz.activities.minor;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.IActivity;
import com.example.contactappuz.activities.LanguageActivity;
import com.example.contactappuz.activities.util.ActivityUtil;
import com.example.contactappuz.database.model.Contact;
import com.example.contactappuz.logic.FireBaseService;
import com.example.contactappuz.util.enums.ContactCategoryEnum;
import com.example.contactappuz.util.enums.mode.ActivityModeEnum;

public class AddEditContactActivity extends LanguageActivity implements IActivity {

    private Button acceptButton;
    private Button backButton;
    private Button clearButton;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText addressEditText;
    private EditText birthDateEditText;
    private Spinner categorySpinner;

    private ActivityModeEnum mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mode = getIntentMode();
        initializeComponents();

        attachListeners();
    }

    public ActivityModeEnum getIntentMode() {
        return (ActivityModeEnum) getIntent().getSerializableExtra("mode");
    }

    @Override
    public void initializeComponents() {
        setContentView(R.layout.activity_add_edit_contact);

        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);
        clearButton = findViewById(R.id.clearButton);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        categorySpinner = findViewById(R.id.categorySpinner);

        ActivityUtil.initializeCategorySpinner(categorySpinner, ContactCategoryEnum.class, this);

        if(mode == ActivityModeEnum.EDIT) {
            setFields();
        }
    }

    @Override
    public void attachListeners() {
        acceptButton.setOnClickListener(view -> {
            Contact contact = loadFields();

            if (mode == ActivityModeEnum.ADD) {
                FireBaseService.addContact(AddEditContactActivity.this, ActivityUtil.getUserId(), contact, task -> {
                    if (task.isSuccessful()) {
                        finish();
                    }
                });
            } else if (mode == ActivityModeEnum.EDIT) {
                String contactId = getIntent().getStringExtra("contactId");
                if (contactId != null) {
                    FireBaseService.updateContact(AddEditContactActivity.this, ActivityUtil.getUserId(), contactId, contact, task -> {
                        if (task.isSuccessful()) {
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(AddEditContactActivity.this, "Failed to update contact", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(view -> {
            // Close the activity when the back button is pressed
            finish();
        });

        clearButton.setOnClickListener(view -> {
            // Clear all the fields when the clear button is pressed
            firstNameEditText.setText("");
            lastNameEditText.setText("");
            addressEditText.setText("");
            birthDateEditText.setText("");
            categorySpinner.setSelection(0); // Set the spinner back to the default selection
        });
    }

    private void setFields() {
        Contact contact = (Contact) getIntent().getSerializableExtra("contact");
        if (contact != null) {
            firstNameEditText.setText(contact.getFirstName());
            lastNameEditText.setText(contact.getLastName());
            addressEditText.setText(contact.getAddress());
            birthDateEditText.setText(contact.getBirthDate());
            int spinnerPosition = ((ArrayAdapter<String>) categorySpinner.getAdapter()).getPosition(contact.getCategory());
            categorySpinner.setSelection(spinnerPosition);
        }
    }

    private Contact loadFields() {
        Contact contact = new Contact();

        contact.setFirstName(firstNameEditText.getText().toString());
        contact.setLastName(lastNameEditText.getText().toString());
        contact.setAddress(addressEditText.getText().toString());
        contact.setBirthDate(birthDateEditText.getText().toString());
        contact.setCategory(categorySpinner.getSelectedItem().toString());

        return contact;
    }
}