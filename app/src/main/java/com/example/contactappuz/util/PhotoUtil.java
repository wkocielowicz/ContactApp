package com.example.contactappuz.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The PhotoUtil class provides static utility methods for working with photos.
 * This includes methods for converting Bitmaps to Uris, Uris to Bitmaps, and for scaling Bitmaps.
 * The methods in this class require a Context to interact with the Android file system and content resolvers.
 */
public class PhotoUtil {
    /**
     * Scales a Bitmap to a given width and height.
     *
     * @param bitmap The Bitmap to scale.
     * @param newWidth The new width of the Bitmap.
     * @param newHeight The new height of the Bitmap.
     * @return The scaled Bitmap.
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    /**
     * This method is used to convert the bitmap to uri.
     * @param mBitmap This is the bitmap which is to be converted to uri.
     * @param context The context from the application or activity
     * @return Uri This returns the Uri of the saved image.
     */
    public static Uri bitmapToUriConverter(Bitmap mBitmap, Context context) {
        // Get the context wrapper
        ContextWrapper wrapper = new ContextWrapper(context);

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
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }

    /**
     * Converts a Uri to a Bitmap.
     *
     * @param selectedFileUri The Uri of the selected image.
     * @param context The context from the application or activity
     * @return The Bitmap object.
     */
    public static Bitmap uriToBitmap(Uri selectedFileUri, Context context) {
        Bitmap image = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(selectedFileUri);
            Bitmap originalImage = BitmapFactory.decodeStream(inputStream);
            if (originalImage != null) {
                image = scaleBitmap(originalImage, 400, 400);
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("UtilityClass", "Error during converting Uri to Bitmap: ", e);
        }
        return image;
    }

}
