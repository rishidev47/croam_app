package com.example.croam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class Home extends Fragment {
    Switch toggleSwitch;
    Button btn_update_threshold;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_home, container, false);
        final BottomNavigationView navView=((MainActivity)getActivity()).navView;
        final EditText txt_threshold = view.findViewById(R.id.editText_threshold);
        btn_update_threshold=view.findViewById(R.id.btn_update_threshold);
        toggleSwitch=view.findViewById(R.id.switch_main);

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
                    ((MainActivity)getActivity()).onSwitchOn();
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
                    ((MainActivity)getActivity()).onSwitchOff();
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
                double threshold=Float.parseFloat(txt_threshold.getText().toString());
                if(threshold<=1 && threshold>=0){
                    ((MainActivity)getActivity()).threshold=threshold;
                }
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
