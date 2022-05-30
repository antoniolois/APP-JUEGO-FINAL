package com.example.smash_topo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MapaJuego extends AppCompatActivity {

    //VARIABLES
    String UID, NOMBRE, Topos;
    TextView contadorToposEscenario, nombreJugadorEscenario, tiempoEscenario;
    ImageView topoescenario;

    Random posicionAleatoria;

    //VARIABLES PARA MEDIDA PANTALLA
    int pantalla_ancho;
    int pantalla_alto;

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

        ScreenSizes();

        //CLICK EN EL TOPO
        topoescenario.setOnClickListener(view -> {
            contadorTopos++;
            contadorToposEscenario.setText(String.valueOf(contadorTopos)); //le envio el valor del contador en froma de string

            // DIRENTES TOPOS SEGUN EL CONTADOR
            if (contadorTopos>=0){
                topoescenario.setImageResource(R.drawable.topo_golpeado2); //CAMBIO DE IMAGEN DEL TOPO
            }if (contadorTopos>20){
                topoescenario.setImageResource(R.drawable.topo_golpeado1); //CAMBIO DE IMAGEN DEL TOPO
            }if (contadorTopos>40){
                topoescenario.setImageResource(R.drawable.topo_golpeado3); //CAMBIO DE IMAGEN DEL TOPO
            }if (contadorTopos>60){
                topoescenario.setImageResource(R.drawable.topo_golpeado4); //CAMBIO DE IMAGEN DEL TOPO
            }
            // INVOCACIÓN DE UN HANDLER PARA VOLVER A LA IMAGEN INICIAL
            new Handler().postDelayed(() -> {
                topoescenario.setImageResource(R.drawable.topo_normal); //CAMBIO DE IMAGEN DEL TOPO
                MovimientoTopo(); //colocación del topo
            },300); //TIEMPO EN MILISEGUNDOS EN EL QUE TARDA EN VOLVER
        });
    } //fin onCreate

    // MÉTODO PARA LAS MEDIDAS DE LA PANTALLA
    private void ScreenSizes(){
        Display screen = getWindowManager().getDefaultDisplay(); //con esta linea obtengo el tamaño del dispositivo
        Point pointScreen = new Point(); //obtener un punto de coordenadas
        screen.getSize(pointScreen);

        pantalla_ancho=pointScreen.x; //asignación del eje x al ancho
        pantalla_alto=pointScreen.y; //asignación del eje y al alto

        posicionAleatoria= new Random();

    }

    // MÉTODO PARA MOVER EL TOPO POR LA PANTALLA
    private void MovimientoTopo(){
        int minSize = 0;
        int maxSize_X = pantalla_ancho-topoescenario.getWidth() ; /* MAXIMO TAMAÑO PARA LA COORDENADA DEL EJE X */
        int maxSize_Y = pantalla_alto-topoescenario.getHeight();  /* MAXIMO TAMAÑO PARA LA COORDENADA DEL EJE Y */

        // OBTENCION DE VALORES ALEATORIOS PARA LA COLOCACIÓN
        int randomX = posicionAleatoria.nextInt(((maxSize_X-minSize)+1)+minSize);
        int randomY = posicionAleatoria.nextInt(((maxSize_Y-minSize)+1)+minSize);

        topoescenario.setX(randomX);
        topoescenario.setY(randomY);
    }
}