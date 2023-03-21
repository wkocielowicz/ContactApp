package com.example.contactappuz;

import androidx.test.espresso.IdlingResource;

public class FirebaseIdlingResource implements IdlingResource {
    private ResourceCallback resourceCallback;
    private boolean isIdle;

    public FirebaseIdlingResource() {
        isIdle = false;
    }

    @Override
    public String getName() {
        return FirebaseIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    public void setIdleState(boolean isIdle) {
        this.isIdle = isIdle;
        if (isIdle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
    }
}