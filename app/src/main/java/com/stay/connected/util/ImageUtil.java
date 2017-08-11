package com.stay.connected.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by karthikeyan on 10/8/17.
 */

public class ImageUtil {

    public static Bitmap getThumbnail(Context context, Uri imageUri, int maxHeight, int maxWidth) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri), null, o);

        float scaleX = o.outWidth / maxWidth;
        float scaleY = o.outHeight / maxHeight;

        float scale = Math.max(scaleX, scaleY);
        int imageScale = 1;
        if (scale > 1) {
            imageScale = (int) Math.ceil(scale);
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = imageScale; // this only effecting for powers of 2
        Bitmap thumb = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri), null, o2); // note: this is null if photo is corrupt

        if (thumb != null) {
            if (thumb.getWidth() > maxWidth || thumb.getHeight() > maxHeight) {
                scaleX = (float) maxWidth / thumb.getWidth();
                scaleY = (float) maxHeight / thumb.getHeight();
                scale = Math.min(scaleY, scaleX);
                // we now scale to final size
                float dstWidth = scale * thumb.getWidth();
                float dstHeight = scale * thumb.getHeight();
                thumb = Bitmap.createScaledBitmap(thumb, (int) dstWidth, (int) dstHeight, true);
            }
        }

        return thumb;
    }

    public static Bitmap rotate(Bitmap bmap, float degrees) {
        // createa matrix for the manipulation
        Matrix matrix = new Matrix();
        // rotate the Bitmap
        matrix.postRotate(degrees);
        // recreate the new Bitmap
        return Bitmap.createBitmap(bmap, 0, 0, bmap.getWidth(), bmap.getHeight(), matrix, true);
    }

    public static int getPhotoOrientation(Context context, Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            if (inputStream != null) {
                Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
                Directory exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
                if (exifDirectory != null) {
                    if (exifDirectory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                        switch (exifDirectory.getInt(ExifIFD0Directory.TAG_ORIENTATION)) {
                            case 1:
                                return 0;
                            case 6:
                                return 90;
                            case 3:
                                return 180;
                            case 8:
                                return 270;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return 0;
    }

    /**
     * To store user avatar in {@link Context#getFilesDir()}
     * @param context
     * @param avatarBmp
     * @param email
     * @return the file path of the avatarBmp
     */
    public static String storeAvatar(Context context, Bitmap avatarBmp, String email){
        String filePath = context.getFilesDir()+"/"+email;

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            avatarBmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            filePath = null;
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  filePath;
        }
    }

    public static boolean isUserAvatarExists(Context context, String email){
        String filePath = context.getFilesDir()+"/"+email+"dfs";
        File file = new File(filePath);
        return file.exists();
    }
}
