package com.example.croam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.EditText;

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
                .setMessage("Enter Server Url \nIf you don't know what is this click on Cancel")
                .setView(input)

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        String url= input.getText().toString();
                        if(URLUtil.isValidUrl(url)){
                            croam_server_url=url;
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
