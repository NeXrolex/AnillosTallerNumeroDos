/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.modelo;

import java.io.Serializable;

/**
 * Representa los Datos y puntajes del juego implementa serializable para
 * guardar y cargar
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
    private int idxJugador;       // índice del jugador que lanza
    private boolean turnoEquipoA; // true = juega A, false = juega B
    private boolean enMuerteSubita;

    // Parámetros del taller (puntos por jugada y meta)
    private int puntajeMonona = 8;
    private int puntajeEngarzada = 5;
    private int puntajeHueco = 3;
    private int puntajePalmo = 2;
    private int puntajeTimbre = 1;
    private int metaPuntos = 21;

    // ===== Getters y Setters =====
    public int getRondaActual() {
        return rondaActual;
    }

    public void setRondaActual(int rondaActual) {
        this.rondaActual = rondaActual;
    }

    public Equipo getEquipoA() {
        return equipoA;
    }

    public void setEquipoA(Equipo equipoA) {
        this.equipoA = equipoA;
    }

    public Equipo getEquipoB() {
        return equipoB;
    }

    public void setEquipoB(Equipo equipoB) {
        this.equipoB = equipoB;
    }

    public int getPuntosA() {
        return puntosA;
    }

    public void setPuntosA(int puntosA) {
        this.puntosA = puntosA;
    }

    public int getPuntosB() {
        return puntosB;
    }

    public void setPuntosB(int puntosB) {
        this.puntosB = puntosB;
    }

    public int getIdxJugador() {
        return idxJugador;
    }

    public void setIdxJugador(int idxJugador) {
        this.idxJugador = idxJugador;
    }

    public boolean isTurnoEquipoA() {
        return turnoEquipoA;
    }

    public void setTurnoEquipoA(boolean turnoEquipoA) {
        this.turnoEquipoA = turnoEquipoA;
    }

    public boolean isEnMuerteSubita() {
        return enMuerteSubita;
    }

    public void setEnMuerteSubita(boolean enMuerteSubita) {
        this.enMuerteSubita = enMuerteSubita;
    }

    public int getPuntajeMonona() {
        return puntajeMonona;
    }

    public void setPuntajeMonona(int puntajeMonona) {
        this.puntajeMonona = puntajeMonona;
    }

    public int getPuntajeEngarzada() {
        return puntajeEngarzada;
    }

    public void setPuntajeEngarzada(int puntajeEngarzada) {
        this.puntajeEngarzada = puntajeEngarzada;
    }

    public int getPuntajeHueco() {
        return puntajeHueco;
    }

    public void setPuntajeHueco(int puntajeHueco) {
        this.puntajeHueco = puntajeHueco;
    }

    public int getPuntajePalmo() {
        return puntajePalmo;
    }

    public void setPuntajePalmo(int puntajePalmo) {
        this.puntajePalmo = puntajePalmo;
    }

    public int getPuntajeTimbre() {
        return puntajeTimbre;
    }

    public void setPuntajeTimbre(int puntajeTimbre) {
        this.puntajeTimbre = puntajeTimbre;
    }

    public int getMetaPuntos() {
        return metaPuntos;
    }

    public void setMetaPuntos(int metaPuntos) {
        this.metaPuntos = metaPuntos;
    }
}
