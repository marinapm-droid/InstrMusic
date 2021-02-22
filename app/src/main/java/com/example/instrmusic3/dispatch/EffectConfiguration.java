package com.example.instrmusic3.dispatch;

import com.example.instrmusic3.sensors.Parameters;

public class EffectConfiguration {
    private boolean send;
    private String effectName;


    public void setSend(boolean send) {
        this.send = send;
    }

    public void setEffectName(String effectName) {
        this.effectName=effectName;
    }

    public String getEffectName() {
        return this.effectName;
    }

}
