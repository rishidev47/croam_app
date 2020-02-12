package com.example.croam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar=(ProgressBar)findViewById(R.id.progress);

        Thread splash=new Thread(){
            public void run(){
                try{
                    sleep(2000);

                    Intent i=new Intent(getBaseContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }catch (Exception e){

                }
            }
        };
        splash.start();
    }
}
