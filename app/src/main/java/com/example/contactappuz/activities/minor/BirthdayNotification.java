package com.example.contactappuz.activities.minor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.IActivity;
import com.example.contactappuz.activities.LanguageActivity;
import com.example.contactappuz.activities.major.ContactActivity;
import com.example.contactappuz.activities.major.MainActivity;
import com.example.contactappuz.activities.util.ActivityUtil;
import com.example.contactappuz.database.model.Contact;
import com.example.contactappuz.logic.FireBaseService;
import com.example.contactappuz.util.ContactFilter;
import com.example.contactappuz.util.enums.mode.ActivityModeEnum;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;

public class BirthdayNotification extends LanguageActivity implements IActivity{

    private List<Contact> birthdayContactList = new ArrayList<>();
    private ActivityModeEnum mode;
    private List<Contact> birthdayContactList = new ArrayList<>();
    private List<Contact> contactList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mode = getIntentMode();
        initializeComponents();

        attachListeners();

        birthdayContactList.clear();
        fetchContacts();
        if (!birthdayContactList.isEmpty()) {
            showBirthdayNotifications();
        }
    //sdfsdfsdfsdfs
    }

    private void fetchContacts() {
        String userId = ActivityUtil.getUserId(); // Use actual user Id
        String selectedCategory = "All";
        ContactFilter contactFilter = new ContactFilter(); // Initialize as per your requirement

        FireBaseService.getContactsFromFirebase(null, userId, selectedCategory, contactFilter,
                new Consumer<List<Contact>>() {
                    @Override
                    public void accept(List<Contact> fetchedContacts) {
                        contactList = fetchedContacts;
                        // You can now use the fetched contacts here
                        // Or you can call some method
                        createbirthdayContactList();
                    }
                });
    }

    /**
     Metoda tworzy kanał do wyświetlania notyfikacji.
     **/

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "channel 1";
            String CHANNEL_ID = "ch1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     Metoda tworzy listę kontaktów, które mają dziś urodziny.
     **/

    private void createbirthdayContactList(){
        for (Contact contact:contactList) {
            if (CheckDate(contact.getBirthDate())) {
                birthdayContactList.add(contact);
            }
        }
    }

    /**
     Metoda sprawdza, czy podana data jest dzisiejszą datą.
     **/

    private boolean CheckDate(String date) {
        String[] parts = date.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]) - 1;

        Calendar today = Calendar.getInstance();
        Calendar birthday = Calendar.getInstance();
        birthday.set(today.get(Calendar.YEAR), month, day); //Ustawiamy rok jako dzisiejszy, aby użyć w porównaniu DAY_OF_YEAR
        return today.get(Calendar.DAY_OF_YEAR) == birthday.get(Calendar.DAY_OF_YEAR);
    }

    /**
     Metoda wyświetla powiadomienia o urodzinach.
     **/

    private void showBirthdayNotifications() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        for (Contact contact : birthdayContactList) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ch1")
                    .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                    .setContentTitle(contact.getFirstName() + " " + contact.getLastName() + " ma dziś urodziny.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    //.setContentText("Znajomy ma dzisiaj urodziny!")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            int notificationId = Integer.parseInt(contact.getContactId());
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, builder.build());
        }
    }
    public ActivityModeEnum getIntentMode() {
        return (ActivityModeEnum) getIntent().getSerializableExtra("mode");
    }


    @Override
    public void initializeComponents() {

    }

    @Override
    public void attachListeners() {

    }
}
