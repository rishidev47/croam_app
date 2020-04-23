package com.example.croam;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_login, container, false);
        Button login= view.findViewById(R.id.btn_login);
        final EditText phone= view.findViewById(R.id.editText_login_phone);
        final EditText password= view.findViewById(R.id.editText_login_password);

        TextView goToSignup=view.findViewById(R.id.text_login);
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
                String input_phone=phone.getText().toString();
                String input_pswd=password.getText().toString();

                int loginStatus=doLogin(input_phone,input_pswd);

                if(loginStatus==0){
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext()); //Get the preferences
                    SharedPreferences.Editor edit = prefs.edit(); //Needed to edit the preferences

                    edit.putString("phone", input_phone);  //add a String
                    edit.putString("pswd",input_pswd);
                    edit.putBoolean("isLoggedin", true); //add a boolean
                    Log.d("Prefs login", "onClick: "+prefs.toString());
                    edit.commit();  // save the edits.
                    Toast.makeText(getContext(), "User Loggedin Successfully", Toast.LENGTH_SHORT).show();
                    Intent mIntent = new Intent(getContext(),MainActivity.class);
                    getActivity().finishAffinity();
                    startActivity(mIntent);
                }
                else if(loginStatus==1){
                    //user does not exist
                    Toast.makeText(getContext(), "User Not Present", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new Signup();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.login_fragment_container, fragment)
                            .commit();
                }
                else{
                    // wrong password
                    Toast.makeText(getContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    int doLogin(String phone, String pswd){
        String url="http://192.168.43.68:8089/login/";
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("id", "3");
            jsonObject.put("phone", phone);
            jsonObject.put("password", pswd);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JsonObjectRequest objectRequest=new JsonObjectRequest(
                Request.Method.GET,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("REST response", "onResponse: "+response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("REST response", "onErrorResponse: "+error.toString());

                    }
                }

        );
        requestQueue.add(objectRequest);
        return 0;
    }
}

