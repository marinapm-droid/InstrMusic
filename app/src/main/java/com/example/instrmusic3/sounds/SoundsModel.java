package com.example.instrmusic3.sounds;

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
        for (String sound : soundList) {
            sensorState.put(sound, false);
        }


    }

    public ObservableArrayMap<String, Boolean> getSoundState() {
        return sensorState;
    }
}
