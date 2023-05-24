package com.example.contactappuz.users;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.major.LoginRegisterActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginRegisterActivityEspressoTest {
    private View decorView;

    @Rule
    public ActivityScenarioRule<LoginRegisterActivity> activityRule =
            new ActivityScenarioRule<>(LoginRegisterActivity.class);

    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
    }

    @Test
    public void registerWithMismatchedPasswordsShowsToast() {
        // Przełącz na tryb rejestracji
        onView(withId(R.id.switchModeButton)).perform(click());

        // Wprowadź e-mail, hasło i niepasujące potwierdzenie hasła
        onView(withId(R.id.usernameEditText)).perform(typeText("test@example.com"));
        onView(withId(R.id.passwordEditText)).perform(typeText("password555"));
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText("password123"));

        // Zamknij klawiaturę
        onView(withId(R.id.confirmPasswordEditText)).perform(closeSoftKeyboard());

        // Kliknij przycisk rejestracji
        onView(withId(R.id.loginRegisterButton)).perform(click());

        // Odczekaj chwilę, zanim sprawdzisz Toast
        try {
            Thread.sleep(2000); // Czekaj przez 0,1 sekundy
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Sprawdź, czy Toast z odpowiednim komunikatem jest wyświetlony
        onView(withText(R.string.passwords_do_not_match))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));

    }

    @Test
    public void switchModeButtonChangesModes() {
        // Sprawdź, czy na początku jest tryb logowania
        onView(withId(R.id.titleLoginRegisterTextView)).check(matches(withText(R.string.loginTittle)));
        onView(withId(R.id.switchModeButton)).check(matches(withText(R.string.loginSwitchToRegister)));
        onView(withId(R.id.confirmPasswordEditText)).check(matches(not(isDisplayed())));

        // Przełącz na tryb rejestracji
        onView(withId(R.id.switchModeButton)).perform(click());

        // Sprawdź, czy tryb rejestracji jest aktywny
        onView(withId(R.id.titleLoginRegisterTextView)).check(matches(withText(R.string.registrationTittle)));
        onView(withId(R.id.switchModeButton)).check(matches(withText(R.string.registerSwitchToLogin)));
        onView(withId(R.id.confirmPasswordEditText)).check(matches(isDisplayed()));

        // Przełącz z powrotem na tryb logowania
        onView(withId(R.id.switchModeButton)).perform(click());

        // Sprawdź, czy tryb logowania jest ponownie aktywny
        onView(withId(R.id.titleLoginRegisterTextView)).check(matches(withText(R.string.loginTittle)));
        onView(withId(R.id.switchModeButton)).check(matches(withText(R.string.loginSwitchToRegister)));
        onView(withId(R.id.confirmPasswordEditText)).check(matches(not(isDisplayed())));
    }
}