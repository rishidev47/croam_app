package com.example.croam;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

public class Home extends Fragment {
    private Switch toggleSwitch;
    private Button btn_update_threshold;

    private String currentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 1;

    static final int REQUEST_VIDEO_CAPTURE = 1;

//    VideoView videoView;

    Button video;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 0);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,20);
//        takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,20);
        if (takeVideoIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getContext()),
                        "com.example.croam.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_home, container, false);
        final BottomNavigationView navView=((MainActivity) Objects.requireNonNull(getActivity())).navView;
        final EditText txt_threshold = view.findViewById(R.id.editText_threshold);
        btn_update_threshold=view.findViewById(R.id.btn_update_threshold);
        toggleSwitch=view.findViewById(R.id.switch_main);
//        videoView = view.findViewById(R.id.videoView);
        video=view.findViewById(R.id.video);

        Button captureButton = view.findViewById(R.id.capture);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });

        if(((MainActivity)getActivity()).isOn){
            navView.setBackgroundColor(getResources().getColor(R.color.light_green));
            view.setBackgroundColor(getResources().getColor(R.color.light_green));
            toggleSwitch.setChecked(true);
            toggleSwitch.setBackground(getResources().getDrawable(R.drawable.circle_green));
        }
        else {
            navView.setBackgroundColor(getResources().getColor(R.color.light_red));
            view.setBackgroundColor(getResources().getColor(R.color.light_red));
            toggleSwitch.setChecked(false);
            toggleSwitch.setBackground(getResources().getDrawable(R.drawable.circle_red));
        }
        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ((MainActivity) Objects.requireNonNull(getActivity())).onSwitchOn();
                    if(((MainActivity) getActivity()).isOn){
                        navView.setBackgroundColor(getResources().getColor(R.color.light_green));
                        view.setBackgroundColor(getResources().getColor(R.color.light_green));
                        toggleSwitch.setBackground(getResources().getDrawable(R.drawable.circle_green));
                    }
                    else{
                        toggleSwitch.setChecked(false);
                    }

                }
                else{
                    ((MainActivity) Objects.requireNonNull(getActivity())).onSwitchOff();
                    if(!((MainActivity) getActivity()).isOn){
                        navView.setBackgroundColor(getResources().getColor(R.color.light_red));
                        view.setBackgroundColor(getResources().getColor(R.color.light_red));
                        toggleSwitch.setBackground(getResources().getDrawable(R.drawable.circle_red));
                    }

                }
            }
        });

        btn_update_threshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thd=txt_threshold.getText().toString();

                if(!thd.equals("")){
                    double threshold=Float.parseFloat(txt_threshold.getText().toString());
                    if(threshold<=1 && threshold>=0){
                        if(((MainActivity) Objects.requireNonNull(getActivity())).isOn){
                            ((MainActivity)getActivity()).onSwitchOff();
                            ((MainActivity)getActivity()).threshold=threshold;
                            ((MainActivity)getActivity()).onSwitchOn();
                        }else{
                            ((MainActivity)getActivity()).threshold=threshold;
                        }

                        Toast.makeText(getActivity(), "Threshold set to: "+threshold, Toast.LENGTH_SHORT).show();
                    }else{
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Setting Threshold")
                                .setMessage("Enter a value only between 0 to 1")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        Toast.makeText(getActivity(), "Could not set threshold value ", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
//            videoView.setVideoURI(videoUri);
        }
    }

}
