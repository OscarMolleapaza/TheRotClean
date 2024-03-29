package com.thesummitdev.ciliapp.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thesummitdev.ciliapp.R;
import com.thesummitdev.ciliapp.views.Register;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class admin extends Fragment {


    private FirebaseAuth mAuth;

    EditText txtUsuario, txtContraseña;
    Button btnIngresar;
     ProgressDialog progressDoalog;
    public admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin, container, false);


        txtUsuario = view.findViewById(R.id.txtUsuario);
        txtContraseña = view.findViewById(R.id.txtContraseña);
        btnIngresar = view.findViewById(R.id.btnIngresar);



        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDoalog = new ProgressDialog(getContext());
                progressDoalog.setMessage("Cargando...");
                progressDoalog.setTitle("Subiendo Datos");
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                // show it
                progressDoalog.show();
                if(txtContraseña!=null&&txtUsuario!=null){
                    logearUsuario();
                }else{
                    Toast.makeText(getContext(), "Completa los campos por favor", Toast.LENGTH_LONG).show();
                }



            }
        });


        mAuth = FirebaseAuth.getInstance();


        return view;

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
       FirebaseUser currentUser = mAuth.getCurrentUser();
       updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            Intent intent = new Intent(getActivity(), Register.class);
            startActivity(intent);

        }

    }


    private void logearUsuario(){

        String email = txtUsuario.getText().toString().trim();
        String password = txtContraseña.getText().toString().trim();

        if(email.matches("")&& password.matches("")){
            Toast.makeText(getContext(),"Ingrese datos", Toast.LENGTH_LONG).show();

        }else if(email.matches("") || password.matches("")){
            Toast.makeText(getContext(),"Usuario o Contraseña en blanco", Toast.LENGTH_LONG).show();

        }

        else{


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                progressDoalog.dismiss();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }


    }
}
