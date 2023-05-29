package com.example.contactappuz.activities.minor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.IActivity;
import com.example.contactappuz.activities.LanguageActivity;
import com.example.contactappuz.activities.util.ActivityUtil;
import com.example.contactappuz.database.model.Statistics;
import com.example.contactappuz.logic.FireBaseManager;

import java.util.function.Consumer;

/**
 * The StatisticsActivity class is responsible for displaying the daily and total steps count
 * to the user. This class retrieves the latest statistics from Firebase every minute.
 */
public class StatisticsActivity extends LanguageActivity implements IActivity {

    private TextView todayStepsTextView;
    private TextView totalStepsTextView;
    private Button exitButton;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final String userId = ActivityUtil.getUserId();

    /**
     * Defines a runnable task that is used to fetch and display the steps count every minute.
     */
    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            FireBaseManager.getStatistics(userId, new Consumer<Statistics>() {
                @Override
                public void accept(Statistics statistics) {
                    todayStepsTextView.setText(String.valueOf(statistics.getDailySteps()));
                    totalStepsTextView.setText(String.valueOf(statistics.getTotalSteps()));
                }
            });
            handler.postDelayed(this, 60000); // Run again after 1 minute
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeComponents();
        attachListeners();
    }

    @Override
    public void initializeComponents() {
        setContentView(R.layout.activity_statistics);

        todayStepsTextView = findViewById(R.id.today_steps);
        totalStepsTextView = findViewById(R.id.total_steps);
        exitButton = findViewById(R.id.exit_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(updateRunnable); // Start updating steps count every minute
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateRunnable); // Stop updating steps count when the activity is paused
    }

    @Override
    public void attachListeners() {
        exitButton.setOnClickListener(v -> finish());
    }
}
