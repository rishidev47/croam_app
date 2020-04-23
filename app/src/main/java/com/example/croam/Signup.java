package com.example.croam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class Signup extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_signup, container, false);
        Button login= view.findViewById(R.id.btn_signup);
        final RadioGroup radioGroupGender=view.findViewById(R.id.radioGroupGender);
        final RadioButton radioGenderButton;
        final EditText name= view.findViewById(R.id.editText_signup_name);
        final EditText phone= view.findViewById(R.id.editText_signup_phone);
        final EditText password= view.findViewById(R.id.editText_signup_password);

        TextView goToLogin=view.findViewById(R.id.text_login);
        goToLogin.setOnClickListener(new View.OnClickListener() {
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
                String input_name=name.getText().toString();
                String input_phone=phone.getText().toString();
                String input_pswd=password.getText().toString();
                int selectedId = radioGroupGender.getCheckedRadioButtonId();
                String gender = selectedId==0? "Female":"Male";

                int regResult=registerUser(input_name, gender, input_phone, input_pswd);


                if(regResult==0){
                    Toast.makeText(getContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new Login();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.login_fragment_container, fragment)
                            .commit();
                }
                else if(regResult==1){

                }

            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    int registerUser(String name, String gender, String phone, String pswd){
        String url="https://enil4oe9ib22.x.pipedream.net/createUser";
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("id", "3");
            jsonObject.put("name", name);
            jsonObject.put("phone", phone);
            jsonObject.put("password", pswd);
            jsonObject.put("dob", "5/5/1993");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest=new JsonObjectRequest(
                Request.Method.POST,
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
