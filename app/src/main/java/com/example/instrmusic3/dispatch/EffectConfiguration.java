package com.example.instrmusic3.dispatch;

import com.example.instrmusic3.sensors.Parameters;

public class EffectConfiguration {
    private boolean send;
    private String effectName;

    public EffectConfiguration() {
    }

    public boolean sendingNeeded(float[] values) {
        if (!this.send) {
            return false;
        }
       // if (sendDuplicates) {
         //   return true;
        //}
        boolean differenceDetected = false;
        //for (int i = 0; i < values.length && i < Parameters.MAX_DIMENSIONS; i++) {
           // if (Math.abs(values[i] - this.currentValues[i]) != 0) {
             //   differenceDetected = true;
           // }
           // this.currentValues[i] = values[i];
        //}
        return differenceDetected;
    }

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
