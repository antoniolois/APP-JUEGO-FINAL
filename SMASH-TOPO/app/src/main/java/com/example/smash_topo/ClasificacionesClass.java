package com.example.smash_topo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClasificacionesClass extends AppCompatActivity {

    //DECLARACIONES
    LinearLayoutManager linearLayoutManager;
    RecyclerView clasificacionesView;
    List<PlayerClass> playerClassList;
    DatabaseReference databaseJugadoresRegistrados;
    AdapterClass adapter;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasificaciones_class);

        /*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("CLASIFICACIONES");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

         */

        //instancia
        auth = FirebaseAuth.getInstance();
        linearLayoutManager = new LinearLayoutManager(this);
        clasificacionesView = findViewById(R.id.clasificacionesView);

        //ORDEN DE LAS CLASIFICACIONES
        linearLayoutManager.setReverseLayout(true); //alfabeticamente
        //linearLayoutManager.setStackFromEnd(true);

        clasificacionesView.setHasFixedSize(true);
        clasificacionesView.setLayoutManager(linearLayoutManager);

        //inicializacion del array
        playerClassList = new ArrayList<>();
        
        getPLayers();
    }

    private void getPLayers() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        databaseJugadoresRegistrados=firebaseDatabase.getReference("DATABASE JUGADORES REGISTRADOS");
        databaseJugadoresRegistrados.orderByChild("Topos").addValueEventListener(new ValueEventListener() {
            //para actualizar la puntuacion
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playerClassList.clear();
                //bucle para recorrer la base de datos
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //obtener los datos de usuario
                    PlayerClass playerClass = dataSnapshot.getValue(PlayerClass.class);
                    playerClassList.add(playerClass);
                    adapter = new AdapterClass(ClasificacionesClass.this,playerClassList);
                    clasificacionesView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}