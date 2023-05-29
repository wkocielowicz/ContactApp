package com.example.contactappuz.logic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.contactappuz.database.model.Statistics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A class responsible for counting the steps of a user using the device's step counter sensor.
 * The class can start and stop counting steps, and save these statistics for a user in a Firebase database.
 */
public class StepCounterManager implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;

    private int totalSteps;
    private int dailySteps;

    /**
     * The constructor for the StepCounterManager.
     *
     * @param context The context in which the StepCounterManager is being used.
     */
    public StepCounterManager(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }
    }

    /**
     * Starts counting the steps of a user.
     * It registers the class as a listener for the device's step counter sensor.
     */
    public void startCountingSteps() {
        if (sensorManager != null && stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    /**
     * Stops counting the steps of a user.
     * It unregisters the class as a listener for the device's step counter sensor.
     */
    public void stopCountingSteps() {
        if (sensorManager != null && stepCounterSensor != null) {
            sensorManager.unregisterListener(this, stepCounterSensor);
        }
    }

    /**
     * This method is called when the step counter sensor reports a new value.
     * It updates the total number of steps and increments the daily step count.
     *
     * @param sensorEvent The new sensor event reported by the step counter sensor.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == stepCounterSensor) {
            totalSteps = (int) sensorEvent.values[0];
            dailySteps++; // Increment daily steps here. Reset this count daily at midnight.
        }
    }

    /**
     * This method is called when the accuracy of the step counter sensor changes.
     * This method is not used in this class, but needs to be implemented as part of the SensorEventListener interface.
     *
     * @param sensor The sensor whose accuracy has changed.
     * @param i The new accuracy of the sensor.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // You may want to do something here.
    }

    /**
     * Returns the total number of steps that have been counted.
     *
     * @return The total number of steps.
     */
    public int getTotalSteps() {
        return totalSteps;
    }

    /**
     * Returns the number of steps that have been counted today.
     *
     * @return The number of steps counted today.
     */
    public int getDailySteps() {
        return dailySteps;
    }

    /**
     * Saves the user's step statistics (total and daily steps) in a Firebase database.
     *
     * @param userId The id of the user for whom the statistics are being saved.
     */
    public void saveStatistics(String userId) {
        Statistics statistics = new Statistics(); // Create a new Statistics object
        statistics.setTotalSteps(totalSteps); // Set the total steps

        // Get the current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        // Get the current daily steps count for this date, or default to 0 if there's no entry yet
        int currentDailySteps = statistics.getDailySteps().getOrDefault(currentDate, 0);

        // Update the daily steps count
        statistics.getDailySteps().put(currentDate, currentDailySteps + dailySteps);

        FireBaseManager.saveStatistics(userId, statistics); // Save statistics to Firebase
    }

}
