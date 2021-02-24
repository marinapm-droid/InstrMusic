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
    private final List<String> effectConfigurations = new ArrayList<>();


    public OscDispatcherEffects() {
        communication = new OscCommunicationEffects("OSC dispatcher thread", Thread.MIN_PRIORITY);
        communication.start();
    }

    public void addEffectConfiguration(String effectConfigurations) {
        this.effectConfigurations.add(effectConfigurations);
        System.out.println("Effect configurations " + this.effectConfigurations);
        for (String effectConfigurations1 : this.effectConfigurations) {
            System.out.println("2:" + effectConfigurations1);
            trySend(effectConfigurations1);
        }
    }


    public void dispatch() {
        for (String effectConfigurations : this.effectConfigurations) {
            System.out.println("2:" + effectConfigurations);
            trySend(effectConfigurations);
        }
    }

    private void trySend(String effectConfigurations) {
      //  if (!effectConfiguration.sendingNeeded(new float[0])) {
        //    return;
        //}
        Message message = new Message();
        Bundle data = new Bundle();
        data.putString("", effectConfigurations);
        System.out.println("top" + effectConfigurations);
        System.out.println("cócó" + data);
        message.setData(data);
        OscHandlerEffects handler = communication.getOscHandler();
        handler.sendMessage(message);
    }

    public void setEffectManager() {

    }

}
