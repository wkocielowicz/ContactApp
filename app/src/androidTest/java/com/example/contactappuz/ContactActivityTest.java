package com.example.contactappuz;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.contactappuz.model.Contact;
import com.example.contactappuz.util.ContactCategory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ContactActivityTest {

    FirebaseIdlingResource firebaseIdlingResource = new FirebaseIdlingResource();

    @Rule
    public ActivityScenarioRule<ContactActivity> mActivityRule = new ActivityScenarioRule<>(ContactActivity.class);

    @Before
    public void setUp() {
        // Write sample data to Firebase before running tests
        DatabaseReference contactRef = FirebaseDatabase.getInstance().getReference("contacts");
        Contact[] contacts = new Contact[]{
                new Contact(ContactCategory.FAMILY, "John", "Doe", "123 Family Street", "1990-01-01"),
                new Contact(ContactCategory.FRIENDS, "Jane", "Smith", "456 Friends Avenue", "1991-02-02"),
                new Contact(ContactCategory.WORK, "Bob", "Johnson", "789 Work Road", "1992-03-03")
        };

        Map<String, Object> contactsMap = new HashMap<>();
        for (int i = 0; i < contacts.length; i++) {
            contactsMap.put("contact" + i, contacts[i]);
        }
        contactRef.setValue(contactsMap);
    }

    @Test
    public void getContactsFromFirebaseTest() {
        // Wybierz kategorię FAMILY z rozwijanej listy
        onView(withId(R.id.contactCategorySpinner))
                .perform(click());
        onView(withText("Family"))
                .perform(click());

        // Sprawdź, czy kontakt z kategorii FAMILY jest wyświetlany na liście
        onView(withId(R.id.recyclerViewContact))
                .check(matches(hasDescendant(withText("John Doe"))));

        // Wybierz kategorię FRIENDS z rozwijanej listy
        onView(withId(R.id.contactCategorySpinner))
                .perform(click());
        onView(withText("FRIENDS"))
                .perform(click());

        // Sprawdź, czy kontakt z kategorii FRIENDS jest wyświetlany na liście
        onView(withId(R.id.recyclerViewContact))
                .check(matches(hasDescendant(withText("Jane Smith"))));

        // Wybierz kategorię WORK z rozwijanej listy
        onView(withId(R.id.contactCategorySpinner))
                .perform(click());
        onView(withText("Work"))
                .perform(click());

        // Sprawdź, czy kontakt z kategorii WORK jest wyświetlany na liście
        onView(withId(R.id.recyclerViewContact))
                .check(matches(hasDescendant(withText("Bob Johnson"))));

        // Wybierz kategorię ALL z rozwijanej listy
        onView(withId(R.id.contactCategorySpinner))
                .perform(click());
        onView(withText("All"))
                .perform(click());

        // Sprawdź, czy kontakty ze wszystkich kategorii są wyświetlane na liście
        onView(withId(R.id.recyclerViewContact))
                .check(matches(hasDescendant(withText("John Doe"))))
                .check(matches(hasDescendant(withText("Jane Smith"))))
                .check(matches(hasDescendant(withText("Bob Johnson"))));
    }
}