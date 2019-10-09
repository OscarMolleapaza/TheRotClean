package com.thesummitdev.rotclean_v1.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thesummitdev.rotclean_v1.R;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {


    EditText txtNombre, txtApellido, txtUsuario,txtContraseña;
    Spinner spnTipoUser;
    Button btnRegistrar;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        txtNombre = (EditText)findViewById(R.id.txtName);
        txtApellido = (EditText)findViewById(R.id.txtLast);
        txtUsuario = (EditText)findViewById(R.id.txtUser);
        txtContraseña = (EditText)findViewById(R.id.txtPass);
        spnTipoUser = (Spinner)findViewById(R.id.type_user);
        btnRegistrar = (Button)findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });




    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user){
        if(user != null){


           // Intent intent = new Intent(Register.this,MapsActivity.this);
            //startActivity(intent);
        }

    }


    public void registrar(){



        final String name = txtNombre.getText().toString().trim();
        final String last = txtApellido.getText().toString().trim();
        String pass = txtContraseña.getText().toString().trim();
        final String user_email = txtUsuario.getText().toString().trim();
        final String type = spnTipoUser.getSelectedItem().toString().trim();

        mAuth.createUserWithEmailAndPassword(user_email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            Map<String,Object> users = new HashMap<>();
                            users.put("Nombre",name);
                            users.put("Apellidos",last);
                            users.put("email",user_email);
                            users.put("tipo",type);

                            mDatabase.child("Usuarios").child(task.getResult().getUser().getUid()).setValue(users);

                            //updateUI(user);



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
            }
        });
    }
}
