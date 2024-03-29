package com.example.instrmusic3.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableMap;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.instrmusic3.Effects.EffectsModel;
import com.example.instrmusic3.HomePage;
import com.example.instrmusic3.R;
import com.example.instrmusic3.auth.Login;
import com.example.instrmusic3.dispatch.Bundling;
import com.example.instrmusic3.dispatch.OscCommunication;
import com.example.instrmusic3.dispatch.OscConfiguration;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.util.ArrayList;
import java.util.List;

public class EffectsFragment extends Fragment {
    String name;
    private final Handler handler = new Handler();
    int onOff = 0;

    public EffectsFragment() {
        super();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HomePage startUpActivity = (HomePage) getActivity();
        startUpActivity.getSettings();
        startUpActivity.loadSettings();
        Bundle args = this.getArguments();
        assert args != null;
        name = args.getString(Bundling.EFFECT_NAME);
        this.name = args.getString(Bundling.EFFECT_NAME);

        this.name = args.getString(Bundling.EFFECT_NAME);
        View v = inflater.inflate(R.layout.effects, null);
        TextView groupName = v.findViewById(R.id.group_name);
        setName(this.name);

        groupName.setText(name);
        CompoundButton button = v.findViewById(R.id.active);

        EffectsModel model = new ViewModelProvider(getActivity()).get(EffectsModel.class);
        model.getEffectState().addOnMapChangedCallback(
                new ObservableMap.OnMapChangedCallback<ObservableMap<String, Boolean>, String, Boolean>() {
                    @Override
                    public void onMapChanged(ObservableMap<String, Boolean> sender, String key) {
                        if (key.equals(name)) {
                            Boolean state = sender.get(name);
                            if (state != null && state.booleanValue() == true) {
                                button.setChecked(true);

                                OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
                                communication.start();
                                communication.getOscHandler();
                                OscConfiguration oscConfiguration = OscConfiguration.getInstance();
                                List<Object> args1 = new ArrayList<>(1);
                                HomePage.setEffect(name);
                                args1.add(name);
                                OSCPortOut sender1 = oscConfiguration.getOscPort();
                                String IP = HomePage.getLocalIpAddress();
                                OSCMessage msg1 = new OSCMessage("/effect" + IP, args1);
                                try {
                                    sender1.send(msg1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                button.setChecked(false);

                            }
                        }
                    }
                });


        button.setOnClickListener(v1 -> {
            if (onOff == 0) {
                onOff = 1;
                OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
                communication.start();
                communication.getOscHandler();
                OscConfiguration oscConfiguration = OscConfiguration.getInstance();
                List<Object> args1 = new ArrayList<>(1);
                HomePage.setEffect(name);
                args1.add(name);
                OSCPortOut sender = oscConfiguration.getOscPort();
                String IP = HomePage.getLocalIpAddress();

                OSCMessage msg1 = new OSCMessage("/effect" + IP, args1);
                try {
                    sender.send(msg1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                onOff = 0;
            }
        });
        return v;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void setName(String name1) {
        this.name = name1;

    }

    public String getName() {
        return this.name;
    }
}
