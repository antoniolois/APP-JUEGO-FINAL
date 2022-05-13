package com.example.smash_topo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // EL OBJECTIO HANDLER NOS VA A PERMITIR EJECUTAR LAS LINEAS DE CODIGO (DENTRO DEL RUN)EN UN TIEMPO DETERMINADO
        int duracion_pantalla=5000;// 5 segundos
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash.this,MainActivity.class); // (donde se va a ejecutar el codigo , la actividad que se ejcuta después)
            startActivity(intent);
        },duracion_pantalla);
    }
}
