package com.gusar.datingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gusar.datingapp.imagesdownloader.ImageLoader;

/**
 * Created by igusar on 2/9/16.
 */
public class MatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_screen);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        ImageView photo = (ImageView) findViewById(R.id.match_photo);
        ImageLoader imageLoader = new ImageLoader(this);
        imageLoader.DisplayImage(url, photo);
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int valueInPixels = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        width -= valueInPixels;
        photo.getLayoutParams().width = width;
        photo.getLayoutParams().height = width;
    }
}
