package com.example.smash_topo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuClass extends AppCompatActivity {

    //DECLARACION DE VARIABLES
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseJugadoresRegistrados;


    //ELEMENTOS DE LA PANTALLA
    Button CerrarSesion,Jugar,Clasificaciones;
    TextView cantidadTopos,userID,nombreJugadorMenu,correoJugadorMenu,botonEditarPerfil;
    CircleImageView foto_perfil_player;

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
            foto_perfil_player = findViewById(R.id.foto_perfil_player);
            botonEditarPerfil = findViewById(R.id.botonEditarPerfil);

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


        botonEditarPerfil.setOnClickListener(view -> {

            EditarDatosPerfil();
        });




    }
    //MÉTODO PARA EDITAR DATOS DEL PERFIL
    private void EditarDatosPerfil() {
        //opciones para editar los datos
        String [] opciones ={"Cambiar imagen de perfil","Cambiar nombre"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(opciones, (dialogInterface, i) -> {
            //switch para las dos opciones
            switch (i){
                case 0:
                    CambiarImagenPerfil();
                case 1:
                    CambiarNombrePerfil("NOMBRE");
            }

        });
        builder.create().show(); //visualización del builder
    }

    // MÉTODO PARA CAMBIAR EL NOMBRE DE PERFIL
    private void CambiarNombrePerfil(String tag) {
        //se crea un dialogo con dos opciones y dos botones de acción
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CAMBIO DE "+tag);

        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(15,15,15,15);

        EditText editText = new EditText(this);
        String nombreJugadorPlay = nombreJugadorMenu.getText().toString();
        editText.setHint(nombreJugadorPlay);
        int maxLength = 15;
        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        linearLayoutCompat.addView(editText);

        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", (dialogInterface, i) -> { //si el usuario puslsa en actualizar
            String value = editText.getText().toString();
            HashMap<String,Object> cambioFinal = new HashMap<>();
            cambioFinal.put(tag,value); //aqui se actuliza el tag por el valor del string del usuario

            databaseJugadoresRegistrados.child(user.getUid()).updateChildren(cambioFinal).addOnSuccessListener(aVoid -> { //si todo funciona correctamente
                Toast.makeText(MenuClass.this,"DATOS ACTUALIZADOS",Toast.LENGTH_SHORT).show();

            }).addOnFailureListener(e -> {//si algo no  funciona correctamente
                Toast.makeText(MenuClass.this,"ERROR: "+e.getMessage(),Toast.LENGTH_SHORT).show();

            });
        });
        builder.setNegativeButton("CANCELAR", (dialogInterface, i) -> {
            //
        });
        builder.create().show(); //visualización del builder
    }

    // MÉTODO PARA CAMBIAR LA IMAGEN DE PERFIL
    private void CambiarImagenPerfil() {
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

                    String imagen = ""+dataSnapshot.child("Imagen Jugador").getValue();
                    try{
                        Picasso.get().load(imagen).into(foto_perfil_player);
                    }catch(Exception e){
                        Picasso.get().load(R.drawable.player_image).into(foto_perfil_player);
                    }
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
