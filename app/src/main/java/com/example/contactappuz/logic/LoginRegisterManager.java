package com.example.contactappuz.logic;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The LoginRegisterManager class handles user authentication and registration using Firebase Authentication.
 */
public class LoginRegisterManager {
    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    /**
     * Logs in a user with the specified email and password.
     *
     * @param email               The user's email.
     * @param password            The user's password.
     * @param onCompleteListener  A listener to handle the completion of the login process.
     */
    public static void loginUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }

    /**
     * Registers a new user with the specified email and password.
     *
     * @param email               The user's email.
     * @param password            The user's password.
     * @param onCompleteListener  A listener to handle the completion of the registration process.
     */
    public static void registerUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }

    /**
     * Signs out the currently signed-in user.
     */
    public static void signOutUser() {
        auth.signOut();
    }

    /**
     * Sets the FirebaseAuth instance for testing purposes.
     *
     * @param mAuth  The FirebaseAuth instance to set.
     */
    //Only for tests
    public static void setAuth(FirebaseAuth mAuth) {
        auth = mAuth;
    }
}
