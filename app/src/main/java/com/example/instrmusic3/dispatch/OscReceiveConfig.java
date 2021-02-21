package com.example.instrmusic3.dispatch;

import android.util.Log;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

import java.net.SocketException;

public class OscReceiveConfig {
    private static String message;

    public void receive() {
        try {
            OSCPortIn testIn = new OSCPortIn(5679);
            Log.d("Listening", "A Correr");
            OSCListener listener = new OSCListener() {
                public void acceptMessage(java.util.Date time, OSCMessage message) {
                    System.out.println("oscReceiveConfig" + message.getArguments().toString());
                    setMessage(message.getArguments().toString());
                }
            };
            testIn.addListener("/scd", listener);
            testIn.startListening();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String message1) {
         message = message1;
    }
    public static String getMessage(){
        System.out.println("Get Message" + message);
        message = message.replace("]", "").replace("[", "");
        return message;
    }
}