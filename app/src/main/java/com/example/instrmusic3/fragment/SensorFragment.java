package com.example.instrmusic3.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.instrmusic3.FavoritesSelectedParameters;
import com.example.instrmusic3.HomePage;
import com.example.instrmusic3.dispatch.OscCommunication;
import com.example.instrmusic3.dispatch.OscConfiguration;
import com.example.instrmusic3.dispatch.OscHandler;
import com.example.instrmusic3.dispatch.SensorConfiguration;

import com.example.instrmusic3.R;
import com.example.instrmusic3.dispatch.Bundling;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.util.ArrayList;
import java.util.List;


public class SensorFragment extends Fragment {

    private SensorConfiguration sensorConfiguration = null;
    CompoundButton activeButton;
    String name;
    public SensorFragment() {
        super();
        this.sensorConfiguration = new SensorConfiguration();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        assert args != null;
        sensorConfiguration.setSensorType(args.getInt(Bundling.SENSOR_TYPE));
        sensorConfiguration.setOscParam(args.getString(Bundling.OSC_PREFIX));
        name = args.getString(Bundling.NAME);
        this.name = args.getString(Bundling.NAME);

        if (sensorConfiguration.getOscParam().equals("nfc")) {
            sensorConfiguration.setSendDuplicates(true);
        }
        View v = inflater.inflate(R.layout.sensor, null);
        TextView groupName = v.findViewById(R.id.group_name);
        groupName.setText(name);
        ((TextView) v.findViewById(R.id.osc_prefix)).setText("/" + args.getString(Bundling.OSC_PREFIX));

        activeButton = v.findViewById(R.id.active); // on and off sensor
        activeButton.setOnCheckedChangeListener((compoundButton, checked) -> {
            sensorConfiguration.setSend(checked);
            HomePage.setSensor(name);
        });
        return v;
    }

   /* public static void setSelected() {
        activeButton.setChecked(true);
        new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                sensorConfiguration.setSend(checked);
                HomePage.setSensor(name);
            }
        };
    }*/

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public SensorConfiguration getSensorConfiguration() {
        return sensorConfiguration;
    }
}
