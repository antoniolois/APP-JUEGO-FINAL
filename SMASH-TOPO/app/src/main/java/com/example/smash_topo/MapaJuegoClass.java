package com.example.smash_topo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class MapaJuegoClass extends AppCompatActivity {


    //VARIABLES
    String UID, NOMBRE, Topos;
    TextView contadorToposEscenario, nombreJugadorEscenario, tiempoEscenario;
    ImageView topoescenario;



    //VARIABLES PARA MEDIDA PANTALLA
    int pantalla_ancho;
    int pantalla_alto;

    //VARIABLES PARA GAME OVER
    Dialog dialog;
    boolean GameOverBoolean = false;

    //TOPOS
    Random posicionAleatoria;
    int contadorTopos;

    //FIREBASE
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseJugadores;



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

        //INICIALIZACION
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser(); //para obtener los datos del usuario que inicia sesion
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseJugadores= firebaseDatabase.getReference("DATABASE JUGADORES REGISTRADOS");

        //RECUPERAMOS DATOS
        Bundle intent = getIntent().getExtras();
        UID = intent.getString("UID");
        NOMBRE = intent.getString("NOMBRE");
        Topos = intent.getString("Topos");

        contadorToposEscenario.setText(Topos);
        nombreJugadorEscenario.setText(NOMBRE);

        ScreenSizes();
        contadorTiempo(30000); //INICIO CON 20 SEGUNDOS


        //CLICK EN EL TOPO
        topoescenario.setOnClickListener(view -> {

            //COMPRUEBO SI NO ES GAME OVER
            if(!GameOverBoolean){
                contadorTopos++;
                contadorToposEscenario.setText(String.valueOf(contadorTopos)); //le envio el valor del contador en froma de string

                // DIRENTES TOPOS SEGUN EL CONTADOR
                if (contadorTopos>=0){
                    topoescenario.setImageResource(R.drawable.topo_golpeado2); //CAMBIO DE IMAGEN DEL TOPO
                }if (contadorTopos>10){
                    topoescenario.setImageResource(R.drawable.topo_golpeado1); //CAMBIO DE IMAGEN DEL TOPO
                }if (contadorTopos>20){
                    topoescenario.setImageResource(R.drawable.topo_golpeado3); //CAMBIO DE IMAGEN DEL TOPO
                }if (contadorTopos>30){
                    topoescenario.setImageResource(R.drawable.topo_golpeado4); } //CAMBIO DE IMAGEN DEL TOPO


                // INVOCACIÓN DE UN HANDLER PARA VOLVER A LA IMAGEN INICIAL
                new Handler().postDelayed(() -> {
                    topoescenario.setImageResource(R.drawable.topo_normal); //CAMBIO DE IMAGEN DEL TOPO
                    MovimientoTopo(); //colocación del topo
                },100); //TIEMPO EN MILISEGUNDOS EN EL QUE TARDA EN VOLVER
            }

        });
    } //fin onCreate

    //método para guardar los datos que obtenga el jugador
    public void guadrarDatosPlayer(String key, int cantidadTopos){
        HashMap<String,Object> hashMap = new HashMap<>(); //envio de datos a la base de datos de firevbase
        hashMap.put(key,cantidadTopos); //nombre del valor en la base de datos || valor en la base de datos
        databaseJugadores.child(firebaseUser.getUid()).updateChildren(hashMap).addOnCompleteListener(task -> {
            Toast.makeText(MapaJuegoClass.this,"PUNTUACIÓN ACTUALIZADA",Toast.LENGTH_SHORT).show();
        }); //actualizacion de datos
    }



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

        int minSize = 10;
        int maxSize_X = pantalla_ancho -  topoescenario.getWidth()-100; /* MAXIMO TAMAÑO PARA LA COORDENADA DEL EJE X */
        int maxSize_Y = pantalla_alto - topoescenario.getHeight()-800;  /* MAXIMO TAMAÑO PARA LA COORDENADA DEL EJE Y */

        // OBTENCION DE VALORES ALEATORIOS PARA LA COLOCACIÓN
        int randomX = posicionAleatoria.nextInt((maxSize_X-minSize+1)+minSize);
        int randomY = posicionAleatoria.nextInt((maxSize_Y-minSize+1)+minSize);

        topoescenario.setX(randomX);
        topoescenario.setY(randomY);
    }


    // MÉTODO PARA CONTADOR TIEMPO
    public void contadorTiempo(int timeCount){

        new CountDownTimer(timeCount,1000){ // 1 SEGUNDOS
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
                guadrarDatosPlayer("Topos",contadorTopos); //cuando el contadpr llegue a 0 se actualiza la informacion del jugador.
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
            contadorTopos=0;
            dialog.dismiss();
            contadorToposEscenario.setText("0");
            GameOverBoolean=false;
            contadorTiempo(30000);
            MovimientoTopo();
        });
        botonMenuGameOver.setOnClickListener(view -> {
            Intent intent = new Intent(MapaJuegoClass.this, MenuClass.class);
            startActivity(intent);
        });
        botonPuntuacionesGameOver.setOnClickListener(v -> {
            Toast.makeText(MapaJuegoClass.this,"PROXIMAMENTE...",Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

}