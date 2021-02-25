package com.example.instrmusic3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.instrmusic3.R;

import org.sensors2.common.sensors.Parameters;
import com.example.instrmusic3.activities.StartUpActivity;
import com.example.instrmusic3.dispatch.Bundling;

import java.util.Objects;


public class StartupFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_start_up, container, false);

        CompoundButton activeButton = v.findViewById(R.id.active);
        StartUpActivity activity = (StartUpActivity) getActivity();
        activeButton.setOnCheckedChangeListener(activity);
        assert activity != null;
        for (Parameters parameters : activity.getSensors()) {
            createSensorFragments((com.example.instrmusic3.sensors.Parameters) parameters);
        }

        return v;
    }

    public void createSensorFragments(com.example.instrmusic3.sensors.Parameters parameters) {
        FragmentManager manager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        SensorFragment groupFragment = (SensorFragment) manager.findFragmentByTag(parameters.getName());

        if (groupFragment == null) {
            groupFragment = createFragment(parameters, manager);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.sensor_group, groupFragment, parameters.getName());
            transaction.commit();
        }
        addSensorToDispatcher(groupFragment);
    }

    private void addSensorToDispatcher(SensorFragment groupFragment) {
        StartUpActivity activity = (StartUpActivity) this.getActivity();
        activity.addSensorFragment(groupFragment);
    }

    public SensorFragment createFragment(com.example.instrmusic3.sensors.Parameters parameters, FragmentManager manager) {
        SensorFragment groupFragment = new SensorFragment();
        Bundle args = new Bundle();
        args.putInt(Bundling.DIMENSIONS, parameters.getDimensions());
        args.putInt(Bundling.SENSOR_TYPE, parameters.getSensorType());
        args.putString(Bundling.OSC_PREFIX, parameters.getOscPrefix());
        args.putString(Bundling.NAME, parameters.getName());
        System.out.println("args2:" + args);
        groupFragment.setArguments(args);

        return groupFragment;
    }

}
