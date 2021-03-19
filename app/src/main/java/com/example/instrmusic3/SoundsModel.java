package com.example.instrmusic3;

import androidx.databinding.ObservableArrayMap;
import androidx.lifecycle.ViewModel;

import com.example.instrmusic3.Effects.ParametersEffects;
import com.example.instrmusic3.sounds.ParametersSounds;

import java.util.List;

public class SoundsModel extends ViewModel {

    private final ObservableArrayMap<String, Boolean> sensorState;

    public SoundsModel() {
        ParametersSounds.getSounds();
        sensorState = new ObservableArrayMap();
        List<String> soundList = ParametersSounds.getSounds();
        for (String effect : soundList) {
            sensorState.put(effect, false);
        }
    }

    public ObservableArrayMap<String, Boolean> getSensorState() {
        return sensorState;
    }
}
