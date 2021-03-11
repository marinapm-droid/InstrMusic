package com.example.instrmusic3.auth;

public class UserHelperClass {

    String nome,email, password, confPassword, sound, effect, sensor;


    public UserHelperClass(String nome, String email, String password, String confPassword) {
        this.nome = nome;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    //    public void setEmail(String email) {
    //        this.email = email;
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
