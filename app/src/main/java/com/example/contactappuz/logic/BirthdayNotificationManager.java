package com.example.contactappuz.logic;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.major.MainActivity;
import com.example.contactappuz.database.model.Contact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BirthdayNotificationManager {

    private List<Contact> contactList;
    private Context context;
    private String userId;
    private List<Contact> birthdayContactList = new ArrayList<>();

    public BirthdayNotificationManager(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    public void checkBirthdaysAndNotify() {
        FireBaseManager.getContactsForUser(userId, contacts -> {
            contactList = contacts;
            createBirthdayContactList();
            for (Contact contact : birthdayContactList) {
                createNotificationChannel(contact);
                showBirthdayNotification(contact);
            }
        });
    }

    private void createNotificationChannel(Contact contact) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Birthday channel for " + contact.getFirstName();
            String CHANNEL_ID = "ch" + contact.getContactId();
            int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            android.app.NotificationManager notificationManager = context.getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createBirthdayContactList(){
        for (Contact contact : contactList) {
            if (CheckDate(contact.getBirthDate())) {
                birthdayContactList.add(contact);
            }
        }
    }

    private boolean CheckDate(String date) {
        String[] parts = date.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]) - 1;

        Calendar today = Calendar.getInstance();
        Calendar birthday = Calendar.getInstance();
        birthday.set(today.get(Calendar.YEAR), month, day); //Ustawiamy rok jako dzisiejszy, aby użyć w porównaniu DAY_OF_YEAR
        return today.get(Calendar.DAY_OF_YEAR) == birthday.get(Calendar.DAY_OF_YEAR);
    }

    private void showBirthdayNotification(Contact contact) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ch" + contact.getContactId())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(contact.getFirstName() + " " + contact.getLastName() + " ma dziś urodziny.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        int notificationId = contact.getContactId().hashCode();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}
