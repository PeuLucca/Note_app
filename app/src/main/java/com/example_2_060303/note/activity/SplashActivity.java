package com.example_2_060303.note.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example_2_060303.note.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView imgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgLoading = findViewById(R.id.imgLoading);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity( new Intent( getApplicationContext(), MainActivity.class ));
                finish();
            }
        },500 /* 0.5 segundos */);

        animateImage( imgLoading );
    }

    public void animateImage( ImageView ratingImage){

        ScaleAnimation scaleAnimation = new ScaleAnimation( 0, 1f, 0 ,1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(500);
        ratingImage.startAnimation( scaleAnimation );

    }

}