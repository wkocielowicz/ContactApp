package com.example.contactappuz.activities.minor;

import android.app.DatePickerDialog;
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
import android.widget.Toast;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

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

        if(mode == ActivityModeEnum.EDIT) {
            setFields();
        }
    }

    @Override
    public void attachListeners() {
        acceptButton.setOnClickListener(view -> {
            Contact contact = loadFields();

            if (mode == ActivityModeEnum.ADD) {
                saveContact(contact);
            } else if (mode == ActivityModeEnum.EDIT) {
                updateContact(contact);
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

    private Bitmap uriToBitmap(Uri selectedFileUri) {
        Bitmap image = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
            image = BitmapFactory.decodeStream(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AddEditContactActivity", "Error during converting Uri to Bitmap: ", e);
        }
        return image;
    }

    private void saveContact(Contact contact) {
        if(!PhotoManager.saveImageToDevice(AddEditContactActivity.this, uriToBitmap(selectedImageUri), contact.getPhotoPath())) {
            Toast.makeText(AddEditContactActivity.this, "Failed to save photo on device.", Toast.LENGTH_SHORT).show();
        }

        FireBaseManager.addContact(AddEditContactActivity.this, ActivityUtil.getUserId(), contact, selectedImageUri, task -> {
            if (task.isSuccessful()) {
                finish();
            }
        });
    }

    private void updateContact(Contact contact) {
        if(!PhotoManager.saveImageToDevice(AddEditContactActivity.this, uriToBitmap(selectedImageUri), contact.getPhotoPath())) {
            Toast.makeText(AddEditContactActivity.this, "Failed to save photo on device.", Toast.LENGTH_SHORT).show();
        }

        String contactId = getIntent().getStringExtra("contactId");
        if (contactId != null) {
            FireBaseManager.updateContact(AddEditContactActivity.this, ActivityUtil.getUserId(), contactId, contact, selectedImageUri, task -> {
                if (task.isSuccessful()) {
                    finish();
                }
            });
        } else {
            Toast.makeText(AddEditContactActivity.this, "Failed to update contact", Toast.LENGTH_SHORT).show();
        }
    }
}
