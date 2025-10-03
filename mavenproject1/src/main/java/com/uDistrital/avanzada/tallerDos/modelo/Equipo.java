/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un equipo en el juego
 *
 * @author Alex
 */
public class Equipo implements Serializable {

    private String nombre;
    /*El enunciado del taller nos Obliga a que todos los equipos sean 
    de 4 jugadores*/
    private final List<Jugador> jugadores = new ArrayList<>(4);

    public Equipo(String nombre) {

        this.nombre = nombre;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

}
