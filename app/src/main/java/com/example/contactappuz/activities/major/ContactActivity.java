package com.example.contactappuz.activities.major;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.IActivity;
import com.example.contactappuz.activities.LanguageActivity;
import com.example.contactappuz.activities.minor.AddEditContactActivity;
import com.example.contactappuz.activities.util.ActivityUtil;
import com.example.contactappuz.activities.util.FilterDialog;
import com.example.contactappuz.database.model.Contact;
import com.example.contactappuz.logic.FireBaseManager;
import com.example.contactappuz.logic.NavigateManager;
import com.example.contactappuz.util.ContactFilter;
import com.example.contactappuz.util.ContactRowAdapter;
import com.example.contactappuz.util.enums.ContactCategoryEnum;
import com.example.contactappuz.util.enums.mode.ActivityModeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * The activity that displays the list of contacts and provides options for filtering and adding/editing contacts.
 */
public class ContactActivity extends LanguageActivity implements IActivity {

    private static final String CATEGORY_ALL = "All";

    private Button filterButton;
    private Button addContactButton;
    private ContactRowAdapter adapter;
    private RecyclerView recyclerView;
    private Spinner categorySpinner;

    private ActivityModeEnum mode;
    private List<Contact> contactList = new ArrayList<>();;
    private ContactFilter contactFilter = new ContactFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mode = getIntentMode();
        initializeComponents();

        attachListeners();
    }

    /**
     * Retrieves the activity mode from the received Intent data.
     *
     * @return The activity mode.
     */
    public ActivityModeEnum getIntentMode() {
        return (ActivityModeEnum) getIntent().getSerializableExtra("mode");
    }

    @Override
    public void initializeComponents() {
        setContentView(R.layout.activity_contact);

        filterButton = findViewById(R.id.filterButton);
        addContactButton = findViewById(R.id.buttonAddContact);
        recyclerView = findViewById(R.id.recyclerViewContact);
        categorySpinner = findViewById(R.id.contactCategorySpinner);

        ActivityUtil.initializeCategorySpinner(categorySpinner, ContactCategoryEnum.class, this, "All");

        adapter = new ContactRowAdapter(contactList, new ContactRowAdapter.OnItemClickListener() {
            @Override
            public void onUpdateButtonClick(Contact contact) {
                Intent intent = new Intent(ContactActivity.this, AddEditContactActivity.class);
                intent.putExtra("mode", ActivityModeEnum.EDIT);
                intent.putExtra("contact", contact);
                intent.putExtra("contactId", contact.getContactId());
                startActivity(intent);
            }

            @Override
            public void onDeleteButtonClick(Contact contact) {
                FireBaseManager.deleteContact(ActivityUtil.getUserId(), contact.getContactId(), task -> {
                    if (task.isSuccessful()) {
                        getContactsFromFirebase(categorySpinner.getSelectedItem().toString());
                    }
                    else {
                        Toast.makeText(ContactActivity.this, "Failed to delete contact", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNavigateButtonClick(Contact contact) {
                NavigateManager.navigateTo(ContactActivity.this, contact.getAddress().getLat(), contact.getAddress().getLng());
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void attachListeners() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (selectedItem.equals(CATEGORY_ALL)) {
                    getContactsFromFirebase(CATEGORY_ALL);
                } else {
                    getContactsFromFirebase(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        filterButton.setOnClickListener(v -> {
            FilterDialog.OnFilterAppliedListener onFilterAppliedListener = new FilterDialog.OnFilterAppliedListener() {
                @Override
                public void onFilterApplied(ContactFilter contactFilter) {
                    String selectedCategory = categorySpinner.getSelectedItem().toString();
                    getContactsFromFirebase(selectedCategory);
                }
            };

            FilterDialog.show(ContactActivity.this, contactFilter, onFilterAppliedListener);
        });

        addContactButton.setOnClickListener(view -> {
            Intent intent = new Intent(ContactActivity.this, AddEditContactActivity.class);
            intent.putExtra("mode", ActivityModeEnum.ADD);
            startActivity(intent);
        });
    }

    /**
     * Retrieves contacts from Firebase based on the selected category.
     * Updates the adapter with the retrieved contacts.
     *
     * @param selectedCategory The selected category to filter the contacts.
     */
    private void getContactsFromFirebase(String selectedCategory) {
        FireBaseManager.getContactsFromFirebase(this, ActivityUtil.getUserId(), selectedCategory, contactFilter, newContacts -> {
            adapter.updateContacts(newContacts);
        });
    }
}
