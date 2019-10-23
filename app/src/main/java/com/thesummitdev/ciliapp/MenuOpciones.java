package com.thesummitdev.ciliapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MenuOpciones {

    TextView opcion1, opcion2, opcion3;

    String tipo;
    Double lat,lon;
    DatabaseReference mDatabaseReference;

    public MenuOpciones(final Context context, String tipo_reporte,Double latitud, Double longitud){
        final Dialog dialogo = new Dialog(context);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(true);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.menu_opciones_reporte);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        opcion1 = dialogo.findViewById(R.id.opcion1);
        opcion2 = dialogo.findViewById(R.id.opcion2);
        opcion3 = dialogo.findViewById(R.id.opcion3);

        tipo = tipo_reporte;
        lat = latitud;
        lon = longitud;





        opcion1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reportar(context, opcion1.getText().toString());//El contenedor no se encuentra.
            }
        });
        opcion2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reportar(context, opcion2.getText().toString()); // El contenedor esta en mal estado
            }
        });
        opcion3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reportar(context, opcion3.getText().toString()); // El contenedor esta colapsado
            }
        });

        dialogo.show();
    }

    public void reportar(final Context context, final String accion){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setTitle("¿Está seguro de reportar este tacho?");
        builder.setMessage("Motivo: "+accion+".\n\n(Si realiza el reporte, su municipalidad correspondiente se encargará de verificarlo.)");
        builder.setPositiveButton("Si",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: El reporte se envió a la papelera de reciclaje satisfactoriamente.");
                /*if(accion.equals("opcion1")) {  //Este bloque se encarga de registrar el reporte.




                } else {

                }*/
                Map<String,Object> reporte =new HashMap<>(  );

                reporte.put("tipo_reporte",accion.trim());
                reporte.put("tipo",tipo.trim());
                reporte.put("latitud",lat);
                reporte.put("longitud",lon);

                mDatabaseReference.child("Reportes").push().setValue(reporte).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Log.d("Estado", "Reporte enviado");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("Estado", "Error al enviar");

                    }
                });


                Log.d("Datos",tipo+lat+lon);

            }
        });

        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: Gracias por ahorrarnos el trabajo.");
                dialog.cancel();
            }
        });

        builder.show();
    }
}
