package com.example.instrmusic3.sensors;

import androidx.databinding.ObservableArrayMap;
import androidx.lifecycle.ViewModel;

public class SensorsModel extends ViewModel {

    private final ObservableArrayMap<String,Boolean> sensorState;

    public SensorsModel() {
        sensorState = new ObservableArrayMap();
        sensorState.put("Accelerometer", false);
        sensorState.put("Gyroscope", false);
        sensorState.put("Magnetic Field", false);
    }

    public ObservableArrayMap<String, Boolean> getSensorState() {
        return sensorState;
    }
}
