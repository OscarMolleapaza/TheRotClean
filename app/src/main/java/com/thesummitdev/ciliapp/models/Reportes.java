package com.thesummitdev.ciliapp.models;

public class Reportes {


    String reporte_id;
    double latitud;
    double longitud;
    String tipo_tacho;

    String tipo_reporte;
    String comentario;
    String estado;
    String user_id;


    public String getReporte_id() {
        return reporte_id;
    }

    public void setReporte_id(String reporte_id) {
        this.reporte_id = reporte_id;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getTipo_tacho() {
        return tipo_tacho;
    }

    public void setTipo_tacho(String tipo_tacho) {
        this.tipo_tacho = tipo_tacho;
    }

    public String getTipo_reporte() {
        return tipo_reporte;
    }

    public void setTipo_reporte(String tipo_reporte) {
        this.tipo_reporte = tipo_reporte;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
