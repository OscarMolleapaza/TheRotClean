package com.thesummitdev.rotclean_v1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity {

    private final int LOCATION = 1;
    //summit123  alias : newkeyrotclean
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        //checkLocationPermission();

        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN );
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION );
        }*/
        SharedPreferences settings = getSharedPreferences( "prefs", 0 );
        boolean firstRun = settings.getBoolean( "firstRun", false );
        if (firstRun == false) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean( "firstRun", true );
            editor.commit();

            Intent i = new Intent( MapsActivity.this, wizard_init.class );
            startActivity( i );
        }

        setContentView( R.layout.activity_maps );
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier( 1 ).withIcon( R.drawable.ic_map_ligh ).withName( "Mapa" ); //MAPA
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier( 2 ).withIcon( R.mipmap.district).withName( "Distritos" ); //Distritos
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier( 3 ).withIcon( R.drawable.ic_acerca ).withName( "Sugerencias" ); //Sugerencias
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier( 4 ).withIcon( R.drawable.ic_informacion ).withName( "Creditos" );     //Creditos
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier( 5 ).withIcon( R.mipmap.comments).withName( "Comentarios" ); //Comentarios
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier( 6 ).withIcon( R.drawable.ic_menu_share ).withName( "Admin" ); //ADMIN


        new DrawerBuilder()
                .withActivity( this )
                .withHeader( R.layout.util_drawer_hdr )
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        item6

                )
                .withOnDrawerItemClickListener( new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Fragment fragment = null;
                            FragmentManager fragmentManager = getSupportFragmentManager();

                            switch ((int) drawerItem.getIdentifier()) {
                                case 1:


                                    fragment = new mapa();
                                   // Mapa
                                    break;
                                case 2:

                                    fragment = new Distritos();
                                  //  Distritos
                                    break;
                                case 3:

                                    fragment = new sugerencias();
                                   // Sugerencias
                                    break;
                                case 4:
                                    fragment = new creditos();
                                    //Creditos
                                    break;
                                case 5:
                                    fragment = new Comentarios();
                                   // comentarios
                                    break;
                                case 6:
                                    fragment = new admin();
                                    // admin
                                    break;
                            }

                            if (fragment != null) {
                                fragmentManager.beginTransaction().replace( R.id.content_frame, fragment ).commit();
                            }

                            if (drawerItem instanceof Nameable) {
                                setTitle( ((Nameable) drawerItem).getName().getText( getApplicationContext() ) );
                            }
                        }

                        return false;
                    }
                } )
                .withShowDrawerOnFirstLaunch( false )
                .withFireOnInitialOnClick( true )
                .withShowDrawerUntilDraggedOpened( false )
                .build();
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Permiso")
                        .setMessage("Necesitamos permiso para acceder a su ubicacion.")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);


                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),"Como chuchas vamos a ver tu ubicacion entonces, me vale madres igual lo tomare como un si",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
}