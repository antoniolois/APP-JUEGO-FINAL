package com.example.smash_topo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MapaJuegoClass extends AppCompatActivity {

    @Override
    public void onBackPressed(){
        //En caso de querer permitir volver atrás usa esta llamada: super.onBackPressed();
    }

    //VARIABLES
    String UID, NOMBRE, Topos;
    TextView contadorToposEscenario, nombreJugadorEscenario, tiempoEscenario;
    ImageView topoescenario;

    Random posicionAleatoria;

    //VARIABLES PARA MEDIDA PANTALLA
    int pantalla_ancho;
    int pantalla_alto;

    //VARIABLES PARA GAME OVER
    Dialog dialog;
    boolean GameOverBoolean = false;

    int contadorTopos;
    int timeCount;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_juego);

        // UNIÓN CON LOS ELEMENTOS DEL LAYOUT
        contadorToposEscenario = findViewById(R.id.contadorToposEscenario);
        nombreJugadorEscenario = findViewById(R.id.nombreJugadorEscenario);
        tiempoEscenario = findViewById(R.id.tiempoEscenario);

        topoescenario = findViewById(R.id.topoescenario);

        //INCIO DIALOG
        dialog = new Dialog(MapaJuegoClass.this);


        //RECUPERAMOS DATOS
        Bundle intent = getIntent().getExtras();
        UID = intent.getString("UID");
        NOMBRE = intent.getString("NOMBRE");
        Topos = intent.getString("Topos");

        contadorToposEscenario.setText(Topos);
        nombreJugadorEscenario.setText(NOMBRE);

        ScreenSizes();
        contadorTiempo(20000); //INICIO CON 20 SEGUNDOS


        //CLICK EN EL TOPO
        topoescenario.setOnClickListener(view -> {

            //COMPRUEBO SI NO ES GAME OVER
            if(!GameOverBoolean){
                contadorTopos++;
                contadorTiempo(20000);
                contadorToposEscenario.setText(String.valueOf(contadorTopos)); //le envio el valor del contador en froma de string

                // DIRENTES TOPOS SEGUN EL CONTADOR
                if (contadorTopos>=0){
                    topoescenario.setImageResource(R.drawable.topo_golpeado2); //CAMBIO DE IMAGEN DEL TOPO
                }if (contadorTopos>20){
                    topoescenario.setImageResource(R.drawable.topo_golpeado1); //CAMBIO DE IMAGEN DEL TOPO
                }if (contadorTopos>40){
                    topoescenario.setImageResource(R.drawable.topo_golpeado3); //CAMBIO DE IMAGEN DEL TOPO
                }if (contadorTopos>60){
                    topoescenario.setImageResource(R.drawable.topo_golpeado4); } //CAMBIO DE IMAGEN DEL TOPO


                // INVOCACIÓN DE UN HANDLER PARA VOLVER A LA IMAGEN INICIAL
                new Handler().postDelayed(() -> {

                    topoescenario.setImageResource(R.drawable.topo_normal); //CAMBIO DE IMAGEN DEL TOPO
                    MovimientoTopo(); //colocación del topo

                },500); //TIEMPO EN MILISEGUNDOS EN EL QUE TARDA EN VOLVER
            }

        });
    } //fin onCreate

    // MÉTODO PARA LAS MEDIDAS DE LA PANTALLA
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void ScreenSizes(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point(); //obtener un punto de coordenadas
        display.getRealSize(size);


        pantalla_ancho= size.x;  // ancho absoluto en pixels
        pantalla_alto = size.y;// alto absoluto en pixels



        posicionAleatoria= new Random();
    }

    // MÉTODO PARA MOVER EL TOPO POR LA PANTALLA
    private void MovimientoTopo(){
        int minSize = 200;
        int maxSize_X = pantalla_ancho -  topoescenario.getWidth(); /* MAXIMO TAMAÑO PARA LA COORDENADA DEL EJE X */
        int maxSize_Y = pantalla_alto - topoescenario.getHeight();  /* MAXIMO TAMAÑO PARA LA COORDENADA DEL EJE Y */

        // OBTENCION DE VALORES ALEATORIOS PARA LA COLOCACIÓN
        int randomX = posicionAleatoria.nextInt((maxSize_X-minSize+1)+minSize);
        int randomY = posicionAleatoria.nextInt((maxSize_Y-minSize+1)+minSize);

        topoescenario.setX(randomX);
        topoescenario.setY(randomY);
    }


    // MÉTODO PARA CONTADOR TIEMPO
    public void contadorTiempo(int timeCount){

        new CountDownTimer(timeCount,1000){ //20 - 1 SEGUNDOS
            @Override
            //ejecución por segundo
            public void onTick(long millisUntilFinished) {
                long tiempoRestante = millisUntilFinished/1000;
                tiempoEscenario.setText(String.valueOf(tiempoRestante));

            }

            @Override
            //cuando se acaba el tiempo
            public void onFinish() {
                tiempoEscenario.setText("O");
                GameOverBoolean = true;
                MensajeGameOver();

            }

        }.start();

    }

    private void MensajeGameOver() {

        //DECLARACIÓN
        TextView cantidadToposGameOver;
        Button botonJugarGameOver,botonMenuGameOver,botonPuntuacionesGameOver;

        //CONEXIÓN
        dialog.setContentView(R.layout.game_over_screen);

        //INICIALIZACIÓN
        cantidadToposGameOver = dialog.findViewById(R.id.cantidadToposGameOver);
        botonJugarGameOver = dialog.findViewById(R.id.botonJugarGameOver);
        botonMenuGameOver = dialog.findViewById(R.id.botonMenuGameOver);
        botonPuntuacionesGameOver = dialog.findViewById(R.id.botonPuntuacionesGameOver);

        String toposAplastadosGameOver = String.valueOf(contadorTopos);
        cantidadToposGameOver.setText(toposAplastadosGameOver);

        botonJugarGameOver.setOnClickListener(view -> {
            Intent intent = new Intent(MapaJuegoClass.this, MapaJuegoClass.class);
            startActivity(intent);
        });
        botonMenuGameOver.setOnClickListener(view -> {
            Intent intent = new Intent(MapaJuegoClass.this, MenuClass.class);
            startActivity(intent);
        });

        dialog.show();
    }

}