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
            OSCPortIn receiver = new OSCPortIn(5679);
            Log.d("test","test");
            OSCListener listener = new OSCListener() {
                public void acceptMessage(Date time, OSCMessage message) {
                    Log.d("Message","Message received!");
                }
            };
            receiver.addListener("/scd", listener);
            receiver.startListening();
        } catch (SocketException e) {
            Log.d("OSCSendInitalisation", "Socket exception error!");
        }


    }
}