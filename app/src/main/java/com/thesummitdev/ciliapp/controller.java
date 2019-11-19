package com.thesummitdev.ciliapp;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.paolorotolo.appintro.AppIntro;
import com.thesummitdev.ciliapp.Vistas.MapsActivity;

public class controller extends AppIntro {
    SharedPreferences settings;
    boolean firstRun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences( "prefs", 0 );
        firstRun = settings.getBoolean( "firstRun", false );
        if (firstRun == true) {
            Intent i = new Intent( getApplicationContext(), MapsActivity.class );
            startActivity( i );
        }else{
            Intent i = new Intent( getApplicationContext(), wizard_init.class );
            startActivity( i );

        }

    }

}
