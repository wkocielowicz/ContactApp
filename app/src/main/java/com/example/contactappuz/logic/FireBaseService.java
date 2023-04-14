package com.example.contactappuz.logic;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.contactappuz.database.model.Contact;
import com.example.contactappuz.util.ContactFilter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FireBaseService {
    private static final DatabaseReference CONTACT_REF = FirebaseDatabase.getInstance().getReference("contacts");

    private FireBaseService() {
    }

    public static void getContactsFromFirebase(Activity activity, String selectedCategory, ContactFilter contactFilter, Consumer<List<Contact>> onContactsFetched) {
        CONTACT_REF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Contact> contactList = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Contact contact = child.getValue(Contact.class);
                    if (selectedCategory == null || selectedCategory.equals("All") || contact.getCategory().equals(selectedCategory)) {
                        contactList.add(contact);
                    }
                }

                List<Contact> filteredContacts = contactList.stream()
                        .filter(c -> contactFilter.getFirstNameFiltr() == null || c.getFirstName().toLowerCase().contains(contactFilter.getFirstNameFiltr().toLowerCase()))
                        .filter(c -> contactFilter.getLastNameFiltr() == null || c.getLastName().toLowerCase().contains(contactFilter.getLastNameFiltr().toLowerCase()))
                        .filter(c -> contactFilter.getAddressFiltr() == null || c.getAddress().toLowerCase().contains(contactFilter.getAddressFiltr().toLowerCase()))
                        .filter(c -> contactFilter.getBirthDateFiltr() == null || c.getBirthDate().toLowerCase().contains(contactFilter.getBirthDateFiltr().toLowerCase()))
                        .sorted(contactFilter.getComparator())
                        .collect(Collectors.toList());

                onContactsFetched.accept(filteredContacts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, "No access to the database.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void addContact(Activity activity, Contact contact, Consumer<Task<Void>> onTaskCompleted) {
        CONTACT_REF.push().setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(activity, "Failed to add contact", Toast.LENGTH_SHORT).show();
                }
                onTaskCompleted.accept(task);
            }
        });
    }

    public static void updateContact(Activity activity, String contactId, Contact contact, Consumer<Task<Void>> onTaskCompleted) {
        CONTACT_REF.orderByChild("contactId").equalTo(contactId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                    contactSnapshot.getRef().setValue(contact).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(activity, "Failed to update contact", Toast.LENGTH_SHORT).show();
                        }
                        onTaskCompleted.accept(task);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to find contact: " + databaseError.getMessage());
            }
        });
    }

    public static void deleteContact(String contactId, OnCompleteListener<Void> onCompleteListener) {
        DatabaseReference contactsRef = FirebaseDatabase.getInstance().getReference("contacts");

        contactsRef.orderByChild("contactId").equalTo(contactId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                    contactSnapshot.getRef().removeValue()
                            .addOnCompleteListener(onCompleteListener)
                            .addOnFailureListener(e -> Log.e("FirebaseError", "Failed to delete contact: " + e.getMessage()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to find contact: " + databaseError.getMessage());
            }
        });
    }
}
