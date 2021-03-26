package com.example.instrmusic3.auth;

public class UserHelperClass {

    String nome,phone, password, confPassword, sound, effect, sensor, username;


    public UserHelperClass(String nome, String phone, String password, String confPassword) {
        this.nome = nome;
        this.phone = phone;
        this.password = password;
        this.confPassword = confPassword;
    }



    public UserHelperClass(String sensor, String effect, String sound) {
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

    public String getConfPassword() {
        return confPassword;
    }

    //   public void setConfPassword(String confPassword) {
    //      this.confPassword = confPassword;
    //    }
}
