package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.os.Handler;
import android.content.Intent;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import  com.github.rahatarmanahmed.cpv.CircularProgressViewAdapter;


public class SplashActivity extends AppCompatActivity {


    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        imageView = (ImageView) findViewById(R.id.imageView2);

        imageView.setImageResource(R.drawable.logomedium);

        Thread background = new Thread() {
            public  void run () {
                try {

                    sleep(500);

                    Intent i = new Intent((getBaseContext()),MainActivity.class);
                    startActivity(i);

                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
