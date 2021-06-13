package com.operatorsapp.utils;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import com.operatorsapp.application.OperatorApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Date;


/**
 * Created by alex.lehtman on 11/12/2016.
 */

public class ImageHelper {


    // ----------------------------------------------------------------------------
    // -- helper method
    // -- this method will return a bitmap from file
    // -- it will down-sample the image, but the result will be at least minWidth X minHeight
    public static Bitmap decodeSampledBitmapFromFile(String path, Bitmap.Config config, int minWidth, int minHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 400;

        // Find the correct scale value. It should be the power of 2.
        int scale = 1;
        while (o.outWidth / scale / 2 >= minWidth && o.outHeight / scale / 2 >= minHeight)
            scale *= 2;

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
//
//        //First decode with inJustDecodeBounds=true to check dimensions
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, options);
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//
//        // 2. find the best re-sample factor (this has to be a power of 2 : 1,2,4,8,16...)
//        //    as long as we can divide by 2 but keep the image size
//        //    larger than minWidth X minHeight - do it
//        int factor = 1;
//
//        while (width / (factor * 2) >= minWidth && height / (factor * 2) >= minHeight) {
//            factor *= 2;
//        }

        // Decode bitmap with inSampleSize set
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, o2);
//        return BitmapFactory.decodeFile(path, options);
    }

    // ----------------------------------------------------------------------------
    // -- helper method
    // -- this method will try to read the EXIF orientation information on the image file
    public static int getExifOrientation(String path) {

        try {
            ExifInterface exif = new ExifInterface(path);
            return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        } catch (IOException e) {
            e.printStackTrace();
            return ExifInterface.ORIENTATION_UNDEFINED;
        }
    }

    // ----------------------------------------------------------------------------
    // -- helper method
    // -- this method will take a bitmap and return a re-oriented copy.
    // -- (and recycle the old bitmap!)
    public static Bitmap orientFromExif(Bitmap bitmap, int exifOrientation) {

        // transform matrix
        Matrix matrix = new Matrix();

        // check EXIF orientation

        switch (exifOrientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;

            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;

            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;

            default:
                // any other case - just return the original
                return bitmap;
        }

        // return a re-oriented copy, recycle the old bitmap!
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }catch (OutOfMemoryError error){
            // not enough memory, return the bitmap without orienting
        }
        return bitmap;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static String fileToBitmapToBase64(String path, Bitmap.Config config, int minWidth, int minHeight) {
        Bitmap bmp = decodeSampledBitmapFromFile(path, config, minWidth, minHeight);
        return bitmapToBase64(bmp);
    }

//    public static byte[] getByteArrFromUri(Context iContext, Uri iUri)
//    {
//        byte[] result = null;
//        ByteBuffer byteBuffer;
//
//        try
//        {
//            InputStream is = iContext.getContentResolver().openInputStream(iUri);
////       ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byteBuffer = ByteBuffer.allocate(is != null ? is.available() : 0);
//            int bufferSize = 1024;
//            byte[] buffer = new byte[bufferSize];
//
//            int len;
//
//            if (is != null)
//            {
//                while ((len = is.read(buffer)) != -1)
//                {
////             baos.write(buffer, 0, len);
//                    byteBuffer.put(buffer, 0, len);
//                }
//            }
//
////       result = baos.toByteArray();
//            result = byteBuffer.array();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return getDownscaledByteArray(result, 200, 200);
//    }
//
//    public static byte[] getDownscaledByteArray(byte[] imageBytes, int minWidth, int minHeight){
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
//
//        //First decode with inJustDecodeBounds=true to check dimensions
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//
//        // 2. find the best re-sample factor (this has to be a power of 2 : 1,2,4,8,16...)
//        //    as long as we can divide by 2 but keep the image size
//        //    larger than minWidth X minHeight - do it
//        int factor = 1;
//
//        while (width / (factor * 2) >= minWidth && height / (factor * 2) >= minHeight) {
//            factor *= 2;
//        }
//
//        // Decode bitmap with inSampleSize set
//        options.inSampleSize = factor;
//        options.inJustDecodeBounds = false;
//
//        return AppHelper.getFileDataFromBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options));
//
//    }

    public static File createImageFile()
    {
        // Store image in dcim
//		File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "pais" + new Date().getTime() + ".png");

        return new File(Environment.getExternalStorageDirectory() + "/DCIM/", "pais" + new Date().getTime() + ".png");
//				Uri imgUri = Uri.fromFile(file);
//		this.imgPath = file.getAbsolutePath();
//		return imgUri;
    }

    public static Bitmap decodeFile(String path)
    {
        try
        {
            return ImageHelper.orientFromExif(ImageHelper.decodeSampledBitmapFromFile(path, Bitmap.Config.ARGB_4444, 200, 200), ExifInterface.ORIENTATION_ROTATE_90);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getByteArrFromUri(Uri iUri, String absolutePath)
    {
        byte[] result = null;
        ByteBuffer byteBuffer;

        try
        {
            InputStream is = OperatorApplication.getAppContext().getContentResolver().openInputStream(iUri);
//       ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byteBuffer = ByteBuffer.allocate(is != null ? is.available() : 0);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;

            if (is != null)
            {
                while ((len = is.read(buffer)) != -1)
                {
//             baos.write(buffer, 0, len);
                    byteBuffer.put(buffer, 0, len);
                }
                is.close();
            }

//       result = baos.toByteArray();
            result = byteBuffer.array();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return getDownscaledByteArray(result, iUri, absolutePath, 200, 200);
    }

    public static byte[] getDownscaledByteArray(byte[] imageBytes, Uri iUri, String absolutePath, int minWidth, int minHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;

        //First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
        final int height = options.outHeight;
        final int width = options.outWidth;

        // 2. find the best re-sample factor (this has to be a power of 2 : 1,2,4,8,16...)
        //    as long as we can divide by 2 but keep the image size
        //    larger than minWidth X minHeight - do it
        int factor = 1;

        while (width / (factor * 2) >= minWidth && height / (factor * 2) >= minHeight) {
            factor *= 2;
        }

        // Decode bitmap with inSampleSize set
        options.inSampleSize = factor;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
        bitmap = orientFromExif(bitmap, getExifOrientation(absolutePath));
        return getFileDataFromBitmap(bitmap);

    }

    public static byte[] getFileDataFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}


