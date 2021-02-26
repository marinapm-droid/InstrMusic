package com.example.instrmusic3.dispatch;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

import java.net.SocketException;

public class OscReceiveConfigSound {
    private static String message;

    public void receive() {
        try {
            OSCPortIn testIn = new OSCPortIn(5650);
            OSCListener listener = new OSCListener() {
                public void acceptMessage(java.util.Date time, OSCMessage message) {
                    setMessage(message.getArguments().toString());
                    System.out.println("mensagem SOM: " + message.getArguments());
                }
            };
            testIn.addListener("/sound", listener);
            testIn.startListening();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String message1) {
        message = message1;
    }
    public static String getMessage(){
        message = message.replace("]", "").replace("[", "");
        return message;
    }
}