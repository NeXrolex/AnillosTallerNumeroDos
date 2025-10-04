/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.modelo;

import java.io.Serializable;

/**
 * Representa los Datos y puntajes del juego, asignados a cada tipo de jugada
 * además de la meta de puntos para ganar. 
 * Implementa serializable para guardar y cargar
 *
 * @author Alex
 */
public class Juego implements Serializable {

    // Estado dinámico de la partida
    private int rondaActual;
    private Equipo equipoA;
    private Equipo equipoB;
    private int puntosA;
    private int puntosB;
    private int idxJugador;// índice del jugador que lanza
    private boolean turnoEquipoA; // true = juega A, false = juega B
    private boolean enMuerteSubita;

    // Parámetros del taller 
    private int puntajeMonona = 8;
    private int puntajeEngarzada = 5;
    private int puntajeHueco = 3;
    private int puntajePalmo = 2;
    private int puntajeTimbre = 1;
    private int metaPuntos = 21;

    /**
     * Devuelve el número de la ronda actual.
     * @return Ronda actual.
     */
    public int getRondaActual() {
        return rondaActual;
    }
    
    /**
     * Asigna el número de la ronda actual.
     * @param rondaActual Nueva ronda actual.
     */
    public void setRondaActual(int rondaActual) {
        this.rondaActual = rondaActual;
    }
    
    /**
     * Devuelve el equipo A.
     * @return Equipo A.
     */
    public Equipo getEquipoA() {
        return equipoA;
    }
    
    /**
     * Asigna el equipo A.
     * @param equipoA Equipo A.
     */
    public void setEquipoA(Equipo equipoA) {
        this.equipoA = equipoA;
    }
    
    /**
     * Devuelve el equipo B.
     * @return Equipo B.
     */
    public Equipo getEquipoB() {
        return equipoB;
    }
    
    /**
     * Asigna el equipo B.
     * @param equipoB Equipo B.
     */
    public void setEquipoB(Equipo equipoB) {
        this.equipoB = equipoB;
    }
    
    /**
     * Devuelve los puntos del equipo A.
     * @return Puntos del equipo A.
     */
    public int getPuntosA() {
        return puntosA;
    }
    
    /**
     * Asigna los puntos del equipo A.
     * @param puntosA Nueva cantidad de puntos.
     */
    public void setPuntosA(int puntosA) {
        this.puntosA = puntosA;
    }
    
    /**
     * Devuelve los puntos del equipo B.
     * @return Puntos del equipo B.
     */
    public int getPuntosB() {
        return puntosB;
    }
    
    /**
     * Asigna los puntos del equipo B.
     * @param puntosB Nueva cantidad de puntos.
     */
    public void setPuntosB(int puntosB) {
        this.puntosB = puntosB;
    }
    
    /**
     * Devuelve el índice del jugador actual.
     * @return Índice del jugador.
     */
    public int getIdxJugador() {
        return idxJugador;
    }
    
    /**
     * Asigna el índice del jugador que lanza.
     * @param idxJugador Índice del jugador.
     */
    public void setIdxJugador(int idxJugador) {
        this.idxJugador = idxJugador;
    }
    
    /**
     * Indica si es el turno del equipo A.
     * @return {@code true} si es turno del equipo A, {@code false} en caso contrario.
     */
    public boolean isTurnoEquipoA() {
        return turnoEquipoA;
    }

   /**
     * Define de quién es el turno actualmente.
     * @param turnoEquipoA {@code true} si es turno del equipo A.
     */
    public void setTurnoEquipoA(boolean turnoEquipoA) {
        this.turnoEquipoA = turnoEquipoA;
    }

    /**
     * Indica si la partida está en modo muerte súbita.
     * @return {@code true} si está en muerte súbita, {@code false} en caso contrario.
     */
    public boolean isEnMuerteSubita() {
        return enMuerteSubita;
    }

    /**
     * Define si la partida entra en muerte súbita.
     * @param enMuerteSubita {@code true} para habilitar muerte súbita.
     */
    public void setEnMuerteSubita(boolean enMuerteSubita) {
        this.enMuerteSubita = enMuerteSubita;
    }

    /**
     * Devuelve el puntaje asignado a una monona.
     * @return Puntaje por monona.
     */
    public int getPuntajeMonona() {
        return puntajeMonona;
    }

    /**
     * Asigna el puntaje de una monona.
     * @param puntajeMonona Nuevo puntaje para monona.
     */
    public void setPuntajeMonona(int puntajeMonona) {
        this.puntajeMonona = puntajeMonona;
    }

    /**
     * Devuelve el puntaje de una engarzada.
     * @return Puntaje por engarzada.
     */
    public int getPuntajeEngarzada() {
        return puntajeEngarzada;
    }

    /**
     * Asigna el puntaje de una engarzada.
     * @param puntajeEngarzada Nuevo puntaje para engarzada.
     */
    public void setPuntajeEngarzada(int puntajeEngarzada) {
        this.puntajeEngarzada = puntajeEngarzada;
    }

    /**
     * Devuelve el puntaje de un hueco.
     * @return Puntaje por hueco.
     */
    public int getPuntajeHueco() {
        return puntajeHueco;
    }

    /**
     * Asigna el puntaje para hueco.
     * @param puntajeHueco Nuevo puntaje para hueco.
     */
    public void setPuntajeHueco(int puntajeHueco) {
        this.puntajeHueco = puntajeHueco;
    }

    /**
     * Devuelve el puntaje de un palmo.
     * @return Puntaje por palmo.
     */
    public int getPuntajePalmo() {
        return puntajePalmo;
    }

    /**
     * Asigna el puntaje para palmo.
     * @param puntajePalmo Nuevo puntaje para palmo.
     */
    public void setPuntajePalmo(int puntajePalmo) {
        this.puntajePalmo = puntajePalmo;
    }

    /**
     * Devuelve el puntaje de un timbre.
     * @return Puntaje por timbre.
     */
    public int getPuntajeTimbre() {
        return puntajeTimbre;
    }

    /**
     * Asigna el puntaje para timbre.
     * @param puntajeTimbre Nuevo puntaje para timbre.
     */
    public void setPuntajeTimbre(int puntajeTimbre) {
        this.puntajeTimbre = puntajeTimbre;
    }

    /**
     * Devuelve la meta de puntos para ganar la partida.
     * @return Meta de puntos.
     */
    public int getMetaPuntos() {
        return metaPuntos;
    }

    /**
     * Asigna una nueva meta de puntos para la partida.
     * @param metaPuntos Nueva meta de puntos.
     */
    public void setMetaPuntos(int metaPuntos) {
        this.metaPuntos = metaPuntos;
    }
}
