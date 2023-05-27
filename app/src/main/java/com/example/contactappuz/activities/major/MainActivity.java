package com.example.contactappuz.activities.major;

import static com.example.contactappuz.activities.util.ActivityUtil.getUserId;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.IActivity;
import com.example.contactappuz.activities.LanguageActivity;
import com.example.contactappuz.logic.AdManager;
import com.example.contactappuz.logic.services.BirthdayNotificationService;
import com.example.contactappuz.util.enums.mode.ActivityModeEnum;

import java.util.Locale;

/**
 * The main activity of the application.
 */
public class MainActivity extends LanguageActivity implements IActivity {

    private Button goToContactActivityButton;
    private Button changeLanguageButton;
    private AdManager adManager;

    private ActivityModeEnum mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mode = getIntentMode();
        initializeComponents();

        attachListeners();

        startServices();
    }

    /**
     * Retrieves the activity mode from the intent.
     *
     * @return The activity mode.
     */
    public ActivityModeEnum getIntentMode() {
        return (ActivityModeEnum) getIntent().getSerializableExtra("mode");
    }

    /**
     * Initializes the UI components of the activity.
     */
    @Override
    public void initializeComponents() {
        setContentView(R.layout.activity_main);

        adManager = new AdManager(this);

        goToContactActivityButton = findViewById(R.id.go_to_contact_activity_button);
        changeLanguageButton = findViewById(R.id.change_language_button);

    }

    /**
     * Attaches listeners to the buttons.
     */
    @Override
    public void attachListeners() {
        goToContactActivityButton.setOnClickListener(view -> {
            adManager.loadAndShowAdvert(this, () -> goToContactActivity());
        });

        changeLanguageButton.setOnClickListener(view -> {
            if (Locale.getDefault().getLanguage().equals("pl")) {
                changeLanguage("en");
            } else {
                changeLanguage("pl");
            }
        });
    }

    /**
     * Starts the required services for the activity.
     */
    private void startServices() {
        Intent serviceIntent = new Intent(this, BirthdayNotificationService.class);
        serviceIntent.putExtra("userId", getUserId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    /**
     * Navigates to the ContactActivity.
     */
    private void goToContactActivity() {
        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
        intent.putExtra("mode", ActivityModeEnum.VIEW);
        startActivity(intent);
    }
}
