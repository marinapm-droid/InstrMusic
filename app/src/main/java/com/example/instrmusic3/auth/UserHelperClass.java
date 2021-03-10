package com.example.instrmusic3.auth;

public class UserHelperClass {

    private static String nome;
    String email, password, confPassword, effect, sound, sensor;


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

    public static String getNome() {
        return nome;
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
