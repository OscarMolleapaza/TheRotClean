package com.thesummitdev.ciliapp.models;

public class Usuarios {

    String user_id;
    String nombres;
    String apellidos;
    String email;
    String tipo_user;
    Integer num_casos;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo_user() {
        return tipo_user;
    }

    public void setTipo_user(String tipo_user) {
        this.tipo_user = tipo_user;
    }

    public Integer getNum_casos() {
        return num_casos;
    }

    public void setNum_casos(Integer num_casos) {
        this.num_casos = num_casos;
    }


}
