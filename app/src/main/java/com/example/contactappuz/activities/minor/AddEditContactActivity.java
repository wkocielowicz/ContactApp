package com.example.contactappuz.activities.minor;

import static com.example.contactappuz.util.AddressUtil.placeToAddress;
import static com.example.contactappuz.util.PhotoUtil.bitmapToUriConverter;
import static com.example.contactappuz.util.PhotoUtil.uriToBitmap;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.IActivity;
import com.example.contactappuz.activities.LanguageActivity;
import com.example.contactappuz.activities.util.ActivityUtil;
import com.example.contactappuz.database.model.Address;
import com.example.contactappuz.database.model.Contact;
import com.example.contactappuz.logic.FireBaseManager;
import com.example.contactappuz.logic.PhotoManager;
import com.example.contactappuz.util.enums.ContactCategoryEnum;
import com.example.contactappuz.util.enums.mode.ActivityModeEnum;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * The AddEditContactActivity class handles the addition and editing of contacts.
 */
public class AddEditContactActivity extends LanguageActivity implements IActivity {

    private Button acceptButton;
    private Button backButton;
    private Button clearButton;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText addressEditText;
    private EditText birthDateEditText;
    private Spinner categorySpinner;
    private ImageView photoImageView;

    PlacesClient placesClient;

