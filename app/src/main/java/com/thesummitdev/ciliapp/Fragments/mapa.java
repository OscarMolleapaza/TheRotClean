package com.thesummitdev.ciliapp.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thesummitdev.ciliapp.DialogoContenedor;
import com.thesummitdev.ciliapp.R;
import com.thesummitdev.ciliapp.Modelos.Tachos;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class mapa extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener, LocationListener, View.OnClickListener {
    private final int GPS=51;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private DatabaseReference mDatabase; //FIREBASE
    FusedLocationProviderClient fusedLocationProviderClient; //Ultima Ubicacion
    LocationRequest locationRequest; //Actualizar posicion
    LocationCallback locationCallback; //ACtualizar posicion
    Location location;

    FloatingActionButton sugerir;

    ProgressDialog progressDialog;
    ImageView imgFotoSug;
    EditText txtReferencia, txtComentario;
    Spinner dialog_spinner_sug;
    Button btnCancel, btnEnviarSug;
    Bitmap bitmap;
    public final int LOCATION = 1;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationManager locationManager;

    Double latitude;
    Double longitud;
    Dialog dialog;
    public boolean isGPSEnabled = false;

    public mapa() {
        // Required empty public constructor
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAttach(Context context) {
        permiso();

        super.onAttach(context);

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

        sugerir = (FloatingActionButton)view.findViewById(R.id.buttomSugerir);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();

        }
        sugerir.setOnClickListener(this);

        gpsEnable();
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Instanciar BD Firebase
        mapFragment.getMapAsync(this);
        return view;

    }

    private void gpsEnable() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( getContext() );
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval( 10000 );
        locationRequest.setFastestInterval( 5000 );
        locationRequest.setPriority( locationRequest.PRIORITY_HIGH_ACCURACY );
        LocationSettingsRequest.Builder builder= new LocationSettingsRequest.Builder().addLocationRequest( locationRequest );
        SettingsClient settingsClient =LocationServices.getSettingsClient( getActivity() );
        Task<LocationSettingsResponse> task= settingsClient.checkLocationSettings( builder.build() );
        task.addOnSuccessListener( new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        } );
        task.addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    ResolvableApiException resolvableApiException= (ResolvableApiException) e;
                    try {
                        resolvableApiException.startResolutionForResult( getActivity(),GPS );
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } );
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
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.tacho_fin));

                    } else if (tachos.getTipoTacho().equals("Contenedor")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.contenedor_fin));
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

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
        latitude = location.getLatitude();
        longitud = location.getLongitude();

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



    public void Update(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(mapa.this).attach(mapa.this).commit();

    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void permiso(){
        if (CheckPermission(Manifest.permission.ACCESS_FINE_LOCATION)){

        }else{
            //No se le pregunto aun
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else{
                new AlertDialog.Builder(getContext())
                        .setTitle("Permiso")
                        .setMessage("Necesitamos permiso para acceder a su ubicacion.")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);

                                Toast.makeText( getContext(),"Funcionamiento gps con normalidad" ,Toast.LENGTH_SHORT).show();
                                Update();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(),"Como chuchas vamos a ver tu ubicacion entonces, me vale madres igual, lo tomare como un si",Toast.LENGTH_LONG).show();
                            }
                        })
                        .create()
                        .show();


            }
        }
    }
    private boolean CheckPermission(String permiso){
        int result= getActivity().checkCallingOrSelfPermission(permiso);
        return result== PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                String permission= permissions[0];
                int result= grantResults[0];
                if(permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)){
                    if (result==PackageManager.PERMISSION_GRANTED){
                        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) return;
                        Update();
                    }else{
                        Toast.makeText(getContext(),"Denegaste el permiso",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Denegaste el permiso x2",Toast.LENGTH_LONG).show();
                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener( new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()){
                            location= task.getResult();
                            if(location!=null){
                                mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( location.getLatitude(),location.getLongitude() ),18) );

                            }else {
                                LocationRequest locationRequest= LocationRequest.create();
                                locationRequest.setInterval( 10000 );
                                locationRequest.setFastestInterval( 5000 );
                                locationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );

                                locationCallback= new LocationCallback(){
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult( locationResult );
                                        if (locationResult==null)
                                            return;
                                        location = locationResult.getLastLocation();

                                        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( location.getLatitude(),location.getLongitude() ),18) );

                                        fusedLocationProviderClient.removeLocationUpdates( locationCallback );
                                    }
                                };

                                fusedLocationProviderClient.requestLocationUpdates( locationRequest,locationCallback,null );

                            }

                        }else{
                            Toast.makeText( getContext(),"Unable to get last lcoation" ,Toast.LENGTH_SHORT).show();
                        }
                    }
                } );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS:
                if (resultCode == RESULT_OK) {

                    getDeviceLocation();

                } else {
                    Toast.makeText(getContext(), "Error de gps", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        if (requestCode==1&&resultCode==RESULT_OK&&data!=null){

            bitmap=(Bitmap) data.getExtras().get( "data" );

            imgFotoSug.setImageBitmap( bitmap );
        }else {
            Toast.makeText( getContext(),"Error de data no se obtuvo" ,Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

        case R.id.buttomSugerir:
            dialog =new Dialog( getContext() );
            dialog.setContentView( R.layout.dialog_sugerencia );
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT ) );
            //findviewid


            imgFotoSug = (ImageView) dialog.findViewById(R.id.imgFotoSug);
            txtReferencia = (EditText) dialog.findViewById(R.id.txtRefSug);
            txtComentario = (EditText) dialog.findViewById(R.id.txtComSug);
            dialog_spinner_sug = (Spinner) dialog.findViewById(R.id.txtDistSug);
            btnEnviarSug = (Button) dialog.findViewById(R.id.btnEnvSug);
            btnCancel = (Button) dialog.findViewById(R.id.btnCancSug);

            //on click

            imgFotoSug.setOnClickListener( this );
            btnEnviarSug.setOnClickListener( this );
            btnCancel.setOnClickListener( this );

            dialog.show();


        break;

            case R.id.btnCancSug:
                dialog.dismiss();
                break;
            case R.id.btnEnvSug:
                guardarDatosFirebaseDialogNotDatabase();
                dialog.dismiss();
                break;
            case  R.id.imgFotoSug:
                Intent intent= new Intent( );
                intent.setAction(  MediaStore.ACTION_IMAGE_CAPTURE );
                startActivityForResult( intent,1 );
                break;

        }
    }
    private void guardarDatosFirebaseDialogNotDatabase()  {


        Map<String,Object> sugerencia =new HashMap<>(  );


        if(bitmap!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress( Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //encode
            String sugerenciaa_id = mDatabase.push().getKey();

            sugerencia.put("imagenBase64",imageString);
            sugerencia.put("Interseccion",txtReferencia.getText().toString());
            sugerencia.put("Comentarios",txtComentario.getText().toString());
            sugerencia.put("Latitud",location.getLatitude());
            sugerencia.put("Longitud",location.getLongitude());
            sugerencia.put( "Distrito", dialog_spinner_sug.getSelectedItem().toString() );
            sugerencia.put("sugerencia_id",sugerenciaa_id);

            mDatabase.child("Sugerencias").child(sugerenciaa_id ).setValue(sugerencia).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getContext(),"Sugerencia enviada",Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getContext(),"Error al enviar.",Toast.LENGTH_SHORT).show();

                }
            });


        }else {
            Toast.makeText(  getContext(),"Error Bitmap vacio",Toast.LENGTH_SHORT ).show();
        }
    }

}
