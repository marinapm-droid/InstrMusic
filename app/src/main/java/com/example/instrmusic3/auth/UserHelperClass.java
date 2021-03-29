package com.example.instrmusic3.auth;

public class UserHelperClass {

    String nome,phone, password, sound, effect, sensor;


    public UserHelperClass(String nome, String phone, String password) {
        this.nome = nome;
        this.phone = phone;
        this.password = password;
    }

    public UserHelperClass() {

    }


    public void UserHelperClass1(String sensor, String effect, String sound) {
        this.effect = effect;
        this.sensor = sensor;
        this.sound = sound;
    }

    public String getNome() {
        return nome;
    }

    public String getEffect() {
        return effect;
    }

    public String getSound() {
        return sound;
    }

    public String getSensor() {
        return sensor;
    }

    // public void setNome(String nome) {
     //   this.nome = nome;
   // }

    public String getPhone() {
        return phone;
    }

    //    public void setEmail(String email) {
    //        this.phone = phone;
    //    }

    public String getPassword() {
        return password;
    }

    //  public void setPassword(String password) {
    //      this.password = password;
    //  }

}
