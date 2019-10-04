package com.thesummitdev.rotclean_v1;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class mapa extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback,LocationListener {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private DatabaseReference mDatabase; //FIREBASE
    FusedLocationProviderClient fusedLocationProviderClient; //Ultima Ubicacion
    LocationRequest locationRequest; //Actualizar posicion
    LocationCallback locationCallback; //ACtualizar posicion
    Location location;

    LocationManager locationManager;

    //private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>(); //Array Marcadores temporales de almacenamiento para hacer llamado
    //private ArrayList<Marker> realTimeMarkers = new ArrayList<>(); //Marcadores tiempo real (ESTA VARIABLE SE USARÁ PARA ALMACENAR LOS MARCADORES EN LA MEMORIA INTERNA)

    public mapa() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);

        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();

        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());


        mDatabase = FirebaseDatabase.getInstance().getReference(); //Instanciar BD Firebase
        lastPosition();
      //  initFused();
        mapFragment.getMapAsync(this);
        return view;
    }

    private void initFused() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildLocationRequest();
        buildLocationCallBack();
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.isMyLocationEnabled();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mDatabase.child("Tachos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Tachos tachos = snapshot.getValue(Tachos.class);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(tachos.getLatitud(), tachos.getLongitud()));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.tacho_general));

                    if (tachos.getTipoTacho().equals("Tacho")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.tacho_azul));

                    } else if (tachos.getTipoTacho().equals("Contenedor")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.tacho_contenedor));
                    }
                    //tmpRealTimeMarkers.clear();
                    //tmpRealTimeMarkers.add(mMap.addMarker(markerOptions));

                    Marker m = mMap.addMarker(markerOptions);
                    m.setTag(tachos); //Añadimos los datos del contenedor al respectivo marcador.

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() { //Capturamos el evento CLICK de un marcador.
                        @Override
                        public boolean onMarkerClick(Marker marker) { //Obtenemos el marcador seleccionado.

                            Tachos infoTacho = (Tachos) marker.getTag(); //Accedemos a los datos del marcador.

                            LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng)); //Situamos el mapa según la posición del marcador.

                            new DialogoContenedor(getContext(), "" + marker.getId(), "" + infoTacho.getTipoTacho(), "" + infoTacho.getLugarDistrito(), "" + infoTacho.getComentario(), "" + infoTacho.getImagenbase64(), infoTacho.getLatitud(), infoTacho.getLongitud());

                            Toast.makeText(getContext(), "" + marker.getId(), Toast.LENGTH_SHORT).show();


                            return true; //Creamos el diálogo pasando los datos.
                        }
                    });
                }
                //realTimeMarkers.clear();
                //realTimeMarkers.addAll(tmpRealTimeMarkers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);


        //buildLocationRequest();
       // buildLocationCallBack();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

    }

    private void buildLocationCallBack() {

        Toast.makeText(getContext(), "Entrando", Toast.LENGTH_SHORT).show();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                location = locationResult.getLocations().get(locationResult.getLocations().size() - 1);
                Log.e("Location2", "" + location.getLatitude() + "" + location.getLongitude());

                Toast.makeText(getContext(), "onLocationResult", Toast.LENGTH_SHORT).show();

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        //OBTENER ULTIMA UBICACION DEL DISPOSITIVO
                        if (location != null) {


                            Toast.makeText(getContext(), "" + location.getLatitude() + location.getLongitude(), Toast.LENGTH_SHORT).show();

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));
                        } else {
                            Toast.makeText(getContext(), "Location Null", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        };
    }

    private void lastPosition() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));

                        }
                    }
                });
    }
    private void buildLocationRequest(){
        locationRequest = new LocationRequest();
        locationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

    //Activar GPS
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("¿El GPS Esta desactivado, te gustaria activarlo?")
                .setCancelable(false)
                .setPositiveButton("Ir a configuración para activar GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "Mi ubicacion", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
