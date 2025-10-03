/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.modelo;

import java.io.Serializable;

/**
 * Representa a un jugador en el programa 
 * 
 * @author Alex
 */
public class Jugador implements Serializable {

    private String nombre;
    private String apodo;

    public Jugador(String nombre, String apodo) {

        this.nombre = nombre;
        this.apodo = apodo;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }
    
    

}
