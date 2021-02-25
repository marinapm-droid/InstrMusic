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

import com.example.instrmusic3.dispatch.SensorConfiguration;

import com.example.instrmusic3.R;
import com.example.instrmusic3.dispatch.Bundling;


public class SensorFragment extends Fragment {

	private final SensorConfiguration sensorConfiguration;

	public SensorFragment() {
		super();
		this.sensorConfiguration = new SensorConfiguration();
	}

	@SuppressLint("SetTextI18n")
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = this.getArguments();
		assert args != null;
		this.sensorConfiguration.setSensorType(args.getInt(Bundling.SENSOR_TYPE));
		System.out.println("TIPO SENSOR:");
		this.sensorConfiguration.setOscParam(args.getString(Bundling.OSC_PREFIX));
		String name = args.getString(Bundling.NAME);

		if (sensorConfiguration.getOscParam().equals("nfc")) {
			this.sensorConfiguration.setSendDuplicates(true);
		}
		View v = inflater.inflate(R.layout.sensor, null);
		TextView groupName = v.findViewById(R.id.group_name);
		groupName.setText(name);
		((TextView) v.findViewById(R.id.osc_prefix)).setText("/" + args.getString(Bundling.OSC_PREFIX));

		CompoundButton activeButton = v.findViewById(R.id.active); // on and off sensor
		activeButton.setOnCheckedChangeListener((compoundButton, checked) -> sensorConfiguration.setSend(checked));
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
