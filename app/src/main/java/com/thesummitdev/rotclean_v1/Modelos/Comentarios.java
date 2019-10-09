package com.thesummitdev.rotclean_v1.Modelos;

import android.content.Intent;

public class Comentarios {

    String comentario_id;
    String calificacion;
    String comentario;
    Integer score;
    public String getComentario_id() {
        return comentario_id;
    }

    public void setComentario_id(String comentario_id) {
        this.comentario_id = comentario_id;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

}
