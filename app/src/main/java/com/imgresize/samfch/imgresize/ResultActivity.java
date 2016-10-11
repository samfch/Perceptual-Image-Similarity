package com.imgresize.samfch.imgresize;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initViews();

    }

    private void initViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        int fileCount = getIntent().getIntExtra("fileCount",0);
        long runningTime = getIntent().getLongExtra("runningTime", 0);
        ArrayList<ImageResult> imgExtras = (ArrayList<ImageResult>) getIntent().getSerializableExtra("data");
        Collections.sort(imgExtras);
        DataAdapter adapter = new DataAdapter(getApplicationContext(),imgExtras);
        recyclerView.setAdapter(adapter);

        TextView resultTv = (TextView) findViewById(R.id.textViewResult);
        resultTv.setText("Found " +imgExtras.size()+ " similar image(s) out of " + fileCount + " images in " + runningTime +" ms");

        String imgQPath = getIntent().getStringExtra("queryImg");
        Bitmap qBitmap = BitmapUtil.resizeBitmapToImageView(imgQPath);
        ImageView qImage = (ImageView) findViewById(R.id.imageViewQ);
        qImage.setImageBitmap(qBitmap);
    }

}
