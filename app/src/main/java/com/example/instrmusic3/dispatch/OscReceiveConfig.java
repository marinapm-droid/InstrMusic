package com.example.instrmusic3.dispatch;

import android.util.Log;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

import java.net.SocketException;

public class OscReceiveConfig {
    String message;

    public void receive() {
        try {
            OSCPortIn testIn = new OSCPortIn(5679);
            Log.d("Listening", "running");
            OSCListener listener = new OSCListener() {
                public void acceptMessage(java.util.Date time, OSCMessage message) {
                    Log.d("message", "ola");
                    setMessage(message.getArguments().toString());
                }
            };
            testIn.addListener("/scd", listener);
            testIn.startListening();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String message) {
         this.message=message;
    }
    public String getMessage(){
        return this.message;
    }
}