package com.example.instrmusic3.dispatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.instrmusic3.activities.HomePage;
import com.illposed.osc.OSCMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OscHandler extends Handler {

    public OscHandler(Looper myLooper) {
        super(myLooper);
    }
    public OscHandler( ) {

    }
    String oscParameter;

    public String getParameter(){
        return oscParameter;
    }

    @Override
    public void handleMessage(Message message) {
        Bundle data = message.getData();
        float[] values = data.getFloatArray(Bundling.VALUES);
        String stringValue = data.getString(Bundling.STRING_VALUE);
        oscParameter = data.getString(Bundling.OSC_PARAMETER);
        OscConfiguration configuration = OscConfiguration.getInstance();

        if (configuration == null || configuration.getOscPort() == null) {
            return;
        }
        List<Object> changes = new ArrayList<>();
        if (values != null) {
            for (float value : values) {
                changes.add(value);
            }
        }
        if (stringValue != null) {
            changes.add(stringValue);
        }
        String IP = HomePage.getLocalIpAddress();
        OSCMessage oscMessage = new OSCMessage("/"  + oscParameter + IP, changes);

        try {
            configuration.getOscPort().send(oscMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
