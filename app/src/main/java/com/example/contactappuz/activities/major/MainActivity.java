package com.example.contactappuz.activities.major;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.LanguageActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MainActivity extends LanguageActivity {

    private Button addContactButton, goToContactActivityButton, changeLanguageButton;
    private DatabaseReference contactRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addContactButton = findViewById(R.id.add_contact_button);
        goToContactActivityButton = findViewById(R.id.go_to_contact_activity_button);
        changeLanguageButton = findViewById(R.id.change_language_button);

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

        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Locale.getDefault().getLanguage().equals("pl")) {
                    // Aktualnie wybrany jest język polski
                    changeLanguage("en");
                } else {
                    // Aktualnie wybrany jest inny język
                    changeLanguage("pl");
                }
            }
        });
    }

    private void addSampleContact() {

    }

    private void goToContactActivity() {
        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
        startActivity(intent);
    }
}