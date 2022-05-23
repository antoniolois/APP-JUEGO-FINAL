package com.example.smash_topo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

public class LoginClass extends AppCompatActivity {

    EditText editTextTextEmailAddressLogin, editTextTextPasswordLogin;
    Button boton_loginClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextTextEmailAddressLogin= findViewById(R.id.editTextTextEmailAddressLogin); // CORREO ELECTRONICO
        editTextTextPasswordLogin= findViewById(R.id.editTextTextPasswordLogin); // CONTRASEÑA
        boton_loginClass = findViewById(R.id.boton_login); // BOTON PARA REGISTRARSE

        // CLICK EN BOTON REGISTRASE PARA REALIZAR REGISTRO
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
                //InicioSesionUsuario(correoElectronico,contraseñaCorreo);
            }

        });

    }
}