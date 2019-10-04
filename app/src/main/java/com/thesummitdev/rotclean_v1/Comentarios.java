package com.thesummitdev.rotclean_v1;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Comentarios extends Fragment {

    View view;
    RatingBar ratingBar;
    TextView tvRatingScale;
    EditText txtComentarioss;
    Button btnSubmit;
    DatabaseReference mDatabaseReference;

    public Comentarios() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        view = inflater.inflate(R.layout.fragment_comentarios,container,false);

        ratingBar = view.findViewById(R.id.ratingBar);
        tvRatingScale = view.findViewById(R.id.tvRatingScale);
        txtComentarioss = view.findViewById(R.id.txtComentarioss);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                builder.setTitle("Enviar Comentario");
                builder.setMessage("¿Está seguro de enviar este comentario?");
                builder.setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: El reporte se envió a la papelera de reciclaje satisfactoriamente.");


                        Map<String,Object> comentarios =new HashMap<>(  );

                        comentarios.put("Score",ratingBar.getRating());
                        comentarios.put("Calificacion", tvRatingScale.getText().toString().trim());
                        comentarios.put("Comentarios",txtComentarioss.getText().toString().trim());

                        mDatabaseReference.child("Comentarios").push().setValue(comentarios).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Log.d("Estado", "Comentario enviado");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d("Estado", "Error al enviar");

                            }
                        });


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
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                tvRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()){
                    case 1 :
                        tvRatingScale.setText("Muy malo");;
                        break;
                    case 2 :
                        tvRatingScale.setText("Necesita alguna mejora");;
                        break;
                    case 3 :
                        tvRatingScale.setText("Bueno");;
                        break;
                    case 4 :
                        tvRatingScale.setText("Excelente");;
                        break;
                    case 5 :
                        tvRatingScale.setText("Increible, me encanta.");;
                        break;

                }

            }
        });





        return view;
    }



}
