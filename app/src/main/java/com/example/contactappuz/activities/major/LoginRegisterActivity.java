package com.example.contactappuz.activities.major;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.IActivity;
import com.example.contactappuz.activities.LanguageActivity;
import com.example.contactappuz.logic.AdManager;
import com.example.contactappuz.logic.LoginRegisterManager;
import com.example.contactappuz.util.enums.mode.ActivityModeEnum;
import com.example.contactappuz.util.enums.mode.LoginRegisterModeEnum;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The activity responsible for user login and registration.
 */
public class LoginRegisterActivity extends LanguageActivity implements IActivity {

    private Button loginRegisterButton;
    private Button switchModeButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private TextView titleLoginRegisterTextView;

    private LoginRegisterModeEnum mode;
    private AdManager adManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("logged_in_user_email", null);
        if (email != null) {
            // User is logged in, start main activity
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                login(user);
            }
        } else {
            // User is not logged in, initialize login/register components
            initializeComponents();
            attachListeners();
        }
    }

    /**
     * Initializes the UI components of the activity.
     */
    @Override
    public void initializeComponents() {
        setContentView(R.layout.activity_login_register);

        mode = LoginRegisterModeEnum.LOGIN;
        adManager = new AdManager(this);

        titleLoginRegisterTextView = findViewById(R.id.titleLoginRegisterTextView);
        loginRegisterButton = findViewById(R.id.loginRegisterButton);
        switchModeButton = findViewById(R.id.switchModeButton);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
    }

    /**
     * Attaches listeners to the buttons.
     */
    @Override
    public void attachListeners() {
        loginRegisterButton.setOnClickListener(view -> {
            String email = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (mode == LoginRegisterModeEnum.LOGIN) {
                loginWithAdsFunction(email, password);

            } else if (mode == LoginRegisterModeEnum.REGISTER) {
                registerFunction(email, password);
            }
        });

        switchModeButton.setOnClickListener(view -> {
            switchModeLogic();
        });
    }

    /**
     * Performs login with an ad display for the specified email and password.
     *
     * @param email    The user's email address.
     * @param password The user's password.
     */
    private void loginWithAdsFunction(String email, String password) {
        LoginRegisterManager.loginUser(email, password, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();

                // Save user email to SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("logged_in_user_email", email);
                editor.apply();

                // Load and show the advert
                adManager.loadAndShowAdvert(this, () -> login(user));
            } else {
                // Login failed
                Toast.makeText(LoginRegisterActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Performs the login operation with the specified user.
     *
     * @param user The logged-in user.
     */
    private void login(FirebaseUser user) {
        // Navigate to main activity when ad is closed
        Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
        intent.putExtra("mode", ActivityModeEnum.VIEW);
        intent.putExtra("userId", user.getUid());

        startActivity(intent);
        finish();
    }

    /**
     * Performs the registration with the specified email and password.
     *
     * @param email    The user's email address.
     * @param password The user's password.
     */
    private void registerFunction(String email, String password) {
        String confirmedPassword = confirmPasswordEditText.getText().toString();

        if (password.equals(confirmedPassword)) {
            LoginRegisterManager.registerUser(email, password, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginRegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    // Switch to LOGIN mode after successful registration
                    mode = LoginRegisterModeEnum.LOGIN;
                    titleLoginRegisterTextView.setText(R.string.loginTittle);
                    switchModeButton.setText(R.string.loginSwitchToRegister);
                    confirmPasswordEditText.setVisibility(View.GONE);
                } else {
                    Toast.makeText(LoginRegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(LoginRegisterActivity.this, R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Switches between login and registration mode.
     */
    private void switchModeLogic() {
        if (mode == LoginRegisterModeEnum.LOGIN) {
            mode = LoginRegisterModeEnum.REGISTER;
            titleLoginRegisterTextView.setText(R.string.registrationTittle);
            switchModeButton.setText(R.string.registerSwitchToLogin);
            confirmPasswordEditText.setVisibility(View.VISIBLE);
            // Clear fields when switching to REGISTER mode
            usernameEditText.setText("");
            passwordEditText.setText("");
            confirmPasswordEditText.setText("");
        } else {
            mode = LoginRegisterModeEnum.LOGIN;
            titleLoginRegisterTextView.setText(R.string.loginTittle);
            switchModeButton.setText(R.string.loginSwitchToRegister);
            confirmPasswordEditText.setVisibility(View.GONE);
            // Clear fields when switching to LOGIN mode
            usernameEditText.setText("");
            passwordEditText.setText("");
            confirmPasswordEditText.setText("");
        }
    }
}
