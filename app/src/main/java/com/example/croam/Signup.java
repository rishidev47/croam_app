package com.example.croam;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends Fragment {

    CoordinatorLayout mCoordinatorLayout;
    LinearLayout bg;
    ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_signup, container, false);
        Button signup = view.findViewById(R.id.btn_signup);
        final RadioGroup radioGroupGender = view.findViewById(R.id.radioGroupGender);
        final EditText name = view.findViewById(R.id.editText_signup_name);
        final EditText phone = view.findViewById(R.id.editText_signup_phone);
        final EditText password = view.findViewById(R.id.editText_signup_password);
        final EditText age = view.findViewById(R.id.editText_signup_age);
        final EditText email = view.findViewById(R.id.input_email);
        bg = view.findViewById(R.id.bg);
        mProgressBar = view.findViewById(R.id.progress_circular);
        mCoordinatorLayout = view.findViewById(R.id.roots);

        TextView goToLogin = view.findViewById(R.id.text_login);
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
                String input_name = name.getText().toString();
                String input_phone = phone.getText().toString();
                String input_pswd = password.getText().toString();
                String input_age = age.getText().toString();
                String input_email = email.getText().toString();
                int selectedId = radioGroupGender.getCheckedRadioButtonId();
                String input_gender = "0";

                RadioButton radioGenderButton = radioGroupGender.findViewById(selectedId);
                if (radioGenderButton == null) {
                    input_gender = "0";
                } else if (radioGenderButton.getText().equals("Male")) {
                    input_gender = "2";

                } else if (radioGenderButton.getText().equals("Other")) {
                    input_gender = "3";

                } else if (radioGenderButton.getText().equals("Female")) {
                    input_gender = "1";

                }
                // Gender 0:Not specified 1:Female 2: Male 3:Other
                boolean flag = true;
                if (input_name.equals("")) {
                    name.setError("Please enter name!");
                    flag = false;
                }
                if (input_phone.equals("")) {
                    phone.setError("Please enter phone number!");
                    flag = false;
                }
                if (input_pswd.equals("")) {
                    password.setError("Please enter password!");
                    flag = false;
                }
                if (input_age.equals("")) {
                    age.setError("Please enter age!");
                    flag = false;
                }

                if (flag) {
                    registerUser(input_name, input_gender, input_age, input_phone, input_pswd,
                            input_email);
                }


            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void registerUser(final String name, final String gender, final String age,
            final String phone, final String pswd, final String email) {
        mProgressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        bg.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = MyApi.Companion.invoke().signUp(phone, name, pswd, email, age,
                gender);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                    Response<ResponseBody> response) {
                mProgressBar.setVisibility(View.GONE);
                bg.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                JSONObject jsonObject = null;
                try {
//                    Log.e("signup", response.body().string());
                    jsonObject = new JSONObject(response.body().string());
                    Log.e("abc", jsonObject.toString());
                    if(jsonObject.has("status")){
                        Toast.makeText(getContext(), jsonObject.getString("status") , Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(), jsonObject.getString("error") , Toast.LENGTH_LONG).show();
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                Fragment fragment = new Login();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login_fragment_container, fragment)
                        .commit();
            }

            @Override
            public void onFailure(final Call<ResponseBody> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                bg.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.e("resp", t.getMessage());
                Snackbar snackbar = Snackbar.make(mCoordinatorLayout, t.getMessage(),
                        Snackbar.LENGTH_LONG).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        registerUser(name, gender, age, phone, pswd, email);
                    }
                });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }
        });

    }
}
