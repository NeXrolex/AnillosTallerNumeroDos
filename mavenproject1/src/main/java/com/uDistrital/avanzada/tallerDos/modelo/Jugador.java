/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.modelo;

import java.io.Serializable;

/**
 * Representa a un jugador en el programa
 * Implementa serializacion para permitir la serializacion de objetos
 *
 * @author Alex
 */
public class Jugador implements Serializable {

    private String nombre;
    private String apodo; //cada jugador tiene un apodo

    /**
     * Constructor que recibe los los datos del jugador
     *
     * @param nombre nombre del jugador
     * @param apodo apodo del jugador
     */
    public Jugador(String nombre, String apodo) {

        this.nombre = nombre;
        this.apodo = apodo;

    }

    /**
     * Obtiene el nombre del jugador
     *
     * @return El nombre del jugador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna un nuevo nombre al jugador
     *
     * @param nombre Nombre del jugador
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apodo del jugador
     *
     * @return El apodo del jugador
     */
    public String getApodo() {
        return apodo;
    }

    /**
     * Asigna el apodo del jugador
     *
     * @param apodo Apodo del jugador
     */
    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

}
