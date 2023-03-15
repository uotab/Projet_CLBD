package com.example.projetmajeur.register;

public class RegisterParams {
    String pwd;
    String username;
    String surname;
    String lastname;
    String email;
    RegisterParams(String pwd, String username,String surname, String lastname,String email){
        this.pwd=pwd;
        this.username=username;
        this.surname=surname;
        this.lastname=lastname;
        this.email=email;
    }
}
