package com.example.contactappuz.logic.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.util.ActivityUtil;
import com.example.contactappuz.logic.StepCounterManager;

/**
 * A Service that counts steps using a StepCounterManager and saves the statistics in a Firebase database.
 * The Service runs in the foreground and shows a notification to the user.
 */
public class StepCounterService extends Service {

    private static final String CHANNEL_ID = "StepCounterServiceChannel";
    private StepCounterManager stepCounterManager;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            stepCounterManager.saveStatistics(ActivityUtil.getUserId());
            handler.postDelayed(this, 60000); // Run again after 1 minute
        }
    };

    /**
     * Initializes the Service and creates a notification channel.
     * A StepCounterManager is also initialized in this method.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        stepCounterManager = new StepCounterManager(this);
    }

    /**
     * Starts the service in the foreground, starts counting steps, and begins saving statistics every minute.
     *
     * @param intent The Intent that was used to bind to this service.
     * @param flags Additional data about this start request.
     * @param startId A unique integer representing this specific request to start.
     * @return The return value indicates what semantics the system should use for the service's current started state.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Step Counter Service")
                .setContentText("Service is running...")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();

        startForeground(1, notification);

        stepCounterManager.startCountingSteps();
        handler.post(runnable); // Start saving statistics every minute

        return START_STICKY;
    }

    /**
     * When binding to the service, return null as this is a started service.
     *
     * @param intent The Intent that was used to bind to this service.
     * @return Return null because this is a started service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Return null for a started service.
    }

    /**
     * Called when the Service is no longer used and is being destroyed.
     * The handler callbacks are removed and the step counting is stopped.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Stop saving statistics when the service is destroyed
        stepCounterManager.stopCountingSteps();
    }

    /**
     * Creates a notification channel for the service. This method is called only on devices
     * running Android Oreo (API level 26) or higher.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Step Counter Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
