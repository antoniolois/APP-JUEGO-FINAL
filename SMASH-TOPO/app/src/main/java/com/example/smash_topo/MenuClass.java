package com.example.smash_topo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuClass extends AppCompatActivity {

    //DECLARACION DE VARIABLES
    FirebaseAuth auth;
    FirebaseUser user;
    Button CerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // INSTANCIAS
        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        CerrarSesion = findViewById(R.id.boton_cerrar_sesion_menu);
        CerrarSesion.setOnClickListener(view -> {
            CloseSesion();
        });



    }

    //ESTE MÉTODO SE EJECUTARÁ CUANDO LA APP ESTÉ ABIERTA
    @Override
    protected  void onStart(){
        checkUserLogin();
        super.onStart();

    }
    // MÉTODO PARA COMPROBAR SI EL USUARIO YA INICIÓ SESIÓN Y ASÍ EVITAR QUE VUELVA A TENER QUE INICIARLA
    private void checkUserLogin(){
        if(user != null){
            Toast.makeText(this, "Jugador actualemente logueado", Toast.LENGTH_SHORT).show();

        } else  {
            startActivity(new Intent(MenuClass.this, RegistroClass.class));
            finish(); }

    }

    // MÉTODO PARA CERRAR SESIÓN
    private void CloseSesion(){
        auth.signOut();
        startActivity(new Intent(MenuClass.this, MainActivity.class));
        Toast.makeText(this, "Sesión cerrada exictosamente", Toast.LENGTH_SHORT).show();
         }
    }
