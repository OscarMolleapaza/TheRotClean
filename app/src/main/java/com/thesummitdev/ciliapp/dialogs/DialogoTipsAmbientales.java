package com.thesummitdev.ciliapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.thesummitdev.ciliapp.R;

import java.util.Random;

public class DialogoTipsAmbientales {

    TextView lblTip, lblNroTip, btnOk;
    ImageView imgTip;

    String[] tips;
    private static final Integer[] tips_imagenes = {R.drawable.eco_1, R.drawable.eco_2, R.drawable.eco_3, R.drawable.eco_4, R.drawable.eco_5, R.drawable.eco_6, R.drawable.eco_7, R.drawable.eco_8};

    public DialogoTipsAmbientales(final Context context) {

        final Dialog dialogo = new Dialog(context);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(true);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.getWindow().getAttributes().windowAnimations = R.style.DialogScale;
        dialogo.setContentView(R.layout.dialog_tips_ambientales);

        final MediaPlayer tipSoundMP = MediaPlayer.create(context, R.raw.tip_sound);
        tipSoundMP.start();

        lblTip = dialogo.findViewById(R.id.lblTip);
        lblNroTip = dialogo.findViewById(R.id.lblNroTip);
        imgTip = dialogo.findViewById(R.id.imgTip);
        btnOk = dialogo.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        Resources res = context.getResources();
        tips = res.getStringArray(R.array.tips_array);

        int indice = new Random().nextInt(tips.length);
        lblTip.setText(tips[indice]);
        lblNroTip.setText("Tip número "+(indice+1));

        indice = new Random().nextInt(tips_imagenes.length);
        imgTip.setImageResource(tips_imagenes[indice]);

        dialogo.show();
    }
}
