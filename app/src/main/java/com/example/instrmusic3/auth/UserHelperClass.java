package com.example.instrmusic3.auth;

public class UserHelperClass {

    String nome, email, password, confPassword, effect, sound, sensor;


    public UserHelperClass(String nome, String email, String password, String confPassword) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.confPassword = confPassword;
    }

    public UserHelperClass(String effect, String sound, String sensor) {
        this.effect = effect;
        this.sound = sound;
        this.sensor = sensor;
    }

}
