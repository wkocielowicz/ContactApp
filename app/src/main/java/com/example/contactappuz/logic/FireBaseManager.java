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

public class FireBaseManager {
    private static FirebaseDatabase database;

    private FireBaseManager() {
    }

    private static DatabaseReference getReferenceForUser(String userId) {
        return FirebaseDatabase.getInstance().getReference("users/" + userId + "/contacts");
    }

    public static void getContactsFromFirebase(Activity activity, String userId, String selectedCategory, ContactFilter contactFilter, Consumer<List<Contact>> onContactsFetched) {
        getReferenceForUser(userId).addValueEventListener(new ValueEventListener() {
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
                        .filter(c -> contactFilter.getFirstNameFilter() == null || c.getFirstName().toLowerCase().contains(contactFilter.getFirstNameFilter().toLowerCase()))
                        .filter(c -> contactFilter.getLastNameFilter() == null || c.getLastName().toLowerCase().contains(contactFilter.getLastNameFilter().toLowerCase()))
                        .filter(c -> contactFilter.getAddressFilter() == null || c.getAddress().toLowerCase().contains(contactFilter.getAddressFilter().toLowerCase()))
                        .filter(c -> contactFilter.getBirthDateFilter() == null || c.getBirthDate().toLowerCase().contains(contactFilter.getBirthDateFilter().toLowerCase()))
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

    public static void getContactsForUser(String userId, Consumer<List<Contact>> onContactsFetched) {
        getReferenceForUser(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Contact> contactList = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Contact contact = child.getValue(Contact.class);
                    contactList.add(contact);
                }

                onContactsFetched.accept(contactList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void addContact(Activity activity, String userId, Contact contact, Consumer<Task<Void>> onTaskCompleted) {
        getReferenceForUser(userId).push().setValue(contact).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(activity, "Failed to add contact", Toast.LENGTH_SHORT).show();
            }
            onTaskCompleted.accept(task);
        });
    }

    public static void updateContact(Activity activity, String userId, String contactId, Contact contact, Consumer<Task<Void>> onTaskCompleted) {
        getReferenceForUser(userId).orderByChild("contactId").equalTo(contactId).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public static void deleteContact(String userId, String contactId, OnCompleteListener<Void> onCompleteListener) {
        DatabaseReference contactsRef = FirebaseDatabase.getInstance().getReference("contacts");

        getReferenceForUser(userId).orderByChild("contactId").equalTo(contactId).addListenerForSingleValueEvent(new ValueEventListener() {
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
