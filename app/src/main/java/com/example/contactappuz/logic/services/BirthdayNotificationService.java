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

/**
 * Service for managing birthday notifications.
 */
public class BirthdayNotificationService extends Service {

    private static final String CHANNEL_ID = "BirthdayNotificationServiceChannel";
    private BirthdayNotificationManager birthdayNotificationManager;
    private ScheduledExecutorService scheduler;

    /**
     * Called when the service is being created. This method is called only once during the
     * lifecycle of the service.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Called when the service is started. This method is called every time the service is
     * started using `startService()` method.
     *
     * @param intent  The intent passed to `startService()`.
     * @param flags   Additional data about this start request.
     * @param startId A unique integer representing this specific request to start.
     * @return The service's start mode.
     */
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

    /**
     * Called when a client is binding to the service. This method is not used in this service,
     * so it returns null.
     *
     * @param intent The intent that was used to bind to this service.
     * @return Always returns null.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Called when the service is being destroyed. This method is called only once during the
     * lifecycle of the service.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        scheduler.shutdown();
    }

    /**
     * Creates a notification channel for the service. This method is called only on devices
     * running Android Oreo (API level 26) or higher.
     */
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
