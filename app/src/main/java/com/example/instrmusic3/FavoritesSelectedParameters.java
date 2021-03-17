package com.example.instrmusic3;

public class FavoritesSelectedParameters {
    private static String soundSelected, effectSelected, sensorSelected;

    public static void setEffectSelected(String effectSelected1, String soundSelected1, String sensorSelected1) {
        effectSelected = effectSelected1;
        soundSelected = soundSelected1;
        sensorSelected = sensorSelected1;
    }

    public static String getSound() {
        return soundSelected;
    }

    public static String getEffect() {
        return effectSelected;
    }

    public static String getSensor() {
        return sensorSelected;
    }


}
