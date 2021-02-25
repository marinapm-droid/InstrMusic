package com.example.instrmusic3.dispatch;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;

import com.example.instrmusic3.sensors.Parameters;

import org.sensors2.common.dispatch.DataDispatcher;
import org.sensors2.common.dispatch.Measurement;

import java.util.ArrayList;
import java.util.List;

public class OscDispatcherEffects {
    private final OscCommunicationEffects communication;
    private final List<EffectConfiguration> effectConfigurations = new ArrayList<>();


    public OscDispatcherEffects() {
        communication = new OscCommunicationEffects("OSC dispatcher thread", Thread.MIN_PRIORITY);
        communication.start();
    }

    private void trySend(EffectConfiguration effectConfigurations) {
      //  if (!effectConfiguration.sendingNeeded(new float[0])) {
        //    return;
        //}
        Message message = new Message();
        Bundle data = new Bundle();
        data.putString(Bundling.EFFECT_NAME, effectConfigurations.getEffectName());
        message.setData(data);
        OscHandlerEffects handler = communication.getOscHandler();
        handler.sendMessage(message);
    }

    public void setEffectManager() {

    }

}
