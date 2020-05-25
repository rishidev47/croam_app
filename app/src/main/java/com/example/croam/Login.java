package com.example.croam;

import static com.example.croam.LoginActivity.croam_server_url;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Login extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button login = view.findViewById(R.id.btn_login);
        final EditText phone = view.findViewById(R.id.editText_login_phone);
        final EditText password = view.findViewById(R.id.editText_login_password);

        TextView goToSignup = view.findViewById(R.id.text_login);
        goToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Signup();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login_fragment_container, fragment)
                        .commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_phone = phone.getText().toString();
                String input_pswd = password.getText().toString();

                doLogin(input_phone, input_pswd);
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void doLogin(final String phone, final String pswd) {
        final Context mContext=getContext();
        Log.v("DOLOGIN", phone+" "+pswd);
        final String msg[]={"Error"};
        final Integer ret[]={-1};
        final boolean status[]={false};
        final JSONObject user[]={null};
        Runnable login=new Runnable() {
            @Override
            public void run() {
                try {
                    String charset = "UTF-8";
                    String requestURL = croam_server_url + "/login";
                    MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                    multipart.addFormField("phone", phone);
                    multipart.addFormField("password", pswd);
                    multipart.addFormField("dummy",null);
                    Log.v("DOLOGIN", phone+ " "  +pswd);
                    List<String> response = multipart.finish();
                    String res = "";
                    for (String line : response) {
                        Log.v("rht", "Line : " + line);
                        res = res + line + "\n";
                    }
                    System.out.println("Server Response on login "+res);
                    try{
                        JSONObject jsobj = new JSONObject(res);

                        status[0]= jsobj.getBoolean("status");
                        msg[0]= jsobj.getString("message");
                        ret[0]= jsobj.getInt("error");
                        if(status[0])user[0]= jsobj.getJSONArray("data").getJSONObject(0);

                    }catch (Exception ex){
                        System.out.println("JSON Error "+ex);
                    }

                } catch (Exception e) {
                    msg[0]=" Connection Error:\n Check Your Internet Connection ";
                    System.out.println("Error in login" + e);
                }
//                if(getActivity()==null)return;
                //using LoginActivity.activity to avoid crash in case of switching fragments while results are awaited
               LoginActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(status[0]){
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext()); //Get the preferences
                            SharedPreferences.Editor edit = prefs.edit(); //Needed to edit the preference
                            edit.putString("phone", phone);  //add a String
                            edit.putString("pswd", pswd);
                            edit.putBoolean("isLoggedin", true); //add a boolean
                            try{
                                edit.putString("name",user[0].getString("name"));
                                edit.putInt("age",user[0].getInt("age"));
                                edit.putInt("gender",user[0].getInt("gender"));
                                edit.putString("id",user[0].getString("id"));

                            }catch (Exception ex){
                                System.out.println("Error in getting user data "+ex);
                            }
                            Log.d("Prefs login", "onClick: " + prefs.toString());
                            edit.commit();  // save the edits.

                     Toast.makeText(getContext(), msg[0], Toast.LENGTH_SHORT).show();
                        }else {

                            new AlertDialog.Builder(LoginActivity.activity)
                                    .setTitle("Login Result")
                                    .setMessage(msg[0])
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            Toast.makeText(LoginActivity.activity, msg[0], Toast.LENGTH_SHORT).show();
                        }
                            if (ret[0] == 0) {

                                Intent mIntent = new Intent(getContext(), MainActivity.class);
                                getActivity().finishAffinity();
                                startActivity(mIntent);

                            } else if (ret[0] == 201) {
                                //user does not exist
                                Fragment fragment = new Signup();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.login_fragment_container, fragment)
                                        .commit();
                            } else if(ret[0] == 202) {
                                // wrong password
//                    Toast.makeText(getContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                            else {
//                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                    }
                });

            }
        };
        try{
            Thread t1 =new Thread(login); t1.start();
        }catch (Exception ex){
            System.out.println(ex);
        }
//
    }
}
