package com.example.smash_topo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    // DECLARACIÓN DE VARIABLES
    EditText editTextTextEmailAddress, editTextTextPassword, editTextTextPersonName;
    TextView fecha_registro;
    Button boton_registar;

    FirebaseAuth fireBaseAutenticacion; // AUTENTICACIÓN DE FIREBASE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        editTextTextEmailAddress= findViewById(R.id.editTextTextEmailAddress); // CORREO ELECTRONICO
        editTextTextPassword= findViewById(R.id.editTextTextPassword); // CONTRASEÑA
        editTextTextPersonName= findViewById(R.id.editTextTextPersonName); // NOMBRE
        fecha_registro= findViewById(R.id.fecha_registro); // FECHA DE REGISTRO
        boton_registar= findViewById(R.id.boton_registar); // BOTON PARA REGISTRARSE

        fireBaseAutenticacion = FirebaseAuth.getInstance();

        // CON EL SIGUIENTE CÓDIGO SE OBTIENE LA FECHA ACTUAL, Y SE REGISTRA AL USUARIO CON ELLA
        Date fechaActual = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy"); // DIA - MES - AÑO
        String fechaActualFinal = dateFormat.format(fechaActual);
        fecha_registro.setText("Fecha actual:\n"+fechaActualFinal);


        // CLICK EN BOTON REGISTRASE PARA REALIZAR REGISTRO
        boton_registar.setOnClickListener(view -> {
            // VARIABLES
            String correoElectronico = editTextTextEmailAddress.getText().toString();
            String contraseñaCorreo = editTextTextPassword.getText().toString();
            String nombreUsuario = editTextTextPersonName.getText().toString();

        // COMPROBACION DE LOS DATOS INTRODUCIDOS

            //CORREO ELECTRONICO
            if (!Patterns.EMAIL_ADDRESS.matcher(correoElectronico).matches()){  // ESTA LINEA COMPRUEBA QUE EL FORMATO DEL CORREO ES EL CORRECTO
                editTextTextEmailAddress.setError("Correo NO válido");
                editTextTextEmailAddress.setFocusable(true); //// ESTA LINEA OBLIGA A ESCRIBIR DE FORMA CORRECTA EL CORREO

            // CONTRASEÑA
            }else if(contraseñaCorreo.length()<5){
                editTextTextPassword.setError("La contraseña debe contener 5 caracteres");
                editTextTextPassword.setFocusable(true); //// ESTA LINEA OBLIGA A ESCRIBIR DE FORMA CORRECTA LA CONTRASEÑA
            }else{
                RegistrarUsuario(correoElectronico,contraseñaCorreo,nombreUsuario);
            }

        });



    }

    // METODO QUE REGISTRARA A UN JUGADOR CON LOS DATOS INTRODUCIDOS
    private void RegistrarUsuario(String correoElectronico, String contraseñaCorreo, String nombreUsuario) {
        fireBaseAutenticacion.createUserWithEmailAndPassword(correoElectronico,contraseñaCorreo)
                .addOnCompleteListener(task -> { // EN CASO DE QUE EL REGISTRO SE EFECTUE DE MANERA CORRECTA SE MUESTRA UN MENSAJE EN PANTALLA
                    if(task.isSuccessful()) {
                        FirebaseUser user = fireBaseAutenticacion.getCurrentUser(); //registro del usuario actual

                        int contadorTopos = 0;
                        assert user != null; // COMPRUEBA QUE EL USUARIO NO SEA NULO
                        String uidStringUser = user.getUid();
                        String correoStringUser = editTextTextEmailAddress.getText().toString();
                        String contraseñaStringUser = editTextTextPassword.getText().toString();
                        String nombreStringUser = editTextTextPersonName.getText().toString();
                        String fechaStringUser = fecha_registro.getText().toString();

                        HashMap<Object, Object> DatosPLAYER = new HashMap<>(); // ESTO PERMITE ASIGNAR CLAVES A LOS VALORES PARA PODER ENVIARLO A FIREBASE
                        DatosPLAYER.put("UID", uidStringUser);
                        DatosPLAYER.put("CORREO ELECTRONICO", correoStringUser);
                        DatosPLAYER.put("CONTRASEÑA", contraseñaStringUser);
                        DatosPLAYER.put("NOMBRE", nombreStringUser);
                        DatosPLAYER.put("FECHA", fechaStringUser);
                        DatosPLAYER.put("Topos", contadorTopos);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("DATABASE JUGADORES REGISTRADOS");
                        reference.child(uidStringUser).setValue(DatosPLAYER); //almacenamiento de los datos del jugador
                        startActivity(new Intent(Registro.this,Menu.class));
                        Toast.makeText(Registro.this, "USUARIO REGISTRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(Registro.this, "Error", Toast.LENGTH_LONG).show();
                    }


                }).addOnFailureListener(e -> { // EN CASO DE QUE EL REGISTRO SE EFECTUE DE MANERA INCORRECTA SE MUESTRA UN MENSAJE EN PANTALLA
            Toast.makeText(Registro.this, "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }


}