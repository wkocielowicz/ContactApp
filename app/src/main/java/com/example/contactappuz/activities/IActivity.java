package com.example.contactappuz.activities;

import com.example.contactappuz.util.enums.ActivityModeEnum;

public interface IActivity {

    ActivityModeEnum getIntentMode();
    void initializeComponents(ActivityModeEnum mode);
    void attachListeners();

}