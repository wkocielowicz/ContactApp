package com.example.contactappuz.activities.major;

import static com.example.contactappuz.activities.util.ActivityUtil.getUserId;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.IActivity;
import com.example.contactappuz.activities.LanguageActivity;
import com.example.contactappuz.activities.minor.StatisticsActivity;
import com.example.contactappuz.logic.AdManager;
import com.example.contactappuz.logic.LoginRegisterManager;
import com.example.contactappuz.logic.services.BirthdayNotificationService;
import com.example.contactappuz.logic.services.StepCounterService;
import com.example.contactappuz.util.enums.mode.ActivityModeEnum;

import java.util.Locale;

/**
 * The main activity of the application.
 */
public class MainActivity extends LanguageActivity implements IActivity {

    private Button goToContactActivityButton;
    private Button changeLanguageButton;
    private Button goToBluetoothActivityButton;
    private Button goToStatistics;
    private Button logoutButton;
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
        goToBluetoothActivityButton = findViewById(R.id.go_to_bluetooth_activity_button);
        goToStatistics = findViewById(R.id.go_to_statistics_button);
        logoutButton = findViewById(R.id.logout_button);
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

        goToBluetoothActivityButton.setOnClickListener(view -> {
            goToBluetoothActivity();
        });

        goToStatistics.setOnClickListener(view -> {
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(view -> {
            LoginRegisterManager.signOutUser();

            // Clear shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Starts the required services for the activity.
     */
    private void startServices() {
        Intent birthdayServiceIntent = new Intent(this, BirthdayNotificationService.class);
        birthdayServiceIntent.putExtra("userId", getUserId());

        Intent stepCounterServiceIntent = new Intent(this, StepCounterService.class);
        stepCounterServiceIntent.putExtra("userId", getUserId());

        startForegroundService(birthdayServiceIntent);
        startForegroundService(stepCounterServiceIntent);
    }

    /**
     * Navigates to the ContactActivity.
     */
    private void goToContactActivity() {
        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
        intent.putExtra("mode", ActivityModeEnum.VIEW);
        startActivity(intent);
    }

    /**
     * Navigates to the BluetoothActivity.
     */
    private void goToBluetoothActivity() {
        Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
        intent.putExtra("mode", ActivityModeEnum.VIEW);
        startActivity(intent);
    }
}
