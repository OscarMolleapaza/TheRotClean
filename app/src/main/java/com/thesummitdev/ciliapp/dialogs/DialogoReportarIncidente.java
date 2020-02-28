package com.thesummitdev.ciliapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.thesummitdev.ciliapp.MenuOpciones;
import com.thesummitdev.ciliapp.R;
import com.thesummitdev.ciliapp.views.MapsActivity;
import java.util.HashMap;
import java.util.Map;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class DialogoReportarIncidente extends AppCompatActivity {

    Button btnReportar, btnCancelar;
    ImageView imgFotoInc;
    TextView lblMotivo, lblConfirmacion;
    EditText txtComentario;
    Context context;
    Bitmap bitmap;

    public DialogoReportarIncidente(final Context context, String idTacho, final String tipo_tacho, String nombre, String comentario, String urlImagen, final Double latitud, final Double longitud){
        final Dialog dialogo = new Dialog(context);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(true);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.dialog_reportar_incidente);

        this.context = context;

        lblMotivo       = dialogo.findViewById(R.id.lblMotivo);
        lblConfirmacion   = dialogo.findViewById(R.id.lblConfirmacion);
        txtComentario = dialogo.findViewById(R.id.txtComentario);

        btnReportar     = dialogo.findViewById(R.id.btnReportar);
        btnCancelar  = dialogo.findViewById(R.id.btnCancelar);

        imgFotoInc   = dialogo.findViewById(R.id.imgFotoInc);

        lblMotivo.setText("Motivo: "+nombre);
        lblConfirmacion.setText("¿Está seguro de reportar este "+tipo_tacho+"?");

        /*byte[] imageAsBytes = Base64.decode(urlImagen.getBytes(), 0); //Interpretamos la URL Base64
        imgFotoInc.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));*/

        imgFotoInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"La funcionalidad de agregar imágenes estará disponible en una próxima actualización.",Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                MapsActivity.activity.startActivityForResult( intent,2 );*/
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        btnReportar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle("¿Está seguro de reportar este tacho?");
                builder.setMessage("Motivo:.\n\n(Si realiza el reporte, su municipalidad correspondiente se encargará de verificarlo.)");
                builder.setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        Toast.makeText(context,"\"El incidente se reportó satisfactoriamente.\"",Toast.LENGTH_SHORT).show();

                        /*Map<String,Object> reporte =new HashMap<>(  );

                        reporte.put("tipo_reporte",accion.trim());
                        reporte.put("tipo",tipo.trim());
                        reporte.put("latitud",lat);
                        reporte.put("longitud",lon);

                        mDatabaseReference.child("Reportes").push().setValue(reporte).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(context,"Reporte enviado! Gracias por contribuir.",Toast.LENGTH_LONG).show();

                                Log.d("Estado", "Reporte enviado");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d("Estado", "Error al enviar");
                            }
                        });


                        Log.d("Datos",tipo+lat+lon);*/
                    }
                });

                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

        dialogo.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("RESULTADO","requestcode: "+requestCode);

        if (requestCode == 2 && resultCode == RESULT_OK){
            bitmap=(Bitmap) data.getExtras().get( "data" );
            imgFotoInc.setImageBitmap( bitmap );
        }
    }
}
