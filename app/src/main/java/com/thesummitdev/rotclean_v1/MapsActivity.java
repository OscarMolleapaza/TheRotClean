package com.thesummitdev.rotclean_v1;

import android.Manifest;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );


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
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier( 1 ).withIcon( R.drawable.ic_map_ligh ).withName( "Mapa" );
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier( 2 ).withIcon( R.drawable.ic_informacion ).withName( "Contenedores" );
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier( 3 ).withIcon( R.drawable.ic_donation ).withName( "Distritos Disponibles" );
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier( 4 ).withIcon( R.drawable.ic_acerca ).withName( "En Desarrollo" );
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier( 5 ).withIcon( R.drawable.ic_settingsajuestes ).withName( "Sobre Nosotros" );
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier( 6 ).withIcon( R.drawable.ic_menu_share ).withName( "Admin" );


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
                                   // Toast.makeText( MapsActivity.this, "uno contenido", Toast.LENGTH_LONG ).show();
                                    //ir a otro fragment
                                    break;
                                case 2:

                                    fragment = new informacion();
                                  //  Toast.makeText( MapsActivity.this, "dos contenido", Toast.LENGTH_LONG ).show();
                                    break;
                                case 3:

                                    fragment = new donaciones();
                                   // Toast.makeText( MapsActivity.this, "tres contenido", Toast.LENGTH_LONG ).show();
                                    break;
                                case 4:
                                    fragment = new creditos();
                                    //Toast.makeText( MapsActivity.this, "cuatro contenido", Toast.LENGTH_LONG ).show();
                                    break;
                                case 5:
                                    fragment = new configuracion();
                                   // Toast.makeText( MapsActivity.this, "quinto contenido", Toast.LENGTH_LONG ).show();
                                    break;
                                case 6:
                                    fragment = new admin();
                                    // Toast.makeText( MapsActivity.this, "quinto contenido", Toast.LENGTH_LONG ).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION:
                String permission = permissions[0];
                int result = grantResults[0];
                String permission2 = permissions[1];
                int result2 = grantResults[1];
                if (permission.equals( Manifest.permission.ACCESS_FINE_LOCATION ) && permission2.equals( Manifest.permission.ACCESS_COARSE_LOCATION )) {
                    if (result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText( MapsActivity.this, "Permiso Activado", Toast.LENGTH_LONG );

                    } else {
                        Toast.makeText( MapsActivity.this, "Permiso Denegado", Toast.LENGTH_LONG );
                    }
                }
                break;

            default:
                super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        }
    }
}