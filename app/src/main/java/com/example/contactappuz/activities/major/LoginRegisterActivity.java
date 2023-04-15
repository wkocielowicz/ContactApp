package com.example.contactappuz.activities.major;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.IActivity;
import com.example.contactappuz.activities.LanguageActivity;
import com.example.contactappuz.logic.LoginRegisterService;
import com.example.contactappuz.util.enums.mode.ActivityModeEnum;
import com.example.contactappuz.util.enums.mode.LoginRegisterModeEnum;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterActivity extends LanguageActivity implements IActivity {

    private Button loginRegisterButton;
    private Button switchModeButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private TextView titleLoginRegisterTextView;

    private LoginRegisterModeEnum mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeComponents();

        attachListeners();
    }

    @Override
    public void initializeComponents() {
        setContentView(R.layout.activity_login_register);

        mode = LoginRegisterModeEnum.LOGIN;

        titleLoginRegisterTextView = findViewById(R.id.titleLoginRegisterTextView);
        loginRegisterButton = findViewById(R.id.loginRegisterButton);
        switchModeButton = findViewById(R.id.switchModeButton);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
    }

    @Override
    public void attachListeners() {
        loginRegisterButton.setOnClickListener(view -> {
            String email = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (mode == LoginRegisterModeEnum.LOGIN) {
                loginFunction(email, password);

            } else if (mode == LoginRegisterModeEnum.REGISTER) {
                registerFunction(email, password);
            }
        });

        switchModeButton.setOnClickListener(view -> {
            if (mode == LoginRegisterModeEnum.LOGIN) {
                mode = LoginRegisterModeEnum.REGISTER;
                titleLoginRegisterTextView.setText(R.string.registrationTittle);
                switchModeButton.setText(R.string.registerSwitchToLogin);
                confirmPasswordEditText.setVisibility(View.VISIBLE);

            } else {
                mode = LoginRegisterModeEnum.LOGIN;
                titleLoginRegisterTextView.setText(R.string.loginTittle);
                switchModeButton.setText(R.string.loginSwitchToRegister);
                confirmPasswordEditText.setVisibility(View.GONE);
            }
        });
    }

    private void loginFunction(String email, String password) {
        LoginRegisterService.loginUser(email, password, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
                intent.putExtra("mode", ActivityModeEnum.VIEW);
                intent.putExtra("userId", user.getUid());
                startActivity(intent);
                finish();
            } else {
                // Błąd logowania
                Toast.makeText(LoginRegisterActivity.this, "Logowanie nieudane: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registerFunction(String email, String password) {
        String confirmedPassword = confirmPasswordEditText.getText().toString();

        if(password == confirmedPassword) {
            LoginRegisterService.registerUser(email, password, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginRegisterActivity.this, "Zarejestrowano pomyślnie", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginRegisterActivity.this, "Rejestracja nieudana: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(LoginRegisterActivity.this, "Podane hasła nie są ze sobą zgodne!", Toast.LENGTH_SHORT).show();
        }
    }
}