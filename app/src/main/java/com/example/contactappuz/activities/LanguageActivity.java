package com.example.contactappuz.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    protected static final String SHARED_PREFS = "language_prefs";
    protected static final String LANG_KEY = "lang_key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String langCode = prefs.getString(LANG_KEY, "en");
        Context context = changeLanguage(newBase, langCode);
        super.attachBaseContext(context);
    }

    protected void changeLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration(getResources().getConfiguration());
        config.setLocale(locale);
        Context newContext = createConfigurationContext(config);

        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString(LANG_KEY, languageCode);
        editor.apply();

        Intent refresh = new Intent(newContext, getClass());
        startActivity(refresh);
        finish();
    }

    private Context changeLanguage(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }
}