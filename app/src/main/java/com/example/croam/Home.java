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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment {
    private Switch toggleSwitch;
    private Button btn_update_threshold;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_home, container, false);
//        final EditText txt_threshold = view.findViewById(R.id.editText_threshold);
//        btn_update_threshold=view.findViewById(R.id.btn_update_threshold);
        toggleSwitch=view.findViewById(R.id.switch_main);
//        videoView = view.findViewById(R.id.videoView);

        if(((MainActivity)getActivity()).isOn){
//            view.setBackgroundColor(getResources().getColor(R.color.light_green));
            toggleSwitch.setChecked(true);
            toggleSwitch.setBackground(getResources().getDrawable(R.drawable.circle_green));
        }
        else {
//            view.setBackgroundColor(getResources().getColor(R.color.light_red));
            toggleSwitch.setChecked(false);
            toggleSwitch.setBackground(getResources().getDrawable(R.drawable.circle_red));
        }
        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ((MainActivity) Objects.requireNonNull(getActivity())).onSwitchOn();
                    if(((MainActivity) getActivity()).isOn){
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
                        view.setBackgroundColor(getResources().getColor(R.color.white));
                        toggleSwitch.setBackground(getResources().getDrawable(R.drawable.circle_red));
                    }

                }
            }
        });

//        btn_update_threshold.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String thd=txt_threshold.getText().toString();
//
//                if(!thd.equals("")){
//                    double threshold=Float.parseFloat(txt_threshold.getText().toString());
//                    if(threshold<=1 && threshold>=0){
//                        if(((MainActivity) Objects.requireNonNull(getActivity())).isOn){
//                            ((MainActivity)getActivity()).onSwitchOff();
//                            ((MainActivity)getActivity()).threshold=threshold;
//                            ((MainActivity)getActivity()).onSwitchOn();
//                        }else{
//                            ((MainActivity)getActivity()).threshold=threshold;
//                        }
//
//                        Toast.makeText(getActivity(), "Threshold set to: "+threshold, Toast.LENGTH_SHORT).show();
//                    }else{
//                        new AlertDialog.Builder(getActivity())
//                                .setTitle("Setting Threshold")
//                                .setMessage("Enter a value only between 0 to 1")
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                    }
//                                })
//                                .setNegativeButton(android.R.string.no, null)
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
//                        Toast.makeText(getActivity(), "Could not set threshold value ", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }
//        });










        //Use score[] float array and display it on home fragment












































        return view;
    }

}
