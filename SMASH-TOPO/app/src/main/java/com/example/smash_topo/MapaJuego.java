package com.example.smash_topo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MapaJuego extends AppCompatActivity {

    //VARIABLES
    String UID, NOMBRE, Topos;
    TextView contadorEscenario, nombreJugadorEscenario, tiempoEscenario;
    ImageView topoescenario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_juego);

        contadorEscenario = findViewById(R.id.contadorEscenario);
        nombreJugadorEscenario = findViewById(R.id.nombreJugadorEscenario);
        tiempoEscenario = findViewById(R.id.tiempoEscenario);

        topoescenario = findViewById(R.id.topoescenario);

        //RECUPERAMOS DATOS
        Bundle intent = getIntent().getExtras();
        UID = intent.getString("UID");
        NOMBRE = intent.getString("NOMBRE");
        Topos = intent.getString("Topos");

        contadorEscenario.setText(Topos);
        nombreJugadorEscenario.setText(NOMBRE);
    }
}