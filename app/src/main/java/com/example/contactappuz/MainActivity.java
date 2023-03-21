package com.example.contactappuz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactappuz.model.Contact;
import com.example.contactappuz.util.ContactCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button addContactButton, goToContactActivityButton;
    private DatabaseReference contactRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addContactButton = findViewById(R.id.add_contact_button);
        goToContactActivityButton = findViewById(R.id.go_to_contact_activity_button);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        contactRef = database.getReference("contacts");

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSampleContact();
            }
        });

        goToContactActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToContactActivity();
            }
        });
    }

    private void addSampleContact() {
        // Choose a random ContactCategory from the available options
        ContactCategory[] categories = ContactCategory.values();
        Random random = new Random();
        int randomIndex = random.nextInt(categories.length);
        ContactCategory randomCategory = categories[randomIndex];

        // Create a sample Contact
        Contact sampleContact = new Contact(randomCategory, "Jan", "Nowak", "Warszawa 15", "10.10.2000");

        // Add the sample Contact to Firebase
        contactRef.push().setValue(sampleContact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Contact added successfully
                } else {
                    // An error occurred while adding the contact
                }
            }
        });
    }

    private void goToContactActivity() {
        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
        startActivity(intent);
    }
}