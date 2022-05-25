package com.example.smash_topo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginClass extends AppCompatActivity {
    // DECLARACIÓN DE VARIABLES
    FirebaseAuth auth;
    EditText editTextTextEmailAddressLogin, editTextTextPasswordLogin;
    Button boton_loginClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    // UNIÓN CON LOS ELEMENTOS DEL LAYOUT
        editTextTextEmailAddressLogin= findViewById(R.id.editTextTextEmailAddressLogin); // CORREO ELECTRONICO
        editTextTextPasswordLogin= findViewById(R.id.editTextTextPasswordLogin); // CONTRASEÑA
        boton_loginClass = findViewById(R.id.boton_login); // BOTON PARA REGISTRARSE
        auth= FirebaseAuth.getInstance();

        // CLICK EN BOTON LOGIN PARA REALIZAR LOGIN
        boton_loginClass.setOnClickListener(view -> {
            // VARIABLES
            String correoElectronico = editTextTextEmailAddressLogin.getText().toString();
            String contraseñaCorreo = editTextTextPasswordLogin.getText().toString();

            // COMPROBACION DE LOS DATOS INTRODUCIDOS
                //CORREO ELECTRONICO
            if (!Patterns.EMAIL_ADDRESS.matcher(correoElectronico).matches()){  // ESTA LINEA COMPRUEBA QUE EL FORMATO DEL CORREO ES EL CORRECTO
                editTextTextEmailAddressLogin.setError("Correo NO válido");
                editTextTextEmailAddressLogin.setFocusable(true); //// ESTA LINEA OBLIGA A ESCRIBIR DE FORMA CORRECTA EL CORREO
                // CONTRASEÑA
            }else if(contraseñaCorreo.length()<5){
                editTextTextPasswordLogin.setError("La contraseña debe contener 5 caracteres");
                editTextTextPasswordLogin.setFocusable(true); //// ESTA LINEA OBLIGA A ESCRIBIR DE FORMA CORRECTA LA CONTRASEÑA
            }else{
                InicioSesionJugador(correoElectronico,contraseñaCorreo);
            }

        });
    }

        // MÉTODO DE INICIO DE SESIÓN DEL JUGADOR
        private void InicioSesionJugador(String correoElectronico, String contraseñaCorreo ){
            auth.signInWithEmailAndPassword(correoElectronico,contraseñaCorreo )
                    .addOnCompleteListener(task -> { // INICIO DE SESION CON CORREO Y CONTRASEÑA
                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    startActivity(new Intent(LoginClass.this,Menu.class));
                    assert user != null;
                    Toast.makeText(LoginClass.this,"BIENVENID@: "+user.getEmail(),Toast.LENGTH_LONG).show();
                    finish();
                }

                    }).addOnFailureListener(e -> { //EN CASO DE ERROR MUESTRA UN MENSAJE
                Toast.makeText(LoginClass.this,""+e.getMessage(),Toast.LENGTH_LONG).show();

                    });
        }

}