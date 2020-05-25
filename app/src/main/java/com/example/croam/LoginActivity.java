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
    public static String croam_server_url="http://192.168.43.35:3000";
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
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Server Detail")
                .setMessage("Enter Server Url without trailing \"\\\" \nIf you don't know what is this click on Cancel")
                .setView(input)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        String url= input.getText().toString();
                        if(URLUtil.isValidUrl(url)){
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); //Get the preferences
                            prefs.edit().putString("server_url",url).commit();
                            croam_server_url=url;
                            CRoamService.croam_server_url=url;
                        }
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
//            Toast.makeText(getContext(), msg[0], Toast.LENGTH_SHORT).show();

    }

}
