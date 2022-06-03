package com.example.smash_topo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PlayerClass extends AppCompatActivity {
    //DECLARACION DE VARIABLES
    String correoElectronico,fechaRegistro,imagenJugador,nombreJugador,passwordJugador,userID;
    int toposAplastados;

    //CONSTRUCTOR
    public PlayerClass(){

    }

    //CONSTRUCTOR CON PARÁMETROS

    public PlayerClass(String correoElectronico, String fechaRegistro, String imagenJugador, String nombreJugador, String passwordJugador, String userID, int toposAplastados) {
        this.correoElectronico = correoElectronico;
        this.fechaRegistro = fechaRegistro;
        this.imagenJugador = imagenJugador;
        this.nombreJugador = nombreJugador;
        this.passwordJugador = passwordJugador;
        this.userID = userID;
        this.toposAplastados = toposAplastados;
    }

    //GETTERS Y SETTERS

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getImagenJugador() {
        return imagenJugador;
    }

    public void setImagenJugador(String imagenJugador) {
        this.imagenJugador = imagenJugador;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getPasswordJugador() {
        return passwordJugador;
    }

    public void setPasswordJugador(String passwordJugador) {
        this.passwordJugador = passwordJugador;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getToposAplastados() {
        return toposAplastados;
    }

    public void setToposAplastados(int toposAplastados) {
        this.toposAplastados = toposAplastados;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_class);
    }
}