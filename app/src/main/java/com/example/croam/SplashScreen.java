package com.example.croam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread splash = new Thread() {
            public void run() {
                try {
                    sleep(1000);

                    if (isOnBoarding()) {
                        Intent i = new Intent(getBaseContext(), OnBoardingActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        if (isLoggedin()) {
                            Intent i = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(getBaseContext(), LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        splash.start();
    }

    private boolean isOnBoarding() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext()); //Get the preferences
        return prefs.getBoolean("new", true);
//        return true;
    }

    boolean isLoggedin() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext()); //Get the preferences
        String phone = prefs.getString("phone", null); //get a String
        String pswd = prefs.getString("pswd", null); //get a String
        boolean isLoggedin = prefs.getBoolean("isLoggedin", false); //get a boolean.
        //When the key "rememberCredentials" is not present, true is returned.
        Log.d("prefs", "isLoggedin: " + isLoggedin + phone);
        Log.d("Prefs splashscreen", "isLoggedin: " + prefs.toString());
        return isLoggedin;
    }
}

