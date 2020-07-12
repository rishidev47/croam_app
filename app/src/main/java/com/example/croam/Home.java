package com.example.croam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.Objects;

import androidx.fragment.app.Fragment;

public class Home extends Fragment {

    private Switch toggleSwitch;
    private ImageView circle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        toggleSwitch = view.findViewById(R.id.switch_main);
        circle = view.findViewById(R.id.large_circle);

        if (((MainActivity) getActivity()).isOn) {
            toggleSwitch.setChecked(true);
            toggleSwitch.setBackground(getResources().getDrawable(R.drawable.circle_green));
            circle.setImageResource(R.drawable.cricle_green_large);
        } else {
            toggleSwitch.setChecked(false);
            toggleSwitch.setBackground(getResources().getDrawable(R.drawable.circle_red));
            circle.setImageResource(R.drawable.cricle_red_large);
        }
        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((MainActivity) Objects.requireNonNull(getActivity())).onSwitchOn();
                    if (((MainActivity) getActivity()).isOn) {
//                        view.setBackgroundColor(getResources().getColor(R.color.light_green));
                        toggleSwitch.setBackground(getResources().getDrawable(R.drawable
                                .circle_green));
                        circle.setImageResource(R.drawable.cricle_green_large);
                    } else {
                        toggleSwitch.setChecked(false);
                        circle.setImageResource(R.drawable.cricle_green_large);
                    }

                } else {
                    ((MainActivity) Objects.requireNonNull(getActivity())).onSwitchOff();
                    if (!((MainActivity) getActivity()).isOn) {
//                        view.setBackgroundColor(getResources().getColor(R.color.white));
                        toggleSwitch.setBackground(getResources().getDrawable(R.drawable
                                .circle_red));
                        circle.setImageResource(R.drawable.cricle_red_large);
                    }

                }
            }
        });

        return view;
    }

}
