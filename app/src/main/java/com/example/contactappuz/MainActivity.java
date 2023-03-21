package com.example.contactappuz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Inicjalizacja pakietu SDK do reklam mobilnych Google.*/
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadAdvert();
        showAdvert();
    }

    @Override
    protected void onPause() {
        super.onPause();
        showAdvert();
    }

    /*Metoda wczytująca reklamę pełnoekranową.*/
    public void loadAdvert() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // mInterstitialAd będzie miało wartość null dopóki reklama jest ładowana
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");

                        /*Obsługa zdarzeń związanych z wyświetlaniem reklamy pełnoekranowej.*/
                        mInterstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdClicked() {
                                        // Metoda wywoływana gdy reklama została kliknięta.
                                        Log.d(TAG, "The advert was clicked.");
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        //Metoda wywoływana, gdy reklama jest zamykana.
                                        MainActivity.this.mInterstitialAd = null;
                                        Log.d("TAG", "The advert dismissed fullscreen content.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Metoda wywoływana, gdy reklama się nie wyświetla.
                                        MainActivity.this.mInterstitialAd = null;
                                        Log.d("TAG", "The advert failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Metoda wywoływana, gdy reklama się wyświetla.
                                        Log.d("TAG", "The advert was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Obsługa błędu załadowania reklamy.
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    /*Metoda uruchamiająca reklamę pełnoekranową*/
    private void showAdvert(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);
        } else {
            Log.d("TAG", "The advert wasn't ready yet.");
        }
    }
}