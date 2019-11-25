package com.thesummitdev.ciliapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.thesummitdev.ciliapp.Vistas.MapsActivity;

public class wizard_init extends AppIntro {

    SharedPreferences settings;
    boolean firstRun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle("Hola Somos The Summit Developers!");
        sliderPage1.setDescription("Acompañanos en este pequeño Tutorial.");
        sliderPage1.setImageDrawable(R.drawable.summitblanco1);
        //sliderPage1.setBgColor( Color.TRANSPARENT);
        sliderPage1.setBgColor(Color.parseColor("#154E94"));
        addSlide( AppIntroFragment.newInstance(sliderPage1));
        //setContentView( R.layout.activity_wizard_init );

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Se parte del cambio");
        sliderPage2.setDescription("Encuentra la ubicacion de un contenedor cerca a tu area.");
        sliderPage2.setImageDrawable(R.drawable.slider2);
        sliderPage2.setBgColor(Color.parseColor("#074343"));


        addSlide(AppIntroFragment.newInstance(sliderPage2));

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("Sugiere un Contenedor");
        sliderPage3.setDescription("Puedes sugerir un contenedor cerca a tu area.");
        sliderPage3.setImageDrawable(R.drawable.slider3);
        sliderPage3.setBgColor(Color.parseColor("#154E94"));
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle("Reporta un Contenedor");
        sliderPage4.setDescription("Puedes reportar un Contenedor dependiendo de su estado.");
        sliderPage4.setImageDrawable(R.drawable.screen_reporte);
        sliderPage4.setBgColor(Color.parseColor("#074343"));
        addSlide(AppIntroFragment.newInstance(sliderPage4));

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        settings = getSharedPreferences( "prefs", 0 );
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean( "firstRun", true );
        editor.commit();
        Intent intent = new Intent(wizard_init.this,MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);

        finish();

               //prueba commit
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        settings = getSharedPreferences( "prefs", 0 );
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean( "firstRun", true );
        editor.commit();
        Intent intent = new Intent(wizard_init.this,MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
        finish();


    }
}
