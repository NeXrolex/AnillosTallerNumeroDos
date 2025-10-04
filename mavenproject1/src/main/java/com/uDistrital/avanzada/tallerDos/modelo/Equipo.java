/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un equipo en el juego Implementa serializacion para permitir la
 * serializacion de objetos
 *
 * @author Alex
 */
public class Equipo implements Serializable {

    private String nombre;
    /*El enunciado del taller nos Obliga a que todos los equipos sean 
    de 4 jugadores*/
    private final List<Jugador> jugadores = new ArrayList<>(4);

    /**
     * Constructor que recibe como parametro el nombre del equipo
     *
     * @param nombre Nombre del equipo
     */
    public Equipo(String nombre) {

        this.nombre = nombre;

    }
    
    /**
     * Obtiene el nombre del equipo
     * 
     * @return Nombre del equipo
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Asigna el nombre del equipo 
     * 
     * @param nombre Nombre del equipo 
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Obtiene la lista de jugadores que conforman el equipo
     * La lista siempre tendra un maximo de 4 jugadores 
     * 
     * @return Lista de jugadores del equipo
     */
    public List<Jugador> getJugadores() {
        return jugadores;
    }

}
