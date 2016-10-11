package com.imgresize.samfch.imgresize;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mvc.imagepicker.ImagePicker;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView placeholder;
    private TextView sizeTv;
    private String queryImgPath;
    private ArrayList<ImageResult> imgRes;
    private int totalFiles=0;
    private int threshold = 80;
    private String searchPath = "/sdcard/Download";
    private long runningTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placeholder = (ImageView) findViewById(R.id.imageViewPlaceholder);
        sizeTv = (TextView) findViewById(R.id.textViewSize);
        Button resizeButton = (Button) findViewById(R.id.button);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        searchPath = sp.getString("folderPicker", searchPath);
        threshold = Integer.parseInt(sp.getString("threshold", String.valueOf(threshold)));

        resizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                resizeImg();

                new SearchImage().execute();

            }
        });

        Button browseButton = (Button) findViewById(R.id.buttonBrowse);
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage(MainActivity.this, "Select Query Image");
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.preferences:
            {
                Intent i = new Intent(this, SettingActivity.class);
                startActivity(i);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        // TODO do something with the bitmap
        placeholder.setImageBitmap(bitmap);
        Uri uri = data.getData();
        String uriString = getRealPathFromURI(uri);
        int sizeByte = bitmap.getByteCount()/1024;
        sizeTv.setText(uriString + " | " + sizeByte + "kb");
        queryImgPath = uriString;

    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.indexOf("image") == 0;
    }

    class SearchImage extends AsyncTask<String, String, String>{
        ProgressDialog progDailog;
        final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String searchFolder = SP.getString("folderPicker",searchPath);
        final int threshold = Integer.parseInt(SP.getString("threshold", "80"));

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(MainActivity.this);
            progDailog.setMessage("Searching similar images in" + searchFolder);
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }
        @Override
        protected String doInBackground(String... aurl) {

//            String dlpath = Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/WhatsApp Images";

            File f = new File(searchFolder);
            File file[] = f.listFiles();

            long startTime = System.nanoTime();
            //query image
            Bitmap resizedBmpQ = AverageHash.resizeTo8x8(queryImgPath);
            Bitmap grayscaleBmpQ = AverageHash.toGreyscale(resizedBmpQ);
            String hashQ = AverageHash.buildHash(grayscaleBmpQ);

            imgRes = new ArrayList<>();

            for (File fl : file){
                //check if file is image
                if (isImageFile(searchFolder + "/" +fl.getName())){
                    Bitmap resizedBmp = AverageHash.resizeTo8x8(searchFolder + "/" +fl.getName());
                    Bitmap grayscaleBmp = AverageHash.toGreyscale(resizedBmp);
                    String hashF = AverageHash.buildHash(grayscaleBmp);
                    int distance = new Hamming(hashQ, hashF).getHammingDistance();
                    int percent = (100 - distance);
                    if (percent >= threshold ){ //threshold here
                        ImageResult imgResult = new ImageResult();
                        imgResult.setImgUrl(searchFolder + "/" +fl.getName());
                        imgResult.setPercentSimilarity(String.valueOf(percent));
                        imgRes.add(imgResult);
    //                Log.e("DISTANCE", fl.getName() +" = "+ percent +"%");
//                    } else {
    //                Log.e("DISTANCE", fl.getName() +" = "+ percent +"% NOT SIMILAR");
                    }
                    totalFiles++;
                }

            }
            long endTime = System.nanoTime();
            runningTime = (endTime - startTime)/1000000; //in milliseconds
            return null;
        }
        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);

            //go to result activity
            Intent intent = new Intent(MainActivity.this,ResultActivity.class);
            intent.putExtra("data", imgRes); //Put your id to your next Intent
            intent.putExtra("queryImg", queryImgPath);
            intent.putExtra("fileCount", totalFiles);
            intent.putExtra("runningTime", runningTime);
            startActivity(intent);
            totalFiles =0;
            progDailog.dismiss();
        }

    }

}





