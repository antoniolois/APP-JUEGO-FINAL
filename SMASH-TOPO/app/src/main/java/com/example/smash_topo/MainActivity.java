package com.example.smash_topo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton boton_login,boton_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boton_login= findViewById(R.id.boton_login);
        boton_registro= findViewById(R.id.boton_registro);

        boton_login.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Click realizado en login", Toast.LENGTH_SHORT).show(); // MUESTRA UN MENSAJE EN PANTALLA
        });

        boton_registro.setOnClickListener(view -> { // AL HACER CLCIK EN EL BOTÓN REGISTRO SE EJECUTA LA CLASE REGISTRO
            Intent intent = new Intent(MainActivity.this,Registro.class);
            startActivity(intent);
        });




    }

}