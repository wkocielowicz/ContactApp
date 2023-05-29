package com.example.contactappuz.database.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The Statistics class represents the walking statistics of a user.
 * It keeps track of the total steps and daily steps of a user.
 * The class implements Serializable, allowing it to be passed between activities.
 */
public class Statistics implements Serializable {

    private int totalSteps;
    private Map<String, Integer> dailySteps;

    /**
     * Default constructor for the Statistics class.
     * It initializes the dailySteps map as a new, empty HashMap.
     */
    public Statistics() {
        dailySteps = new HashMap<>();
    }

    /**
     * Retrieves the total steps count.
     *
     * @return total steps as an integer.
     */
    public int getTotalSteps() {
        return totalSteps;
    }

    /**
     * Sets the total steps count.
     *
     * @param totalSteps an integer representing the total steps.
     */
    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    /**
     * Retrieves the daily steps count.
     *
     * @return a map where keys are the dates in a string format and values are the steps count for each date.
     */
    public Map<String, Integer> getDailySteps() {
        return dailySteps;
    }

    /**
     * Sets the daily steps count.
     *
     * @param dailySteps a map where keys are the dates in a string format and values are the steps count for each date.
     */
    public void setDailySteps(Map<String, Integer> dailySteps) {
        this.dailySteps = dailySteps;
    }
}
