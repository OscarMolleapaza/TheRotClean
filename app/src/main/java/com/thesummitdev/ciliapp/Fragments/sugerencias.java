package com.thesummitdev.ciliapp.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class sugerencias extends Fragment {

    FusedLocationProviderClient mFusedLocationClient;

    ImageView imgFotoSug;
    EditText txtReferencia, txtComentario;
    Spinner dialog_spinner_sug;
    Button btnCancel, btnEnviarSug;

    double latitud, longitud;

    Bitmap bitmap;
    View view;

    DatabaseReference mDatabaseReference;


    public sugerencias() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        /*
        view = inflater.inflate(R.layout.fragment_sugerencias,container,false);
        imgFotoSug = view.findViewById(R.id.imgFotoSug);
        txtReferencia = view.findViewById(R.id.txtRefSug);
        txtComentario = view.findViewById(R.id.txtComSug);
        dialog_spinner_sug = view.findViewById(R.id.txtDistSug);
        btnEnviarSug = view.findViewById(R.id.btnEnvSug);



        lastPosition();

        imgFotoSug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);

            }
        });
        btnEnviarSug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatosFirebaseDialogNotDatabase();
            }
        });
        */
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode==1&&resultCode==RESULT_OK&&data!=null){

            bitmap=(Bitmap) data.getExtras().get( "data" );

            imgFotoSug.setImageBitmap( bitmap );
        }else {
            Toast.makeText( getContext(),"Error de data no se obtuvo" ,Toast.LENGTH_SHORT).show();
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
            sugerencia.put("imagenBase64",imageString);
            sugerencia.put("Interseccion",txtReferencia.getText().toString());
            sugerencia.put("Comentarios",txtComentario.getText().toString());
            sugerencia.put("Latitud",latitud);
            sugerencia.put("Longitud",longitud);
            sugerencia.put( "tipoTacho", dialog_spinner_sug.getSelectedItem().toString() );

            mDatabaseReference.child("Sugerencias").push().setValue(sugerencia).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getContext(),"Sugerencia enviada",Toast.LENGTH_LONG).show();

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


    private void lastPosition(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {


                        }
                        Log.d("Latitud",""+location.getLatitude());
                        Log.d("Longitud",""+location.getLongitude());
                        latitud = location.getLatitude();
                        longitud = location.getLongitude();

                        //  lastLat = location.getLatitude();
                        // lastLong = location.getLongitude();
                    }
                });
    }

}
