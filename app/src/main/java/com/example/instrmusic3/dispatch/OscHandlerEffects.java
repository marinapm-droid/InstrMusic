package com.example.instrmusic3.dispatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.illposed.osc.OSCMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OscHandlerEffects extends Handler {

    public OscHandlerEffects(Looper myLooper) {
        super(myLooper);
    }

    @Override
    public void handleMessage(Message message) {
        Bundle data = message.getData();
        String oscEffectName = data.getString(Bundling.EFFECT_NAME);
        String stringValue = data.getString(Bundling.STRING_VALUE);
        OscConfiguration configuration = OscConfiguration.getInstance();

        if (configuration == null || configuration.getOscPort() == null) {
            return;
        }
        System.out.println("3: " + oscEffectName);
        System.out.println("\n");
        System.out.println("4: " + message);
        System.out.println("bundle" + Bundling.EFFECT_NAME);
        System.out.println("data" + data);
        OSCMessage oscMessage = new OSCMessage("/" + oscEffectName);
        try {
            configuration.getOscPort().send(oscMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
