package com.example.instrmusic3.sounds;

import com.example.instrmusic3.dispatch.OscReceiveConfigSound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParametersSounds {
    public static List<String> getSounds() {
        OscReceiveConfigSound oscReceiveConfig = new OscReceiveConfigSound();
        oscReceiveConfig.receive();
        System.out.println("Parameters Sounds" + OscReceiveConfigSound.getMessage());
        String soundMessage = oscReceiveConfig.getMessage();
        return new ArrayList<>(Arrays.asList(soundMessage.split(",")));
    }


    public ParametersSounds(){
    }

}