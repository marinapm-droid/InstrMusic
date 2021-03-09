package com.example.instrmusic3;

public class FavoritesParameters {
    String sound, effect, sensor;

    public FavoritesParameters() {}

    public String getEffect() {
        return this.effect;
    }

    public String getSensor() {
        return this.sensor;
    }

    public String getSound() {
        return this.sound;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
