package com.example.instrmusic3.Effects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.instrmusic3.dispatch.OscReceiveConfig;

public class ParametersEffects {
    public static List<String> getEffects() {
        OscReceiveConfig oscReceiveConfig = new OscReceiveConfig();
        oscReceiveConfig.receive();
        System.out.println("Parameters Effects" + OscReceiveConfig.getMessage());
        String effectMessage = oscReceiveConfig.getMessage();
        return new ArrayList<>(Arrays.asList(effectMessage.split(", ")));
    }


    public ParametersEffects(){
    }

}
