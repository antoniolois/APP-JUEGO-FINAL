package com.example.smash_topo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


    //ELEMENTOS DE LA PANTALLA
    Button CerrarSesion,Jugar,Clasificaciones;
    TextView cantidadTopos,userID,nombreJugadorMenu,correoJugadorMenu;

    @Override
    public void onBackPressed(){
        //En caso de querer permitir volver atrás usa esta llamada: super.onBackPressed();
    }


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



        // UNIÓN CON LOS ELEMENTOS DEL LAYOUT

            CerrarSesion = findViewById(R.id.boton_cerrar_sesion_menu);
            Jugar = findViewById(R.id.botonJugarMenu);
            Clasificaciones = findViewById(R.id.clasifiacionJugadoresPuntuacion);

            cantidadTopos = findViewById(R.id.cantidadTopos);
            userID = findViewById(R.id.userID);
            correoJugadorMenu = findViewById(R.id.correoJugadorMenu);
            nombreJugadorMenu = findViewById(R.id.nombreJugadorMenu);

        // CODIGO PARA JUGAR PARTIDA
        Jugar.setOnClickListener(view -> {
            Intent intent = new Intent(MenuClass.this, MapaJuegoClass.class);

            String userIDPlay = userID.getText().toString();
            String nombreJugadorPlay = nombreJugadorMenu.getText().toString();
            String cantidadToposPlay = cantidadTopos.getText().toString();

            //MANDAMOS LOS PARÁMETROS
            intent.putExtra("UID",userIDPlay);
            intent.putExtra("Topos",cantidadToposPlay);
            intent.putExtra("NOMBRE",nombreJugadorPlay);

            startActivity(intent);
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

                    String toposCantidadValor= ""+dataSnapshot.child("Topos").getValue(); //VALOR TOPOS APLASTADOS Y ASIGNACIÓN
                    cantidadTopos.setText(toposCantidadValor);

                    String userIDValue= ""+dataSnapshot.child("UID").getValue(); //VALOR USER ID Y ASIGNACIÓN
                    userID.setText(userIDValue);

                    String correoJugadorValue= ""+dataSnapshot.child("CORREO ELECTRONICO").getValue(); //VALOR CORREO JUGADOR Y ASIGNACIÓN
                    correoJugadorMenu.setText(correoJugadorValue);

                    String nombreJugadorValue= ""+dataSnapshot.child("NOMBRE").getValue(); //VALOR NOMBRE JUGADOR Y ASIGNACIÓN
                    nombreJugadorMenu.setText(nombreJugadorValue);


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
            consultaDatabase();
            Toast.makeText(this, "Jugador actualemente logueado", Toast.LENGTH_LONG).show();

        } else  {
            startActivity(new Intent(MenuClass.this, MainActivity.class));
            finish(); }

    }

    // MÉTODO PARA CERRAR SESIÓN
    private void CloseSesion(){
        auth.signOut();
        startActivity(new Intent(MenuClass.this, MainActivity.class));
        Toast.makeText(this, "Sesión cerrada exictosamente", Toast.LENGTH_LONG).show();
         }
    }
