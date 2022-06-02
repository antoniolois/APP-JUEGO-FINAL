package com.example.smash_topo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

    //ELEMENTOS ALMACENAMIENTO CAMBIO DE IMAGEN
    private StorageReference storageReferenceUser;
    private String pathAlmacenamiento="ImagenPerfil/*";
    private String [] permisoAlmacenar;
    private Uri imagen_uri;
    private String perfilJugador;

    //codigos de permisos y solictudes
    private static  final int codigoSolicitudAlmacenamiento=8520;
    private static  final int codigoSeleccionImagen=7410;


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


        //INICIALIZACION DEL ALMACENANMIETNO
        storageReferenceUser = FirebaseStorage.getInstance().getReference();
        permisoAlmacenar = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


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
            if(i==0) {
                perfilJugador = "Imagen Jugador";
                CambiarImagenPerfil();
            }else if (i==1) {
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
        String [] opciones ={"ACCEDER A LA GALERÍA"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CAMBIO DE LA IMAGEN DE PERFIL");
        builder.setItems(opciones, (dialogInterface, i) -> {
            //switch para las dos opciones
            switch (i){
                case 0:
                    if(!checkStoragePermissions()){
                        SolicitarPermisos(); //activar permisos
                    }else{
                        imageEleccion();
                    }
            }
        });
        builder.create().show();
    }

    //ABRIR LA GALERIA
    private void imageEleccion() {
        Intent intentGaleria = new Intent(Intent.ACTION_PICK);
        intentGaleria.setType("image/*");
        startActivityForResult(intentGaleria,codigoSeleccionImagen);
    }

    //MÉTODO DE SOLICUTUD DE LOS PERMISOS
    private void SolicitarPermisos() {
        requestPermissions(permisoAlmacenar,codigoSolicitudAlmacenamiento);
    }
    //MÉTODO QUE COMPRUEBa LOS PERMISOS
    private boolean checkStoragePermissions() {
        boolean permiso = ContextCompat.checkSelfPermission(MenuClass.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return permiso;
    }

    //ACTIVACION CUANDO EL JUGADOR YA TIENE SELECCINADA UNA IAMGEN
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode==codigoSeleccionImagen){
                imagen_uri=data.getData();
                uploadImage(imagen_uri);
                
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //actualizar base de datos
    private void uploadImage(Uri imagen_uri) {
        String pathAlmacenamientoUserName = pathAlmacenamiento + ""+ perfilJugador +""+user.getUid();
        StorageReference storageReference = storageReferenceUser.child(pathAlmacenamientoUserName);
        storageReference.putFile(imagen_uri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while(!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();

                if(uriTask.isSuccessful()){
                    HashMap<String,Object> resultadoImage = new HashMap<>();
                    resultadoImage.put(perfilJugador,downloadUri.toString());

                    databaseJugadoresRegistrados.child(user.getUid()).updateChildren(resultadoImage)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(MenuClass.this,"IMAGEN ACTUALIZADA",Toast.LENGTH_SHORT).show();

                            }).addOnFailureListener(e -> {
                        Toast.makeText(MenuClass.this,"ERROR: "+e.getMessage(),Toast.LENGTH_SHORT).show();

                    });
                }else {
                    Toast.makeText(MenuClass.this, "ERROR", Toast.LENGTH_SHORT).show();
                }


        }).addOnFailureListener(e -> {
            Toast.makeText(MenuClass.this,"ERROR: "+e.getMessage(),Toast.LENGTH_SHORT).show();

        });
    }

    //METODO PARA ELECCION DEL USUARIO EN LOS BOTONES PERMITIR/DENEGAR EN EL DIALOGO
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case codigoSolicitudAlmacenamiento:
                if(grantResults.length>0){
                    boolean permisoAlmacenamientoCorrecto = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    if(permisoAlmacenamientoCorrecto){
                        imageEleccion();
                    }else{
                        Toast.makeText(MenuClass.this,"ES NECESARIO HABILITAR LOS PERMISOS DE ALAMCENAMIENTO",Toast.LENGTH_SHORT).show();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
