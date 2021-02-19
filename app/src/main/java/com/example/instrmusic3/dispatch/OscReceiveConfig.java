package com.example.instrmusic3.dispatch;

import android.os.Bundle;
import android.util.Log;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

import java.net.SocketException;
import java.util.Date;

public class OscReceiveConfig {
    protected void Receive() {
        try {
            OSCPortIn testIn = new OSCPortIn(5679);
            Log.d("Listening", "running");
            OSCListener listener = new OSCListener() {
                public void acceptMessage(java.util.Date time, OSCMessage message) {
                    Log.d("message", "ola");
                }
            };
            testIn.addListener("/scd", listener);
            testIn.startListening();
        } catch (SocketException e) {
            e.printStackTrace();
        }


    }
}