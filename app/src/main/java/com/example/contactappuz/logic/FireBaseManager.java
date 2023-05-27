package com.example.contactappuz.logic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.contactappuz.database.model.Contact;
import com.example.contactappuz.util.ContactFilter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    public static void addContact(Activity activity, String userId, Contact contact, Uri imageUri, Consumer<Task<Void>> onTaskCompleted) {
        DatabaseReference newContactRef = getReferenceForUser(userId).push();
        contact.setContactId(newContactRef.getKey()); // assumes your Contact class has a setter for contactId

        uploadContactImageToFirebaseStorage(contact, imageUri, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Failed to upload contact image", Toast.LENGTH_SHORT).show();
            }
        });

        newContactRef.setValue(contact).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(activity, "Failed to add contact", Toast.LENGTH_SHORT).show();
            }
            onTaskCompleted.accept(task);
        });
    }

    public static void updateContact(Activity activity, String userId, String contactId, Contact contact, Uri imageUri, Consumer<Task<Void>> onTaskCompleted) {
        uploadContactImageToFirebaseStorage(contact, imageUri, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Failed to upload contact image", Toast.LENGTH_SHORT).show();
            }
        });

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

    public static void downloadPhoto(Activity activity, Contact contact, Consumer<Bitmap> onPhotoDownloaded) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Przykład ścieżki do pliku: "lCoA9aslI1R1s1WaRBkDvu2EWSH2/3599ecb6-a2d7-4ad7-bea9-2cf80bb2a28c/cb57d594-88af-4ea4-b125-c1578e3ea399"
        StorageReference photoRef = storage.getReference().child(contact.getPhotoUrl());

        photoRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            PhotoManager.saveImageToDevice(activity, bitmap, contact.getPhotoPath());
            onPhotoDownloaded.accept(bitmap);
        }).addOnFailureListener(e -> {
            Toast.makeText(activity, "Failed to download photo", Toast.LENGTH_SHORT).show();
        });
    }


    private static void uploadContactImageToFirebaseStorage(Contact contact, Uri imageUri, OnFailureListener onFailureListener) {
        if (imageUri != null) {
            StorageReference fileReference = FirebaseStorage.getInstance().getReference(contact.getPhotoUrl());
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            onFailureListener.onFailure(task.getException());
                        }
                    }))
                    .addOnFailureListener(onFailureListener);
        }
    }

}
