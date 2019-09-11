package com.thesummitdev.rotclean_v1;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DialogoContenedor {

    Button btnOk, btnReport, btnClose;
    ImageView imgFoto;
    TextView lblTipo, lblDistrito, lblComentario;

    public DialogoContenedor(final Context context, String tipo, String distrito, String comentario, String urlImagen){
        final Dialog dialogo = new Dialog(context);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(true);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.dialog_contenedor);

        lblTipo       = dialogo.findViewById(R.id.lblTipo);
        lblDistrito   = dialogo.findViewById(R.id.lblDistrito);
        lblComentario = dialogo.findViewById(R.id.lblComentario);

        btnOk     = dialogo.findViewById(R.id.btnOk);
        btnClose  = dialogo.findViewById(R.id.btnClose);
        btnReport = dialogo.findViewById(R.id.btnReport);
        imgFoto   = dialogo.findViewById(R.id.imgFoto);

        lblTipo.setText(tipo);
        lblDistrito.setText("Distrito: "+distrito);
        lblComentario.setText(comentario);

        byte[] imageAsBytes = Base64.decode(urlImagen.getBytes(), 0); //Interpretamos la URL Base64
        imgFoto.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        dialogo.show();


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new MenuOpciones(context);
            }
        });
    }
}
