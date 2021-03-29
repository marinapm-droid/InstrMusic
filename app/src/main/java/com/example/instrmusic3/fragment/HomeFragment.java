package com.example.instrmusic3.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;


import com.example.instrmusic3.HomePage;
import com.example.instrmusic3.R;
import com.example.instrmusic3.auth.Login;
import com.example.instrmusic3.dispatch.OscCommunication;
import com.example.instrmusic3.dispatch.OscConfiguration;
import com.example.instrmusic3.dispatch.OscHandler;
import com.example.instrmusic3.sensors.Settings;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Handler handler = new Handler();
    private Settings settings;
    int onOff = 0;
    int onOffStop = 0;
    int onOffRecord = 0;
    int count=0;
    boolean pauseActive=false;

    public Settings getSettings() {
        return this.settings;
    }
    String IP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HomePage activity = (HomePage) getActivity();
        activity.getSettings();
        this.settings = activity.loadSettings();
        sendGO();
        sendIP();
        sendHomeMsg();
        View v = inflater.inflate(R.layout.activity_home, container, false);
        CompoundButton activeButton = v.findViewById(R.id.active);
        activeButton.setOnCheckedChangeListener(activity);
        activeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOff == 0) {
                    onOff = 1;
                    OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
                    communication.start();
                    OscHandler handler = communication.getOscHandler();
                    OscConfiguration oscConfiguration = OscConfiguration.getInstance();
                    OSCPortOut sender = oscConfiguration.getOscPort();
                    List<Object> args = new ArrayList<Object>(1);
                    String IP = HomePage.getLocalIpAddress();
                    if(count!=2){
                        count++;
                    }
                    args.add(count);
                    System.out.println(args);
                    OSCMessage msg = new OSCMessage("/play"+IP, args);
                    try {
                        sender.send(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
                    communication.start();
                    OscHandler handler = communication.getOscHandler();
                    OscConfiguration oscConfiguration = OscConfiguration.getInstance();
                    OSCPortOut sender = oscConfiguration.getOscPort();
                    List<Object> args = new ArrayList<Object>(1);
                    String IP = HomePage.getLocalIpAddress();
                    OSCMessage msg = new OSCMessage("/pause"+IP);
                    try {
                        sender.send(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    onOff = 0;
                }
            }
        });

        CompoundButton recordButton = v.findViewById(R.id.record);
        recordButton.setOnCheckedChangeListener(activity);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOffRecord == 0) {
                    onOffRecord = 1;
                    count=0;
                    OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
                    communication.start();
                    OscHandler handler = communication.getOscHandler();
                    OscConfiguration oscConfiguration = OscConfiguration.getInstance();
                    OSCPortOut sender = oscConfiguration.getOscPort();
                    String IP = HomePage.getLocalIpAddress();
                    OSCMessage msg = new OSCMessage("/startRecord"+IP);
                    try {
                        sender.send(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
                    communication.start();
                    OscHandler handler = communication.getOscHandler();
                    OscConfiguration oscConfiguration = OscConfiguration.getInstance();
                    OSCPortOut sender = oscConfiguration.getOscPort();
                    List<Object> args = new ArrayList<Object>(1);
                    String IP = HomePage.getLocalIpAddress();
                    args.add(Login.getUsername());
                    OSCMessage msg = new OSCMessage("/stopRecord"+IP, args);
                    try {
                        sender.send(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    onOffRecord = 0;
                }
            }
        });


        activeButton.setOnCheckedChangeListener(activity);
        CompoundButton stopButton = v.findViewById(R.id.stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOffStop == 0) {
                    onOffStop = 1;
                    if (onOff==1){
                        activeButton.setChecked(false);
                        onOff=0;
                    }
                    OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
                    communication.start();
                    OscHandler handler = communication.getOscHandler();
                    OscConfiguration oscConfiguration = OscConfiguration.getInstance();
                    OSCPortOut sender = oscConfiguration.getOscPort();
                    String IP = HomePage.getLocalIpAddress();
                    OSCMessage msg = new OSCMessage("/stop"+IP);
                    try {
                        sender.send(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    onOffStop = 0;
                }
            }
        });
        return v;
    }

    public void sendIP(){
        IP = HomePage.getLocalIpAddress();
        OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
        communication.start();
        OscHandler handler = communication.getOscHandler();
        OscConfiguration oscConfiguration = OscConfiguration.getInstance();
        OSCPortOut sender = oscConfiguration.getOscPort();
        List<Object> args = new ArrayList<Object>(1);
        args.add(IP);
        OSCMessage msg = new OSCMessage("/ip", args);
        try {
            sender.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendHomeMsg(){
        IP = HomePage.getLocalIpAddress();
        OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
        communication.start();
        OscHandler handler = communication.getOscHandler();
        OscConfiguration oscConfiguration = OscConfiguration.getInstance();
        OSCPortOut sender = oscConfiguration.getOscPort();
        List<Object> args = new ArrayList<Object>(1);
        args.add(IP);
        OSCMessage msg = new OSCMessage("/home", args);
        try {
            sender.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendGO(){
        IP = HomePage.getLocalIpAddress();
        OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
        communication.start();
        OscHandler handler = communication.getOscHandler();
        OscConfiguration oscConfiguration = OscConfiguration.getInstance();
        OSCPortOut sender = oscConfiguration.getOscPort();
        OSCMessage msg = new OSCMessage("/GO");
        try {
            sender.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}