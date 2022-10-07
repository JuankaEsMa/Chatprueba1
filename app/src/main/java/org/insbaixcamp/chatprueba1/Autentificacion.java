package org.insbaixcamp.chatprueba1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Autentificacion extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText tiNameAutentificacion;
    private TextInputEditText tiMailAutentificacion;
    private TextInputEditText tiPasswordAutentificacion;
    private Button btSignIn;
    private Button btLogIn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    static private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentificacion);


        btSignIn = findViewById(R.id.btSignIn);
        btLogIn = findViewById(R.id.btLogIn);
        tiNameAutentificacion = findViewById(R.id.tiNameAutentificacion);
        tiMailAutentificacion = findViewById(R.id.tiMailAutentificacion);
        tiPasswordAutentificacion = findViewById(R.id.tiPasswordAutentificacion);
        btLogIn.setOnClickListener(this);
        btSignIn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();


    }

    public void reload(){
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
                usuario = new Usuario(tiNameAutentificacion.getText().toString(), tiMailAutentificacion.getText().toString());

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
        }
    }


    @Override
    public void onClick(View v) {
        if(tiNameAutentificacion.getText().toString() != null ||
                tiMailAutentificacion.getText().toString() != null ||
                tiPasswordAutentificacion.toString() != null) {
            if (v.getId() == btLogIn.getId()) {
                mAuth.createUserWithEmailAndPassword(tiMailAutentificacion.getText().toString(),
                        tiPasswordAutentificacion.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error al Iniciar sesion", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else if (v.getId() == btSignIn.getId()) {
                mAuth.signInWithEmailAndPassword(tiMailAutentificacion.getText().toString(), tiPasswordAutentificacion.getText().toString());
            }
            reload();
        } else {
            Toast.makeText(this, "Porfavor rellene todos los campos de texto", Toast.LENGTH_LONG);
        }
    }

    public static Usuario getUsuario() {
        return usuario;
    }
}