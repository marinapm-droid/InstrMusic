package com.example.instrmusic3.Effects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.instrmusic3.dispatch.OscReceiveConfig;

public class ParametersEffects extends OscReceiveConfig {
    static OscReceiveConfig oscReceiveConfig;
    static private final String effectMessage = oscReceiveConfig.getMessage();
    String effectName;

    private ParametersEffects(String effectName) {
        this.effectName = effectName;
    }

    static List<String> effectsList = new ArrayList<>(Arrays.asList(effectMessage.split(", ")));

    public static List<String> getEffectList() {
        return effectsList;
    }

}
