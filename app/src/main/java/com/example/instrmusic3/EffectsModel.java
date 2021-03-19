package com.example.instrmusic3;

import androidx.databinding.ObservableArrayMap;
import androidx.lifecycle.ViewModel;

import com.example.instrmusic3.Effects.ParametersEffects;
import com.example.instrmusic3.sounds.ParametersSounds;

import java.util.List;

public class EffectsModel extends ViewModel {

    private final ObservableArrayMap<String, Boolean> sensorState;

    public EffectsModel() {
        sensorState = new ObservableArrayMap();
        List<String> effectList = ParametersEffects.getEffects();
        for (String effect : effectList) {
            sensorState.put(effect, false);
        }


    }

    public ObservableArrayMap<String, Boolean> getEffectState() {
        return sensorState;
    }
}
