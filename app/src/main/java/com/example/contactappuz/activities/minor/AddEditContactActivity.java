package com.example.contactappuz.activities.minor;

import android.app.DatePickerDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.contactappuz.R;
import com.example.contactappuz.activities.IActivity;
import com.example.contactappuz.activities.LanguageActivity;
import com.example.contactappuz.activities.util.ActivityUtil;
import com.example.contactappuz.database.model.Contact;
import com.example.contactappuz.logic.FireBaseManager;
import com.example.contactappuz.logic.PhotoManager;
import com.example.contactappuz.util.enums.ContactCategoryEnum;
import com.example.contactappuz.util.enums.mode.ActivityModeEnum;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
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

    private ActivityModeEnum mode;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;

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
    }

    /**
     * Sets the field values based on the contact received from the intent.
     */
    private void setFields() {
        Contact contact = (Contact) getIntent().getSerializableExtra("contact");
        if (contact != null) {
            firstNameEditText.setText(contact.getFirstName());
            lastNameEditText.setText(contact.getLastName());
            addressEditText.setText(contact.getAddress());
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
        contact.setAddress(addressEditText.getText().toString());
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
     * Converts a Uri to a Bitmap.
     *
     * @param selectedFileUri The Uri of the selected image.
     * @return The Bitmap object.
     */
    private Bitmap uriToBitmap(Uri selectedFileUri) {
        Bitmap image = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
            Bitmap originalImage = BitmapFactory.decodeStream(inputStream);
            if (originalImage != null) {
                image = scaleBitmap(originalImage, 400, 400);
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AddEditContactActivity", "Error during converting Uri to Bitmap: ", e);
        }
        return image;
    }

    /**
     * Saves a contact to the Firebase database.
     *
     * @param contact The Contact object to be saved.
     */
    private void saveContact(Contact contact) {
        Bitmap scaledBitmap = uriToBitmap(selectedImageUri);
        Uri scaledImageUri = bitmapToUriConverter(scaledBitmap);

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
        Bitmap scaledBitmap = uriToBitmap(selectedImageUri);
        Uri scaledImageUri = bitmapToUriConverter(scaledBitmap);

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

    /**
     * Scales a Bitmap to a given width and height.
     *
     * @param bitmap The Bitmap to scale.
     * @param newWidth The new width of the Bitmap.
     * @param newHeight The new height of the Bitmap.
     * @return The scaled Bitmap.
     */
    private Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    /**
     * This method is used to convert the bitmap to uri.
     * @param mBitmap This is the bitmap which is to be converted to uri.
     * @return Uri This returns the Uri of the saved image.
     */
    private Uri bitmapToUriConverter(Bitmap mBitmap) {
        // Get the context wrapper
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        // Initialize a new file in specific directory
        File file = wrapper.getExternalFilesDir("Images");
        file = new File(file, "UniqueFileName"+".jpg");

        try {
            // Compress the bitmap and save in the file
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();

        } catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Return the saved bitmap uri
        return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
    }

}
