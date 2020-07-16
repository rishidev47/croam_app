package com.example.croam;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends Fragment {
    private TextView nametxt;
    private TextView phonetxt;
    private TextView emailtxt;
    //    private TextView dobtxt;
    private Dialog editDialog;
    private String activeTill;
//    DBHandler db;

    private boolean isPremium = true;
    private TextInputLayout membership;
    private TextInputLayout till;
    private ProgressBar bar;
    private ProgressBar bar2;
    private CardView premium;
    private TextView status;
    private TextView date;
    private MaterialTextView premiumButon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        premium = view.findViewById(R.id.premium);
        status = view.findViewById(R.id.status);
        premiumButon = view.findViewById(R.id.button_premium);
        bar2 = view.findViewById(R.id.progress_circular2);
        date = view.findViewById(R.id.premium_date);
        membership = view.findViewById(R.id.status_layout);
        till = view.findViewById(R.id.premium_layout);
        membership.setVisibility(View.GONE);
        till.setVisibility(View.GONE);
        bar = view.findViewById(R.id.progress_circular);
        bar.setVisibility(View.VISIBLE);
        bar2.setVisibility(View.VISIBLE);
        premium.setVisibility(View.GONE);
        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PaymentActivity.class);
                startActivity(intent);

            }
        });
        premium.setEnabled(false);
        getPremiumStatus();

        Button logout = view.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                        Objects.requireNonNull(
                                getContext()).getApplicationContext()); //Get the preferences
                prefs.edit().clear().commit();
                ((MainActivity) Objects.requireNonNull(getActivity())).stopCroamService();
                Intent mIntent = new Intent(getContext(), LoginActivity.class);
                getActivity().finishAffinity();
                startActivity(mIntent);
            }
        });
//        if(((MainActivity)getActivity()).isOn){
//            view.setBackgroundColor(getResources().getColor(R.color.light_green));
//        }
//        else {
//            view.setBackgroundColor(getResources().getColor(R.color.light_red));
//        }
        nametxt = view.findViewById(R.id.name);
        phonetxt = view.findViewById(R.id.phone);
        emailtxt = view.findViewById(R.id.email);
//        dobtxt = view.findViewById(R.id.dob);

        updateView();

        final FloatingActionButton editButton = view.findViewById(R.id.editButton);
        final TextView openFolder = view.findViewById(R.id.openFolder);

        openFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFolder();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        db=new DBHandler(getActivity().getApplicationContext());
    }

    private void update(String name, String email, String phone, String dob) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getContext().getApplicationContext()); //Get the preferences
        SharedPreferences.Editor edit = prefs.edit(); //Needed to edit the preference
        edit.putString("name", name);
        edit.putString("email", email);
        edit.putString("phone", phone);
        edit.putString("dob", dob);
        edit.commit();
//        db.updateProfile(name, email, phone, dob);
//        doUpdate();
    }

    //    void doUpdate(){
//
//    }
    private void updateView() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getContext().getApplicationContext()); //Get the preferences
        String phone = prefs.getString("phone", null); //get a String
        String name = prefs.getString("name", null); //get a String
        String email = prefs.getString("email", null); //get a String
        int age = prefs.getInt("age", 0); //get a String
        String dob = prefs.getString("dob", null); //get a String
        nametxt.setText(name);
        phonetxt.setText(phone);
        emailtxt.setText(email);
//        dobtxt.setText(dob);

    }

    private void openFolder() {

        Intent intent = new Intent(getContext(), ViewImagesActivity.class);
        startActivity(intent);

    }

    private void dialog() {
        editDialog = new Dialog(getContext());
        editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editDialog.setContentView(R.layout.dialog_editprofile);
        editDialog.setTitle("Edit Profile");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getContext().getApplicationContext()); //Get the preferences
        String phone = prefs.getString("phone", null); //get a String
        String name = prefs.getString("name", null); //get a String
        String email = prefs.getString("email", null); //get a String
        int age = prefs.getInt("age", 0); //get a String
        String dob = prefs.getString("dob", null); //get a String


        final EditText nameText = editDialog.findViewById(R.id.editTextName);
        final EditText phoneText = editDialog.findViewById(R.id.editTextPhone);
        final EditText emailText = editDialog.findViewById(R.id.editTextEmail);
        final EditText dobText = editDialog.findViewById(R.id.editTextDob);


        nameText.setText(name);
        phoneText.setText(phone);
        emailText.setText(email);
        dobText.setText(dob);

        Button saveButton = editDialog.findViewById(R.id.save_button);
        Button cancelButton = editDialog.findViewById(R.id.cancel_button);
        // if button is clicked, close the custom dialog
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = nameText.getText().toString();
                String p = phoneText.getText().toString();
                String e = emailText.getText().toString();
                String d = dobText.getText().toString();
                update(n, e, p, d);
                updateView();
                editDialog.cancel();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Update Cancelled", Toast.LENGTH_SHORT).show();
                editDialog.cancel();
            }
        });

        editDialog.show();
    }


    void getPremiumStatus() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getContext().getApplicationContext());
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        Call<ResponseBody> call = MyApi.Companion.invoke().isPremium(
                Objects.requireNonNull(prefs.getString("phone", null)));
        Objects.requireNonNull(call).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                    Response<ResponseBody> response) {
                Log.e("profile", response.toString());
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    activeTill = jsonObject.getString("active_till");
                    Calendar c = Calendar.getInstance();
                    Date currentTime = c.getTime();
                    c.setTime(currentTime);
                    c.add(Calendar.DATE, 365);
                    String output = sdf.format(c.getTime());
                    Log.e("profile", output);
                    try {
                        Date d = sdf.parse(activeTill);
                        if (activeTill.equals("null") || (new Date().after(d))) {
                            isPremium = false;
                            Log.e("profile", "notprem");
                        }
                    } catch (ParseException ex) {
                        Log.v("Exception", ex.getLocalizedMessage());
                        isPremium = false;
                        Log.e("profile", "notprem");
                    }
                    bar.setVisibility(View.GONE);
                    membership.setVisibility(View.VISIBLE);
                    till.setVisibility(View.VISIBLE);
                    bar2.setVisibility(View.GONE);
                    premium.setVisibility(View.VISIBLE);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                if (!isPremium) {
                    premium.setEnabled(true);
                }else{
                    DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext().getApplicationContext());
                    status.setText("PREMIUM");
                    try {
                        date.setText(dateFormat.format(sdf.parse(activeTill)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    premiumButon.setText("Already Premium Member!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getPremiumStatus();
            }
        });
    }
}
