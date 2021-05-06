package com.example.instrmusic3.dispatch;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;

import org.sensors2.common.dispatch.Measurement;
import org.sensors2.common.dispatch.DataDispatcher;
import com.example.instrmusic3.sensors.Parameters;

import java.util.ArrayList;
import java.util.List;


public class OscDispatcher implements DataDispatcher {
    private final List<SensorConfiguration> sensorConfigurations = new ArrayList<>();
    private final OscCommunication communication;
    private float[] gravity;
    private float[] geomagnetic;

    public OscDispatcher() {
        communication = new OscCommunication("OSC dispatcher thread", Thread.MIN_PRIORITY);
        communication.start();
    }

    public void addSensorConfiguration(SensorConfiguration sensorConfiguration) {
        this.sensorConfigurations.add(sensorConfiguration);
    }

    @Override
    public void dispatch(Measurement sensorData) {
        // iterar os sensores disponíveis
        for (SensorConfiguration sensorConfiguration : this.sensorConfigurations) {
            if (sensorConfiguration.getSensorType() == sensorData.getSensorType()) {
                if (sensorData.getValues() != null) { //receber valores do sensor
                    trySend(sensorConfiguration, sensorData.getValues()); //enviar nome e valor do sensor
                } else {
                    trySend(sensorConfiguration, sensorData.getStringValue());
                }
            }
            if (sensorConfiguration.getSensorType() == Parameters.FAKE_ORIENTATION || sensorConfiguration.getSensorType() == Parameters.INCLINATION) {
                // Fake orientation
                if (sensorData.getSensorType() != Sensor.TYPE_ACCELEROMETER && sensorData.getSensorType() != Sensor.TYPE_MAGNETIC_FIELD) {
                    continue;
                }
                if (sensorData.getSensorType() == Sensor.TYPE_ACCELEROMETER) {
                    this.gravity = sensorData.getValues();
                }

                if (sensorData.getSensorType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    this.geomagnetic = sensorData.getValues();
                }
                if (this.gravity != null && this.geomagnetic != null) {
                    float[] rotationMatrix = new float[9];
                    float[] inclinationMatrix = new float[9];

                    boolean success = SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, this.gravity, this.geomagnetic);
                    if (success) {
                        if (sensorConfiguration.getSensorType() == Parameters.FAKE_ORIENTATION) {
                            float[] orientation = new float[3];
                            SensorManager.getOrientation(rotationMatrix, orientation);
                            this.trySend(sensorConfiguration, orientation);
                        }
                        if (sensorConfiguration.getSensorType() == Parameters.INCLINATION) {
                            float[] inclination = new float[1];
                            inclination[0] = SensorManager.getInclination(inclinationMatrix);
                            this.trySend(sensorConfiguration, inclination);
                        }
                    }
                }
            }
        }
    }

    private void trySend(SensorConfiguration sensorConfiguration, float[] values) {
        if (!sensorConfiguration.sendingNeeded(values)) {
            return;
        }
        Message message = new Message();
        Bundle data = new Bundle();
        data.putFloatArray(Bundling.VALUES, values);
        data.putString(Bundling.OSC_PARAMETER, sensorConfiguration.getOscParam());
        message.setData(data);
        OscHandler handler = communication.getOscHandler();
        handler.sendMessage(message);
    }

    private void trySend(SensorConfiguration sensorConfiguration, String value) {
        if (!sensorConfiguration.sendingNeeded(new float[0])) {
            return;
        }
        Message message = new Message();
        Bundle data = new Bundle();
        data.putString(Bundling.STRING_VALUE, value);
        data.putString(Bundling.OSC_PARAMETER, sensorConfiguration.getOscParam());
        message.setData(data);
        OscHandler handler = communication.getOscHandler();
        handler.sendMessage(message);
    }



    public void setSensorManager() {
    }
}
