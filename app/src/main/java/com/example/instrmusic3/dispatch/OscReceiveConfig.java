package com.example.instrmusic3.dispatch;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCPortIn;
import java.net.SocketException;

public class OscReceiveConfig {
    private static String message;

    public void receive() {
        try {
            OSCPortIn testIn = new OSCPortIn(5679);
            OSCListener listener = (time, message) -> {
                setMessage(message.getArguments().toString());
                System.out.println("mensagem: " + message.getArguments());
            };
            testIn.addListener("/effect", listener);
            testIn.startListening();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String message1) {
         message = message1;
    }
    public static String getMessage(){
        message = message.replace("]", "").replace("[", "").replace("List", "");
        return message;
    }
}