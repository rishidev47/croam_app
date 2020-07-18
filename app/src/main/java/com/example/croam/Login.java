package com.example.croam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Fragment {

    CoordinatorLayout mCoordinatorLayout;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    ProgressBar mProgressBar;
    LinearLayout bg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(
                getContext().getApplicationContext()); //Get the preferences

        edit = prefs.edit(); //Needed to edit the preference

        String access_token = prefs.getString("access_token", null);
        if (access_token != null && !access_token.equals("")) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
        mProgressBar = view.findViewById(R.id.progress_circular);
        bg = view.findViewById(R.id.bg);
        Button login = view.findViewById(R.id.btn_login);
        final EditText phone = view.findViewById(R.id.editText_login_phone);
        final EditText password = view.findViewById(R.id.editText_login_password);
        mCoordinatorLayout = view.findViewById(R.id.rootl);

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

        TextView forgotPass = view.findViewById(R.id.text_forgot_pass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PasswordRecoveryActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_phone = phone.getText().toString();
                String input_pswd = password.getText().toString();

                boolean flag = true;
                if (input_phone.equals("")) {
                    phone.setError("Please enter phone number!");
                    flag = false;
                }
                if (input_pswd.equals("")) {
                    password.setError("Please enter password");
                    flag = false;
                }
                if (flag) {
                    doLogin(input_phone, input_pswd);
                }
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void doLogin(final String phone, final String pswd) {
        mProgressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        bg.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = MyApi.Companion.invoke().logIn(phone, pswd);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                    Response<ResponseBody> response) {
                mProgressBar.setVisibility(View.GONE);
                bg.setVisibility(View.GONE);
                Objects.requireNonNull(getActivity()).getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                String body = null;
                try {
                    body = response.body().string();
                    Log.e("body", body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("resp", response.toString());

                try {

                    JSONObject obj = new JSONObject(body);

                    Log.e("My App", obj.toString());
                    Log.e("token", obj.getString("accesstoken"));
                    JSONObject user = obj.getJSONObject("user");
                    Log.e("name", user.getString("name"));
                    edit.putString("access_token", obj.getString("accesstoken"));
                    edit.putString("name", user.getString("name"));
                    edit.putString("email", user.getString("email"));
                    edit.putString("phone", user.getString("number"));
                    edit.putString("gender", user.getString("gender"));
//                    edit.putString("dob", dob);
//                    edit.putString("access_token",token);
                    edit.commit();
                    Intent intent = new Intent(getActivity(), PersonalizationActivity.class);
                    startActivity(intent);

                } catch (Throwable t) {
//                    Log.e("My App", t.getMessage());
                    try {
                        JSONObject obj = new JSONObject(body);
                        Snackbar snackbar = Snackbar.make(mCoordinatorLayout,
                                obj.getString("status"),
                                Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, t.getMessage(),
                                Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                bg.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.e("resp", t.getMessage());
                Snackbar snackbar = Snackbar.make(mCoordinatorLayout, t.getMessage(),
                        Snackbar.LENGTH_LONG).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doLogin(phone, pswd);
                    }
                });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }
        });


    }
}
