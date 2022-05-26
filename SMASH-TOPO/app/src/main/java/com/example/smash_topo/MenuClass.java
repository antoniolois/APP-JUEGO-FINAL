package com.example.smash_topo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MenuClass extends AppCompatActivity {

    //DECLARACION DE VARIABLES
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseJugadoresRegistrados;

    Button CerrarSesion,Jugar,Clasificaciones;
    TextView cantidadTopos,userID,userName,correoElectronicoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // INSTANCIAS
        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        //INSTANCIA DE LA BASE DE DATOS
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseJugadoresRegistrados=firebaseDatabase.getReference("DATABASE JUGADORES REGISTRADOS");




            CerrarSesion = findViewById(R.id.boton_cerrar_sesion_menu);
            Jugar = findViewById(R.id.botonJugarMenu);
            Clasificaciones = findViewById(R.id.clasifiacionJugadoresPuntuacion);

            cantidadTopos = findViewById(R.id.cantidadTopos);
            userID = findViewById(R.id.userID);
            userName = findViewById(R.id.userName);
            correoElectronicoUser = findViewById(R.id.correoElectronicoUser);

        // CODIGO PARA JUGAR PARTIDA
        Jugar.setOnClickListener(view -> {

        });

        // CODIGO PARA MIRAR CLASIFICACIONES
        Clasificaciones.setOnClickListener(view -> {

        });


        // CODIGO PARA CERRAR SESION
        CerrarSesion.setOnClickListener(view -> {
            CloseSesion();
        });



    }

    // MÉTODO PARA PODER REALIZAR CONSULTAS A LA DATABASE
    private void consultaDatabase(){

        //ORDEN DE LOS USUARIOS POR CORREO ELECTRÓNICO, Y COMPARACIÓN CON EL CORREO DEL USUARIO QUE INICIE SESIÓN
        Query query = databaseJugadoresRegistrados.orderByChild("CORREO ELECTRONICO").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){ //BUCLE PARA RECORRER TODA LA BASE DE DATOS

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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
            startActivity(new Intent(MenuClass.this, MainActivity.class));
            finish(); }

    }

    // MÉTODO PARA CERRAR SESIÓN
    private void CloseSesion(){
        auth.signOut();
        startActivity(new Intent(MenuClass.this, MainActivity.class));
        Toast.makeText(this, "Sesión cerrada exictosamente", Toast.LENGTH_SHORT).show();
         }
    }
