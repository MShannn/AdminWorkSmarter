package com.jvidal.worksmarter.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.jvidal.worksmarter.R;

public class DisplayImageActivity extends AppCompatActivity {

    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        layout = findViewById(R.id.layout);


        Intent intent = getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
        BitmapDrawable background = new BitmapDrawable(getResources(), bitmap);
        layout.setBackground(background);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 8000);
    }
}
