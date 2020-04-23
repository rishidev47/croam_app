package com.example.croam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
                    if(isLoggedin()){
                        Intent i=new Intent(getBaseContext(),MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Intent i=new Intent(getBaseContext(),LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                }catch (Exception e){

                }
            }
        };
        splash.start();
    }
    boolean isLoggedin(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext()); //Get the preferences
        String phone = prefs.getString("phone", null); //get a String
        String pswd =prefs.getString("pswd", null); //get a String
        boolean isLoggedin = prefs.getBoolean("isLoggedin", false); //get a boolean.
        //When the key "rememberCredentials" is not present, true is returned.
        Log.d("prefs", "isLoggedin: "+isLoggedin+ phone);
        Log.d("Prefs splashscreen", "isLoggedin: "+prefs.toString());
        return isLoggedin;
    }
}

