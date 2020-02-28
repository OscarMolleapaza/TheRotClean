package com.thesummitdev.ciliapp.models;

public class Sugerencias {

    String sugerencia_id;
    String comentario;
    String interseccion;
    Double latitud;
    Double longitud;
    String imagenBase64;
    String tipo_tacho;
    String distrito;


    public String getSugerencia_id() {
        return sugerencia_id;
    }

    public void setSugerencia_id(String sugerencia_id) {
        this.sugerencia_id = sugerencia_id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getInterseccion() {
        return interseccion;
    }

    public void setInterseccion(String interseccion) {
        this.interseccion = interseccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }

    public String getTipo_tacho() {
        return tipo_tacho;
    }

    public void setTipo_tacho(String tipo_tacho) {
        this.tipo_tacho = tipo_tacho;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

}
