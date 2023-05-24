package com.example.contactappuz.users;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.contactappuz.logic.LoginRegisterService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class LoginRegisterServiceAndroidTest {
    private FirebaseAuth auth;

    @Before
    public void setUp() {
        Context appContext = ApplicationProvider.getApplicationContext();
        FirebaseApp app = FirebaseApp.initializeApp(appContext);
        FirebaseDatabase.getInstance().useEmulator("10.0.2.2", 9000);
        FirebaseAuth.getInstance(app).useEmulator("10.0.2.2", 9099);

        auth = FirebaseAuth.getInstance();
        LoginRegisterService.setAuth(auth);
    }

    @Test
    public void testRegisterUser() throws InterruptedException {
        String email = "test@email.com";
        String password = "test123";
        CountDownLatch latch = new CountDownLatch(1);

        LoginRegisterService.registerUser(email, password, task -> {
            if (task.isSuccessful()) {
                assertTrue(task.isSuccessful());
                auth.getCurrentUser().delete();
                latch.countDown();
            } else {
                Log.d("Error: ", task.getException().getMessage());
                System.out.println("Error: " + task.getException().getMessage());
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testLoginUser() throws InterruptedException {
        String email = "user@test.com";
        String password = "admin123";
        CountDownLatch registerLatch = new CountDownLatch(1);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        registerLatch.countDown();
                    } else {
                        System.out.println("Error creating user: " + task.getException().getMessage());
                        Log.d("Error: ", task.getException().getMessage());
                        registerLatch.countDown();
                    }
                });

        registerLatch.await(10, TimeUnit.SECONDS);

        if (auth.getCurrentUser() != null) {
            CountDownLatch loginLatch = new CountDownLatch(1);

            LoginRegisterService.loginUser(email, password, task -> {
                assertTrue(task.isSuccessful());
                auth.getCurrentUser().delete();
                loginLatch.countDown();
            });

            loginLatch.await(10, TimeUnit.SECONDS);
        } else {
            fail("User not created");
        }
    }
}
