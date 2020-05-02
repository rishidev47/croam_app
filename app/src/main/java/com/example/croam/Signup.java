package com.example.croam;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static com.example.croam.LoginActivity.croam_server_url;

public class Signup extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_signup, container, false);
        Button signup= view.findViewById(R.id.btn_signup);
        final RadioGroup radioGroupGender=view.findViewById(R.id.radioGroupGender);
        final EditText name= view.findViewById(R.id.editText_signup_name);
        final EditText phone= view.findViewById(R.id.editText_signup_phone);
        final EditText password= view.findViewById(R.id.editText_signup_password);
        final EditText age= view.findViewById(R.id.editText_signup_age);

        TextView goToLogin=view.findViewById(R.id.text_login);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Login();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login_fragment_container, fragment)
                        .commit();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_name=name.getText().toString();
                String input_phone=phone.getText().toString();
                String input_pswd=password.getText().toString();
                String input_age=age.getText().toString();
                int selectedId = radioGroupGender.getCheckedRadioButtonId();
                String input_gender="0";

                RadioButton radioGenderButton=radioGroupGender.findViewById(selectedId);
                if(radioGenderButton==null){
                    input_gender="0";
                }else if(radioGenderButton.getText().equals("Male")){
                    input_gender="2";

                }else if(radioGenderButton.getText().equals("Other")){
                    input_gender="3";

                }else if(radioGenderButton.getText().equals("Female")){
                    input_gender="1";

                }
                // Gender 0:Not specified 1:Female 2: Male 3:Other


                registerUser(input_name, input_gender, input_age, input_phone, input_pswd);


            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    void registerUser(final String name, final String gender, final String age, final String phone, final String pswd){
        Log.v("REGISTER", phone+" "+pswd);
        final String msg[]={"Error"};
        final Integer ret[]={-1};
        final boolean status[]={false};
        Runnable register=new Runnable() {
            @Override
            public void run() {
                try {
                    String charset = "UTF-8";
                    String requestURL = croam_server_url + "/register";
                    MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                    multipart.addFormField("phone", phone);
                    multipart.addFormField("password", pswd);
                    multipart.addFormField("name",name);
                    multipart.addFormField("gender",gender);
                    multipart.addFormField("age",age);
                    multipart.addFormField("dummy",null);
                    Log.v("REGISTER", name+gender+age+phone+pswd);
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

                    }catch (Exception ex){
                        System.out.println("JSON Error "+ex);
                    }

                } catch (Exception e) {
                    msg[0]=" Connection Error:\n Check Your Internet Connection ";
                    System.out.println("Error in signup" + e);
                }
//                if(getActivity()==null)return;
                LoginActivity.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(LoginActivity.activity)
                                .setTitle("Signup Result")
                                .setMessage(msg[0])
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        Toast.makeText(LoginActivity.activity, msg[0], Toast.LENGTH_SHORT).show();

                        if(ret[0]==0){
                            Fragment fragment = new Login();
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.login_fragment_container, fragment)
                                    .commit();
                        }
                        else if(ret[0]==101){
                            Fragment fragment = new Login();
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.login_fragment_container, fragment)
                                    .commit();
                        }
                        else{
//                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        };
        try{
            Thread t1 =new Thread(register); t1.start();
        }catch (Exception ex){
            System.out.println(ex);
        }

    }
}
