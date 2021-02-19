package com.example.instrmusic3.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.instrmusic3.R;
import com.example.instrmusic3.dispatch.Bundling;


public class HelpSensorFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = this.getArguments();

        assert args != null;
        String name = args.getString(Bundling.NAME);
        String sensorName = args.getString(Bundling.SENSOR_NAME);
        View v = inflater.inflate(R.layout.help_sensor, null);
        TextView groupName = (TextView) v.findViewById(R.id.group_name);
        groupName.setText(name + " (" + sensorName + ")");
        AddText((TextView) v.findViewById(R.id.osc_prefix), args.getString(Bundling.OSC_PREFIX));
        AddText((TextView) v.findViewById(R.id.dimensions), args.getInt(Bundling.DIMENSIONS));
        AddText((TextView) v.findViewById(R.id.range), args.getFloat(Bundling.SENSOR_RANGE));
        AddText((TextView) v.findViewById(R.id.resolution), args.getFloat(Bundling.RESOLUTION));
        return v;
    }

    private void AddText(TextView view, float val) {
        view.setText(view.getText() + ": " + val);
    }

    private void AddText(TextView view, int val) {
        view.setText(view.getText() + ": " + val);
    }

    private void AddText(TextView view, String val) {
        view.setText(view.getText() + ": " + val);
    }
}