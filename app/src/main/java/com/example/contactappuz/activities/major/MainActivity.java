package com.example.contactappuz.activities.major;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.IActivity;
import com.example.contactappuz.activities.LanguageActivity;
import com.example.contactappuz.util.enums.mode.ActivityModeEnum;

import java.util.Locale;

public class MainActivity extends LanguageActivity implements IActivity {

    private Button goToContactActivityButton;
    private Button changeLanguageButton;

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
        setContentView(R.layout.activity_main);

        goToContactActivityButton = findViewById(R.id.go_to_contact_activity_button);
        changeLanguageButton = findViewById(R.id.change_language_button);

    }

    @Override
    public void attachListeners() {
        goToContactActivityButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            intent.putExtra("mode", ActivityModeEnum.VIEW);
            startActivity(intent);
        });

        changeLanguageButton.setOnClickListener(view -> {
            if (Locale.getDefault().getLanguage().equals("pl")) {
                changeLanguage("en");
            } else {
                changeLanguage("pl");
            }
        });
    }
}