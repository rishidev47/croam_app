package com.example.croam;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordRecoveryActivity extends AppCompatActivity {


    LinearLayout bg;
    ProgressBar mProgressBar;
    private TextView otp;
    private TextView password;
    private TextView confirmPass;
    private TextView phone;
    private Button sendOtp;
    private Button submitOtp;
    private Button resetPass;
    private CoordinatorLayout root;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(
                PasswordRecoveryActivity.this,
                LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        otp = findViewById(R.id.editText_otp);
        password = findViewById(R.id.editText_pass);
        confirmPass = findViewById(R.id.editText_new_pass);
        phone = findViewById(R.id.editText_phone);
        sendOtp = findViewById(R.id.send_code);
        submitOtp = findViewById(R.id.submit_code);
        resetPass = findViewById(R.id.reset_pass);
        root = findViewById(R.id.root);
        bg = findViewById(R.id.bg);
        mProgressBar = findViewById(R.id.progress_circular);

        password.setEnabled(false);
        confirmPass.setEnabled(false);
        otp.setEnabled(false);
        submitOtp.setEnabled(false);
        resetPass.setEnabled(false);


        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().toString().length() < 10) {
                    phone.setError("Please enter a valid phone number!");
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    PasswordRecoveryActivity.this.getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    bg.setVisibility(View.VISIBLE);
                    Map<String, String> map = new HashMap<>();
                    String number = phone.getText().toString();
                    if (number.startsWith("91") || number.startsWith("+91")) {

                    } else {
                        number = "+91" + number;
                    }
                    map.put("number", number);
                    map.put("channel", "sms");
                    Call<ResponseBody> call = MyApi.Companion.invoke().forgotPass(map);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call,
                                Response<ResponseBody> response) {
                            mProgressBar.setVisibility(View.GONE);
                            bg.setVisibility(View.GONE);
                            PasswordRecoveryActivity.this.getWindow().clearFlags(
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        Log.e("response", response.body().toString());
                            if (response.code() == 200) {
                                phone.setEnabled(false);
                                sendOtp.setEnabled(false);
                                otp.setEnabled(true);
                                submitOtp.setEnabled(true);
                                otp.requestFocus();

                                final Snackbar snackbar = Snackbar.make(root,
                                        "Otp sent successfully. Please enter it!",
                                        Snackbar.LENGTH_LONG);
                                snackbar.setAction("Ok",
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                snackbar.dismiss();
                                            }
                                        });
                                snackbar.setActionTextColor(Color.RED);
                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar.make(root,
                                        "OOps, something went wrong.",
                                        Snackbar.LENGTH_LONG);
                                snackbar.setActionTextColor(Color.RED);
                                snackbar.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);
                            bg.setVisibility(View.GONE);
                            PasswordRecoveryActivity.this.getWindow().clearFlags(
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Snackbar snackbar = Snackbar.make(root,
                                    t.getMessage(),
                                    Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.RED);
                            snackbar.show();
                        }
                    });
                }

            }
        });

        submitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                PasswordRecoveryActivity.this.getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                bg.setVisibility(View.VISIBLE);
                Map<String, String> map = new HashMap<>();
                String number = phone.getText().toString();
                if (number.startsWith("91") || number.startsWith("+91")) {

                } else {
                    number = "+91" + number;
                }
                map.put("phonenumber", number);
                map.put("code", otp.getText().toString());
                Call<ResponseBody> call = MyApi.Companion.invoke().verifyOtp(map);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                            Response<ResponseBody> response) {
                        mProgressBar.setVisibility(View.GONE);
                        bg.setVisibility(View.GONE);
                        PasswordRecoveryActivity.this.getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Log.e("response", response.toString());
                        if (response.code() == 200) {
                            otp.setEnabled(false);
                            submitOtp.setEnabled(false);
                            password.setEnabled(true);
                            confirmPass.setEnabled(true);
                            resetPass.setEnabled(true);
                            password.requestFocus();
                            final Snackbar snackbar = Snackbar.make(root,
                                    "Otp verified successfully. Please enter the new password!",
                                    Snackbar.LENGTH_LONG);
                            snackbar.setAction("Ok",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            snackbar.dismiss();
                                        }
                                    });
                            snackbar.setActionTextColor(Color.RED);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);
                        bg.setVisibility(View.GONE);
                        PasswordRecoveryActivity.this.getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Snackbar snackbar = Snackbar.make(root,
                                t.getMessage(),
                                Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }
                });
            }
        });


        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!password.getText().toString().equals(confirmPass.getText().toString())) {
                    password.setError("Password and confirm password are not same!");
                    confirmPass.setError("Password and confirm password are not same!");
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    PasswordRecoveryActivity.this.getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    bg.setVisibility(View.VISIBLE);
                    String number = phone.getText().toString();
                    if (number.startsWith("91")) {
                        number = number.substring(2);
                    } else if (number.startsWith("+91")) {
                        number = number.substring(3);
                    }

                    Call<ResponseBody> call = MyApi.Companion.invoke().resetPass(number,
                            password.getText().toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call,
                                Response<ResponseBody> response) {
                            mProgressBar.setVisibility(View.GONE);
                            bg.setVisibility(View.GONE);
                            PasswordRecoveryActivity.this.getWindow().clearFlags(
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Log.e("response", response.toString());
                            if (response.code() == 200) {
                                password.setEnabled(false);
                                confirmPass.setEnabled(false);
                                resetPass.setEnabled(false);
                                final Snackbar snackbar = Snackbar.make(root,
                                        "Password Updated successfully!",
                                        Snackbar.LENGTH_LONG);
                                snackbar.setAction("Login",
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(
                                                        PasswordRecoveryActivity.this,
                                                        LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                snackbar.setActionTextColor(Color.RED);
                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar.make(root,
                                        "OOps, something went wrong.",
                                        Snackbar.LENGTH_LONG);
                                snackbar.setActionTextColor(Color.RED);
                                snackbar.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);
                            bg.setVisibility(View.GONE);
                            PasswordRecoveryActivity.this.getWindow().clearFlags(
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Snackbar snackbar = Snackbar.make(root,
                                    t.getMessage(),
                                    Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.RED);
                            snackbar.show();
                        }
                    });
                }

            }
        });

    }
}