package com.thesummitdev.ciliapp.models;

public class Tachos {

    private String tachoID;
    private double latitud;
    private double longitud;
    private String comentario;
    private String lugarDistrito;
    private String tipoTacho;
    private String imagenbase64;


    public double getLatitud() {
        return latitud;
    }
    public double getLongitud() {
        return longitud;
    }
    public String getComentario() {
        return comentario;
    }
    public String getLugarDistrito() {
        return lugarDistrito;
    }
    public String getTipoTacho() {
        return tipoTacho;
    }
    public String getImagenbase64() {
        return imagenbase64;
    }
    public String getTachoID() { return tachoID; }
}