    private ActivityModeEnum mode;
    private Uri selectedImageUri;
    private Address selectedAddress;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mode = getIntentMode();
        initializeComponents();
        attachListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            selectedImageUri = imageUri;
            photoImageView.setImageURI(imageUri);
        }

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            Place selectedPlace = Autocomplete.getPlaceFromIntent(data);

            selectedAddress = placeToAddress(selectedPlace);
            addressEditText.setText(selectedAddress.getAddress());
        }
    }

    /**
     * Retrieves the mode (ADD or EDIT) from the intent.
     *
     * @return The activity mode.
     */
    public ActivityModeEnum getIntentMode() {
        return (ActivityModeEnum) getIntent().getSerializableExtra("mode");
    }

    @Override
    public void initializeComponents() {
        setContentView(R.layout.activity_add_edit_contact);

        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);
        clearButton = findViewById(R.id.clearButton);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        photoImageView = findViewById(R.id.photoImageView);

        ActivityUtil.initializeCategorySpinner(categorySpinner, ContactCategoryEnum.class, this);

        if (mode == ActivityModeEnum.EDIT) {
            setFields();
        }

        Places.initialize(getApplicationContext(), "AIzaSyBd9vdotS9KZiTAhj6wkjV5irrRD6f12u8");
        placesClient = Places.createClient(this);
    }

    @Override
    public void attachListeners() {
        acceptButton.setOnClickListener(view -> {
            if (validateFields()) {
                Contact contact = loadFields();

                if (mode == ActivityModeEnum.ADD) {
                    saveContact(contact);
                } else if (mode == ActivityModeEnum.EDIT) {
                    updateContact(contact);
                }
            }
        });

        backButton.setOnClickListener(view -> {
            // Close the activity when the back button is pressed
            finish();
        });

        clearButton.setOnClickListener(view -> {
            // Clear all the fields when the clear button is pressed
            firstNameEditText.setText("");
            lastNameEditText.setText("");
            addressEditText.setText("");
            birthDateEditText.setText("");
            categorySpinner.setSelection(0); // Set the spinner back to the default selection
        });

        birthDateEditText.setOnClickListener(view -> {
            showDatePickerDialog();
        });

        photoImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
        });

        addressEditText.setOnClickListener(view -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });
    }

    /**
     * Sets the field values based on the contact received from the intent.
     */
    private void setFields() {
        Contact contact = (Contact) getIntent().getSerializableExtra("contact");
        if (contact != null) {
            firstNameEditText.setText(contact.getFirstName());
            lastNameEditText.setText(contact.getLastName());
            addressEditText.setText(contact.getAddress().getAddress());
            birthDateEditText.setText(contact.getBirthDate());
            int spinnerPosition = ((ArrayAdapter<String>) categorySpinner.getAdapter()).getPosition(contact.getCategory());
            categorySpinner.setSelection(spinnerPosition);

            // Download the photo from Firebase and set it to the ImageView
            FireBaseManager.downloadPhoto(this, contact, bitmap -> {
                photoImageView.setImageBitmap(bitmap);
            });
        }
    }

    /**
     * Loads the field values into a Contact object.
     *
     * @return The Contact object with field values.
     */
    private Contact loadFields() {
        Contact contact = new Contact();

        contact.setFirstName(firstNameEditText.getText().toString());
        contact.setLastName(lastNameEditText.getText().toString());
        contact.setAddress(selectedAddress);
        contact.setBirthDate(birthDateEditText.getText().toString());
        contact.setCategory(categorySpinner.getSelectedItem().toString());

        String photoId = UUID.randomUUID().toString();
        String fullPath = String.format("%s/%s/%s", ActivityUtil.getUserId(), contact.getContactId(), photoId);
        contact.setPhotoPath(fullPath);
        contact.setPhotoUrl(fullPath);

        return contact;
    }

    /**
     * Validates if all fields are filled.
     *
     * @return true if all fields are filled, false otherwise.
     */
    private boolean validateFields() {
        if (firstNameEditText.getText().toString().trim().isEmpty()) {
            firstNameEditText.setError("Pole wymagane");
            return false;
        } else if (lastNameEditText.getText().toString().trim().isEmpty()) {
            lastNameEditText.setError("Pole wymagane");
            return false;
        } else if (addressEditText.getText().toString().trim().isEmpty()) {
            addressEditText.setError("Pole wymagane");
            return false;
        } else if (birthDateEditText.getText().toString().trim().isEmpty()) {
            birthDateEditText.setError("Pole wymagane");
            return false;
        } else if (categorySpinner.getSelectedItem().toString().trim().isEmpty()) {
            ((TextView) categorySpinner.getSelectedView()).setError("");
            Toast.makeText(this, "Wybierz kategorię", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedImageUri == null) {
            Toast.makeText(this, "Dodaj zdjęcie", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    /**
     * Shows a DatePickerDialog to select the birth date.
     */
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Increment month by 1 because January is 0
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                birthDateEditText.setText(date);
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        // Show the dialog
        datePickerDialog.show();
    }

    /**
     * Saves a contact to the Firebase database.
     *
     * @param contact The Contact object to be saved.
     */
    private void saveContact(Contact contact) {
        Bitmap scaledBitmap = uriToBitmap(selectedImageUri, AddEditContactActivity.this);
        Uri scaledImageUri = bitmapToUriConverter(scaledBitmap, AddEditContactActivity.this);

        if (!PhotoManager.saveImageToDevice(AddEditContactActivity.this, scaledBitmap, contact.getPhotoPath())) {
            Toast.makeText(AddEditContactActivity.this, "Failed to save photo on device.", Toast.LENGTH_SHORT).show();
        }

        FireBaseManager.addContact(AddEditContactActivity.this, ActivityUtil.getUserId(), contact, scaledImageUri, task -> {
            if (task.isSuccessful()) {
                finish();
            }
        });
    }


    /**
     * Updates a contact in the Firebase database.
     *
     * @param contact The Contact object to be updated.
     */
    private void updateContact(Contact contact) {
        Bitmap scaledBitmap = uriToBitmap(selectedImageUri, AddEditContactActivity.this);
        Uri scaledImageUri = bitmapToUriConverter(scaledBitmap, AddEditContactActivity.this);

        if (!PhotoManager.saveImageToDevice(AddEditContactActivity.this, scaledBitmap, contact.getPhotoPath())) {
            Toast.makeText(AddEditContactActivity.this, "Failed to save photo on device.", Toast.LENGTH_SHORT).show();
        }

        String contactId = getIntent().getStringExtra("contactId");
        if (contactId != null) {
            FireBaseManager.updateContact(AddEditContactActivity.this, ActivityUtil.getUserId(), contactId, contact, scaledImageUri, task -> {
                if (task.isSuccessful()) {
                    finish();
                }
            });
        } else {
            Toast.makeText(AddEditContactActivity.this, "Failed to update contact", Toast.LENGTH_SHORT).show();
        }
    }
}
