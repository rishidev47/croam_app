package com.example.croam;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class Profile extends Fragment {
    private AlertDialog.Builder editDialog;
    private TextView nametxt;
    private TextView phonetxt;
    private TextView emailtxt;
    private TextView dobtxt;
    private View editbox;
//    DBHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
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
        dobtxt = view.findViewById(R.id.dob);

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
                if (editbox.getParent() != null) {
                    ((ViewGroup) editbox.getParent()).removeView(editbox); // <- fix
                }
                editDialog.show();
            }
        });


        editDialog = new AlertDialog.Builder(getActivity());
//        db=new DBHandler(getActivity().getApplicationContext());
        LayoutInflater inflater1 = (LayoutInflater) Objects.requireNonNull(
                getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        editbox = inflater1.inflate(R.layout.dialog_editprofile, null);
        String name;
        String phone;
        String email;
        String dob;
        //To be retrieved from database or server
//        String[] profile=db.getProfile();
//        name=profile[0];
//        phone=profile[1];
//        email=profile[2];
//        dob=profile[3];

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getContext().getApplicationContext()); //Get the preferences
        phone = prefs.getString("phone", null); //get a String
        name = prefs.getString("name", null); //get a String
        email = prefs.getString("email", null); //get a String
        int age = prefs.getInt("age", 0); //get a String
        dob = prefs.getString("dob", null); //get a String


        final EditText nameText = editbox.findViewById(R.id.editTextName);
        final EditText phoneText = editbox.findViewById(R.id.editTextPhone);
        final EditText emailText = editbox.findViewById(R.id.editTextEmail);
        final EditText dobText = editbox.findViewById(R.id.editTextDob);


        nameText.setText(name);
        phoneText.setText(phone);
        emailText.setText(email);
        dobText.setText(dob);


        editDialog.setView(editbox);
        editDialog.setTitle("Edit Details");
        editDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String n = nameText.getText().toString();
                        String p = phoneText.getText().toString();
                        String e = emailText.getText().toString();
                        String d = dobText.getText().toString();
                        update(n, e, p, d);
                        updateView();

                    }
                });

        editDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Update Cancelled", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
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
        dobtxt.setText(dob);

//        String[] details=db.getProfile();
//        if(details[0]!=null){
//            if(!details[0].equals("")){
////                nametxt.setText(details[0]);
////                phonetxt.setText(details[1]);
////                emailtxt.setText(details[2]);
////                dobtxt.setText(details[3]);
//            }
//
//        }
//

    }

    private void openFolder() {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(sdDir, "");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.withAppendedPath(Uri.fromFile(file), "/CRoam"), "image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        File sdDir = Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment
//        .DIRECTORY_PICTURES).getPath()
//                + "/Project1/");
//        intent.setDataAndType(uri, "image/jpeg");
////        startActivity(Intent.createChooser(intent, "Open folder"));
//        startActivity(intent);
    }
}
