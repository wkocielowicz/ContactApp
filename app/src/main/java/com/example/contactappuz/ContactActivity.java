package com.example.contactappuz;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactappuz.model.Contact;
import com.example.contactappuz.util.ContactCategory;
import com.example.contactappuz.util.ContactFiltr;
import com.example.contactappuz.util.MyAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContactActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Contact> contactList;
    MyAdapter adapter;
    ContactFiltr filtr = new ContactFiltr();
    private Button filtrButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        recyclerView = findViewById(R.id.recyclerViewContact);
        contactList = new ArrayList<>();
        adapter = new MyAdapter(contactList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Spinner contactCategorySpinner = findViewById(R.id.contactCategorySpinner);
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("All");
        for (ContactCategory category : ContactCategory.values()) {
            spinnerItems.add(category.toString());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactCategorySpinner.setAdapter(spinnerAdapter);

        contactCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (selectedItem.equals("All")) {
                    getContactsFromFirebase(null); // Pass null for "All" option
                } else {
                    ContactCategory selectedCategory = ContactCategory.valueOf(selectedItem);
                    getContactsFromFirebase(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void getContactsFromFirebase(ContactCategory selectedCategory) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference contactRef = database.getReference("contacts");

        contactRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactList.clear(); // Clear the contactList before adding new data to avoid duplication

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Contact contact = child.getValue(Contact.class);
                    if (selectedCategory == null || contact.getCategory() == selectedCategory) {
                        contactList.add(contact);
                    }
                }

                List<Contact> filteredContacts = contactList.stream()
                        .filter(c -> filtr.getFirstNameFiltr() == null || c.getFirstName().toLowerCase().contains(filtr.getFirstNameFiltr().toLowerCase()))
                        .filter(c -> filtr.getLastNameFiltr() == null || c.getLastName().toLowerCase().contains(filtr.getLastNameFiltr().toLowerCase()))
                        .filter(c -> filtr.getAddressFiltr() == null || c.getAddress().toLowerCase().contains(filtr.getAddressFiltr().toLowerCase()))
                        .filter(c -> filtr.getBirthDateFiltr() == null || c.getBirthDate().toLowerCase().contains(filtr.getBirthDateFiltr().toLowerCase()))
                        .sorted(filtr.getComparator())
                        .collect(Collectors.toList());

                // Update the adapter with the new list of contacts
                adapter.updateContacts(filteredContacts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error reading data from Firebase
            }
        });
    }
}