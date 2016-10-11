package com.imgresize.samfch.imgresize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by samfch on 8/6/16.
 */
class BitmapUtil {

    public static Bitmap resizeBitmapToImageView(String path) {
        Bitmap resizedBitmap = null;
        try {
            int inWidth;
            int inHeight;
            int dstWidth = 1000;
            int dstHeight = 1000;

            InputStream in = new FileInputStream(path);

            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // decode full image pre-resized
            in = new FileInputStream(path);
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)

            options.inSampleSize = Math.max(inWidth / dstWidth, inHeight / dstHeight);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            // resize bitmap
            resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);

            // save image
//            try {
//                FileOutputStream out = new FileOutputStream(path);
//                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
//            } catch (Exception e) {
////                Log.e("Image", e.getMessage(), e);
//            }
        } catch (IOException e) {
//            Log.e("Image", e.getMessage(), e);
        }
        return resizedBitmap;
    }

}
