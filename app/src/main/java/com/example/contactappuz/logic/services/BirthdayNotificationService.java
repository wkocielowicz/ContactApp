package com.example.contactappuz.logic.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.contactappuz.logic.BirthdayNotificationManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BirthdayNotificationService extends Service {

    private BirthdayNotificationManager birthdayNotificationManager;
    private ScheduledExecutorService scheduler;

    @Override
    public void onCreate() {
        super.onCreate();

        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String userId = intent.getStringExtra("userId");

        // Tworzenie nowej instancji NotificationService tutaj
        birthdayNotificationManager = new BirthdayNotificationManager(this, userId);

        scheduler.scheduleAtFixedRate(() -> {
            // Ta funkcja będzie wywoływana co minutę
            birthdayNotificationManager.checkBirthdaysAndNotify();
        }, 0, 1, TimeUnit.MINUTES);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Serwis nie jest przeznaczony do powiązania, więc zwracamy null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Pamiętaj, aby zatrzymać scheduler przy zatrzymywaniu serwisu
        scheduler.shutdown();
    }
}