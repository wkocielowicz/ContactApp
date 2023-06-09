package com.example.contactappuz.logic;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

/**
 * Manager class for handling ads.
 */
public class AdManager {

    private InterstitialAd mInterstitialAd;
    private static final String TAG = "AdManager";

    /**
     * Constructs an AdManager object.
     *
     * @param activity The activity in which the ads will be shown.
     */
    public AdManager(Activity activity) {
        MobileAds.initialize(activity, initializationStatus -> { });
    }

    /**
     * Loads and shows an interstitial ad.
     *
     * @param activity    The activity in which the ad will be shown.
     * @param onAdClosed  A Runnable to be executed when the ad is closed.
     */
    public void loadAndShowAdvert(Activity activity, Runnable onAdClosed) {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Log.d(TAG, "The advert was dismissed.");
                                onAdClosed.run(); // Run the provided function when ad is closed
                                mInterstitialAd = null; // Clear the ad object
                            }

                        });

                        mInterstitialAd.show(activity);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }
}
