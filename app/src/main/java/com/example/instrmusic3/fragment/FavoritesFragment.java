package com.example.instrmusic3.fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.instrmusic3.Effects.ParametersEffects;
import com.example.instrmusic3.HomePage;
import com.example.instrmusic3.R;
import com.example.instrmusic3.dispatch.Bundling;
import com.example.instrmusic3.dispatch.OscCommunication;
import com.example.instrmusic3.dispatch.OscConfiguration;
import com.example.instrmusic3.dispatch.OscHandler;
import com.example.instrmusic3.dispatch.OscReceiveConfig;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import org.sensors2.common.sensors.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoritesFragment extends Fragment {
    String fav;
    int onOff=0;
    String effect, sensor, sound;

    public FavoritesFragment() {
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
        fav = args.getString(Bundling.FAV);
        this.fav = args.getString(Bundling.FAV);
        View v = inflater.inflate(R.layout.favorites, null);
        TextView groupName = v.findViewById(R.id.group_name);
        setName(this.fav);
        List<String> favs =  new ArrayList<>(Arrays.asList(fav.split("/")));
        sound=favs.get(0);
        effect=favs.get(1);
        sensor=favs.get(2);
        groupName.setText(fav);
        Button button = v.findViewById(R.id.active);
        button.setOnClickListener(v1 -> {
            if (onOff == 0) {
                onOff = 1;
                OscCommunication communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
                communication.start();
                communication.getOscHandler();
                OscConfiguration oscConfiguration = OscConfiguration.getInstance();
                List<Object> args1 = new ArrayList<>(1);
                args1.add(effect);
                OSCPortOut sender = oscConfiguration.getOscPort();
                OSCMessage msg = new OSCMessage("/effect", args1);
                try {
                    sender.send(msg);
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
        this.fav = name1;

    }

}

