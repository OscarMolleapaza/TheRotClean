package com.thesummitdev.ciliapp;

import android.content.SharedPreferences;

public class Utils {
    public static void setData(SharedPreferences prefs,String pais,String departamento,String distrito){
        SharedPreferences.Editor edit= prefs.edit();
        edit.putString("pais",pais);
        edit.putString("departamento",departamento);
        edit.putString("distrito",distrito);
        edit.apply();
    }
    public static String getPais(SharedPreferences prefs){
        return  prefs.getString("pais","Perú");
    }
    public static String getDepartamento(SharedPreferences prefs){
        return  prefs.getString("departamento","Puno");
    }
    public static String getDistrito(SharedPreferences prefs){
        return  prefs.getString("distrito","Puno");
    }
}
