package com.thesummitdev.rotclean_v1;

import android.Manifest;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class wizard_init extends AppIntro {

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
        sliderPage1.setImageDrawable(R.drawable.summit);
        sliderPage1.setBgColor( Color.TRANSPARENT);
        addSlide( AppIntroFragment.newInstance(sliderPage1));
        //setContentView( R.layout.activity_wizard_init );
        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Se parte del cambio");
        sliderPage2.setDescription("Encuentra la ubicacion de un contenedor cerca a tu area.");
        sliderPage2.setImageDrawable(R.drawable.ubicacion);
        sliderPage2.setBgColor(Color.TRANSPARENT);
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("Mas opciones");
        sliderPage3.setDescription("Desliza este boton y encuentra estas opciones.");
        sliderPage3.setImageDrawable(R.drawable.menu);
        sliderPage3.setBgColor(Color.TRANSPARENT);
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle("Conocenos");
        sliderPage4.setDescription("Veremos informacion de la empresa y donaciones!! ayudanos a mejorar.");
        sliderPage4.setImageDrawable(R.drawable.creditos);
        sliderPage4.setBgColor(Color.TRANSPARENT);
        addSlide(AppIntroFragment.newInstance(sliderPage4));
        askForPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
               /* Intent intent = new Intent(wizard_init.this,MapsActivity.class);
                startActivity(intent);*/
               finish();

               //prueba commit
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();

    }
}
