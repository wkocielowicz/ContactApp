package com.example.contactappuz.logic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.contactappuz.database.model.Contact;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * The PhotoManager class handles loading and saving contact photos.
 */
public class PhotoManager {

    /**
     * Loads a contact's photo from the device storage or downloads it from Firebase if not available locally.
     *
     * @param context        The context of the calling activity or fragment.
     * @param contact        The contact for which to load the photo.
     * @param onImageLoaded  A consumer to handle the loaded photo bitmap.
     */
    public static void loadImageFromDevice(Context context, Contact contact, Consumer<Bitmap> onImageLoaded) {
        File filePath = new File(context.getFilesDir(), contact.getPhotoPath() + ".jpeg");
        Log.d("PhotoManager", "Loading image from path: " + filePath.getAbsolutePath());  // Log image path
        Bitmap bitmap = BitmapFactory.decodeFile(filePath.toString());

        if (bitmap != null) {
            onImageLoaded.accept(bitmap);
        } else {
            Log.d("PhotoManager", "Image not found on device, downloading from Firebase");  // Log when downloading from Firebase
            FireBaseManager.downloadPhoto((Activity) context, contact, bitmapFromFirebase -> {
                if (!saveImageToDevice(context, bitmapFromFirebase, contact.getPhotoPath())) {
                    Log.e("PhotoManager", "Failed to save photo on device.");
                }
                onImageLoaded.accept(bitmapFromFirebase);
            });
        }
    }

    /**
     * Saves a contact's photo to the device storage.
     *
     * @param context       The context of the calling activity or fragment.
     * @param imageBitmap   The bitmap of the photo to save.
     * @param photoPath     The path at which to save the photo.
     * @return              True if the photo was saved successfully, false otherwise.
     */
    public static boolean saveImageToDevice(Context context, Bitmap imageBitmap, String photoPath) {
        File imageFile = new File(context.getFilesDir(), photoPath + ".jpeg");  // Add extension here
        File storageDir = imageFile.getParentFile();

        Log.d("PhotoManager", "Saving image to path: " + imageFile.getAbsolutePath());  // Log image saving path

        // Create the storage directories if they do not exist
        if (storageDir != null && !storageDir.exists()) {
            boolean mkdirsResult = storageDir.mkdirs();
            if (!mkdirsResult) {
                Log.e("PhotoManager", "Failed to create directory");
                return false;
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            Log.d("PhotoManager", "Image saved successfully");  // Log successful save
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("PhotoManager", "Error during saving image: ", e);
            return false;
        }

        return true;
    }
}
