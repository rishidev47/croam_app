package com.example.croam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class LoginActivity extends AppCompatActivity {
    public static String croam_server_url="https://backend-279606.el.r.appspot.com/";
    public static Activity activity =null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        Log.d("prefs", "in login activity");
        setContentView(R.layout.activity_login);
        Fragment fragment = new Login();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_fragment_container, fragment)
                .commit();

    }

}
