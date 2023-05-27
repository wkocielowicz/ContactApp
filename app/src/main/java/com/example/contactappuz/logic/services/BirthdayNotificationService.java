package com.example.contactappuz.logic.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.contactappuz.R;
import com.example.contactappuz.logic.BirthdayNotificationManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BirthdayNotificationService extends Service {

    private static final String CHANNEL_ID = "BirthdayNotificationServiceChannel";
    private BirthdayNotificationManager birthdayNotificationManager;
    private ScheduledExecutorService scheduler;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String userId = intent.getStringExtra("userId");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Birthday Notification Service")
                .setContentText("Service is running...")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();

        startForeground(1, notification);

        birthdayNotificationManager = new BirthdayNotificationManager(this, userId);

        scheduler.scheduleAtFixedRate(() -> {
            birthdayNotificationManager.checkBirthdaysAndNotify();
        }, 0, 1, TimeUnit.MINUTES);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scheduler.shutdown();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Birthday Notification Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
