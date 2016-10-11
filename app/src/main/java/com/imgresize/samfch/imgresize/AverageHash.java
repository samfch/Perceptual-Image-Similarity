package com.imgresize.samfch.imgresize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by samfch (samfch@unissula.ac.id) on 8/1/16.
 * Don't delete this comment.
 *
 * This algorithm is based on article here : http://www.hackerfactor.com/blog/index.php?/archives/432-Looks-Like-It.html
 *
 */
class AverageHash  {

    public static Bitmap resizeTo8x8(String filePath){
        Bitmap originalBitmap = BitmapFactory.decodeFile(filePath);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 8, 8, true);
        return resizedBitmap;
    }

    public static Bitmap toGreyscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpGrayscale);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(bmpOriginal, 0, 0, paint);

        return bmpGrayscale;
    }

// --Commented out by Inspection START (8/7/16, 21:30):
//    public static void saveNewFile(Bitmap bmp, String newPath, String newName){
//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream(newPath + "/" +newName);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
// --Commented out by Inspection STOP (8/7/16, 21:30)

    public static String buildHash(Bitmap grayscaleBitmap){
        int myHeight = grayscaleBitmap.getHeight();
        int myWidth  = grayscaleBitmap.getWidth();
        int totalPixVal = 0;

        for(int i = 0; i < myWidth; i++){
            for(int j = 0; j < myHeight; j++){
                int currPixel = grayscaleBitmap.getPixel(i, j)& 0xff; //read lowest byte of pixels
                totalPixVal += currPixel;
            }
        }

        int average = totalPixVal/64;
        String hashVal = "";
        for(int i = 0; i < myWidth; i++){
            for(int j = 0; j < myHeight; j++){
                int currPixel = grayscaleBitmap.getPixel(i, j)& 0xff; //read lowest byte of pixels
                if (currPixel >= average){
                    hashVal += "1";
                } else {
                    hashVal += "0";
                }
            }
        }
        return hashVal;
    }
}
