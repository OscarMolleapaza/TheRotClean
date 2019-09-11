package com.thesummitdev.rotclean_v1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MenuOpciones {

    TextView opcion1, opcion2;

    public MenuOpciones(final Context context){
        final Dialog dialogo = new Dialog(context);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(true);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.menu_opciones_reporte);

        opcion1 = dialogo.findViewById(R.id.opcion1);
        opcion2 = dialogo.findViewById(R.id.opcion2);

        opcion1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reportar(context, "opcion1");
            }
        });
        opcion2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reportar(context, "opcion2");
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
