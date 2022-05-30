package com.example.smash_topo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MapaJuego extends AppCompatActivity {

    //VARIABLES
    String UID, NOMBRE, Topos;
    TextView contadorToposEscenario, nombreJugadorEscenario, tiempoEscenario;
    ImageView topoescenario;

    int contadorTopos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_juego);

        // UNIÓN CON LOS ELEMENTOS DEL LAYOUT
        contadorToposEscenario = findViewById(R.id.contadorToposEscenario);
        nombreJugadorEscenario = findViewById(R.id.nombreJugadorEscenario);
        tiempoEscenario = findViewById(R.id.tiempoEscenario);

        topoescenario = findViewById(R.id.topoescenario);

        //RECUPERAMOS DATOS
        Bundle intent = getIntent().getExtras();
        UID = intent.getString("UID");
        NOMBRE = intent.getString("NOMBRE");
        Topos = intent.getString("Topos");

        contadorToposEscenario.setText(Topos);
        nombreJugadorEscenario.setText(NOMBRE);

        //CLICK EN EL TOPO
        topoescenario.setOnClickListener(view -> {
            contadorTopos++;
            contadorToposEscenario.setText(String.valueOf(contadorTopos)); //le envio el valor del contador en froma de string
        });





    }
}