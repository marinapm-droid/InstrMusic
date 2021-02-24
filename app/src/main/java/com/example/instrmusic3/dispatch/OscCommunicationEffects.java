package com.example.instrmusic3.dispatch;

import android.os.HandlerThread;


public class OscCommunicationEffects extends HandlerThread {
    private OscHandlerEffects handler;

    public OscCommunicationEffects(String name, int priority) {
        super(name, priority);
    }

    @Override
    public void run() {
        super.run();
        handler = null;
    }

    @Override
    protected void onLooperPrepared() {
        handler = new OscHandlerEffects(this.getLooper());
    }

    public OscHandlerEffects getOscHandler() {
        return handler;
    }


}
