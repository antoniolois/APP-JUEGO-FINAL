package com.example.smash_topo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton boton_login,boton_registro;

    @Override
    public void onBackPressed(){
        //En caso de querer permitir volver atrás usa esta llamada: super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boton_login= findViewById(R.id.boton_cerrar_sesion_menu);
        boton_registro= findViewById(R.id.boton_registro);

        boton_login.setOnClickListener(view -> {// AL HACER CLCIK EN EL BOTÓN REGISTRO SE EJECUTA LA CLASE LOGIN
            Intent intent = new Intent(MainActivity.this, LoginClass.class);
            startActivity(intent);
        });

        boton_registro.setOnClickListener(view -> { // AL HACER CLCIK EN EL BOTÓN REGISTRO SE EJECUTA LA CLASE REGISTRO
            Intent intent = new Intent(MainActivity.this, RegistroClass.class);
            startActivity(intent);
        });




    }

}