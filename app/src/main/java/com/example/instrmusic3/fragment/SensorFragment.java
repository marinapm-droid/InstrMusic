package com.example.instrmusic3.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableMap;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.instrmusic3.activities.HomePage;
import com.example.instrmusic3.sensors.SensorsModel;
import com.example.instrmusic3.dispatch.SensorConfiguration;

import com.example.instrmusic3.R;
import com.example.instrmusic3.dispatch.Bundling;


public class SensorFragment extends Fragment {

    private SensorConfiguration sensorConfiguration = null;
    String name;
    CompoundButton activeButton;

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


        CompoundButton activeButton = v.findViewById(R.id.active); // on and off sensor
        SensorsModel model = new ViewModelProvider(getActivity()).get(SensorsModel.class);
        model.getSensorState().addOnMapChangedCallback(
                new ObservableMap.OnMapChangedCallback<ObservableMap<String, Boolean>, String, Boolean>() {
                    @Override
                    public void onMapChanged(ObservableMap<String, Boolean> sender, String key) {

                        if (key.equals(name)) {
                            Boolean state = sender.get(name);
                            if (state != null && state.booleanValue() == true) {
                                activeButton.setChecked(true);
                                sensorConfiguration.setSend(true);
                                HomePage.setSensor(name);
                            } else {
                                activeButton.setChecked(false);
                                sensorConfiguration.setSend(false);

                            }
                        }
                    }
                });


        activeButton.setOnCheckedChangeListener((compoundButton, checked) -> {
            sensorConfiguration.setSend(checked);
            HomePage.setSensor(name);
        });
        return v;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public SensorConfiguration getSensorConfiguration() {
        return sensorConfiguration;
    }
}
