package com.example.contactappuz.logic;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginRegisterService {
    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    public static void loginUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }

    public static void registerUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }

    //Only for tests
    public static void setAuth(FirebaseAuth mAuth) {
        auth = mAuth;
    }
}
