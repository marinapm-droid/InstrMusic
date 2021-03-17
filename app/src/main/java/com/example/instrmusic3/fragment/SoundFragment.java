package com.example.instrmusic3.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.instrmusic3.HomePage;
import com.example.instrmusic3.R;
import com.example.instrmusic3.dispatch.Bundling;
import com.example.instrmusic3.dispatch.OscCommunication;
import com.example.instrmusic3.dispatch.OscConfiguration;
import com.example.instrmusic3.dispatch.OscHandler;
import com.example.instrmusic3.sensors.Settings;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.util.ArrayList;
import java.util.List;

public class SoundFragment extends Fragment {
    static String name;
    private Handler handler = new Handler();
    private Settings settings;
    static int onOff = 0;
    public Settings getSettings() {
        return this.settings;
    }
    static CompoundButton button;

    public SoundFragment() {
        super();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HomePage startUpActivity = (HomePage) getActivity();
        startUpActivity.getSettings();
        this.settings = startUpActivity.loadSettings();
        Bundle args = this.getArguments();
        assert args != null;
        name = args.getString(Bundling.SOUND_NAME);
        View v = inflater.inflate(R.layout.sounds, null);
        TextView groupName = v.findViewById(R.id.group_name);
        setName(name);

        groupName.setText(name);

        button = v.findViewById(R.id.active);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOff == 0) {
                    onOff = 1;
                    OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
                    communication.start();
                    OscHandler handler = communication.getOscHandler();
                    OscConfiguration oscConfiguration = OscConfiguration.getInstance();
                    List<Object> args = new ArrayList<Object>(1);
                    HomePage.setSound(name);
                    args.add(name);
                    OSCPortOut sender = oscConfiguration.getOscPort();
                    OSCMessage msg = new OSCMessage("/sound", args);
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
        return v;
    }

    public static void setSelected(){
        button.setChecked(true);
        if (onOff == 0) {
            onOff = 1;
            OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
            communication.start();
            OscHandler handler = communication.getOscHandler();
            OscConfiguration oscConfiguration = OscConfiguration.getInstance();
            List<Object> args = new ArrayList<Object>(1);
            HomePage.setSound(name);
            args.add(name);
            OSCPortOut sender = oscConfiguration.getOscPort();
            OSCMessage msg = new OSCMessage("/sound", args);
            try {
                sender.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            onOff = 0;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void setName(String name1) {
        name = name1;

    }

    public String getName() {
        return name;
    }
}
