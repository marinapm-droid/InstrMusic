package com.example.instrmusic3.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.instrmusic3.R;
import com.example.instrmusic3.activities.EffectActivity;
import com.example.instrmusic3.activities.StartUpActivity;
import com.example.instrmusic3.activities.StartUpEffectActivity;
import com.example.instrmusic3.dispatch.Bundling;
import com.example.instrmusic3.dispatch.EffectConfiguration;
import com.example.instrmusic3.dispatch.OscCommunicationEffects;
import com.example.instrmusic3.dispatch.OscConfiguration;
import com.example.instrmusic3.dispatch.OscHandlerEffects;
import com.example.instrmusic3.sensors.Settings;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EffectsFragment extends Fragment {
    EffectConfiguration effectConfiguration;
    String name;
    private Handler handler = new Handler();
    private Settings settings;
    int onOff = 0;
    public Settings getSettings() {
        return this.settings;
    }


    public EffectsFragment() {
        super();
        this.effectConfiguration = new EffectConfiguration();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StartUpEffectActivity startUpActivity = (StartUpEffectActivity) getActivity();
        startUpActivity.getSettings();
        this.settings = startUpActivity.loadSettings();
        Bundle args = this.getArguments();
        assert args != null;
        name = args.getString(Bundling.EFFECT_NAME);
        this.name = args.getString(Bundling.EFFECT_NAME);
        this.effectConfiguration.setEffectName(args.getString(Bundling.EFFECT_NAME));
        View v = inflater.inflate(R.layout.effects, null);
        TextView groupName = v.findViewById(R.id.group_name);
        setName(this.name);

        groupName.setText(name);

        //CompoundButton activeButton = v.findViewById(R.id.active);
        // EffectActivity activity = (EffectActivity) getActivity();
        Button button = v.findViewById(R.id.active);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOff == 0) {
                    onOff = 1;
                    OscCommunicationEffects communication = new OscCommunicationEffects("OSC dispatcher thread", Thread.MIN_PRIORITY);
                    communication.start();
                    OscHandlerEffects handler = communication.getOscHandler();
                    OscConfiguration oscConfiguration = OscConfiguration.getInstance();
                    List<Object> args = new ArrayList<Object>(1);
                    args.add(name);
                    OSCPortOut sender = oscConfiguration.getOscPort();
                    OSCMessage msg = new OSCMessage("/", args);
                    try {
                        sender.send(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    onOff = 0;
                }
            }
        });
        //activeButton.setOnCheckedChangeListener((compoundButton, checked) -> effectConfiguration.setSend(checked));

        // activeButton.setOnCheckedChangeListener(activity);

        return v;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public EffectConfiguration getEffectConfiguration() {
        return effectConfiguration;
    }


    public void setName(String name1) {
        this.name = name1;

    }

    public String getName() {
        return this.name;
    }
}
