/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.control;

import com.uDistrital.avanzada.tallerDos.modelo.ArchivoProperties;
import com.uDistrital.avanzada.tallerDos.modelo.Equipo;
import com.uDistrital.avanzada.tallerDos.modelo.Jugador;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase encargada de gestionar a los equipos
 * @author Alex
 * @author Jeison 
 */
public class ControlEquipos {
    /**
     * Lista que almacena los equipos gestionados
     */
    private final List<Equipo> equipos = new ArrayList<>();

    /**
     * Remplaza la lista interna de equipos con una nueva
     * @param nuevos Nueva lista de uquipos para remplazar
     */
    public void reemplazarTodos(List<Equipo> nuevos) {
        equipos.clear();
        if (nuevos != null) {
            equipos.addAll(nuevos);
        }
    }

    /**
     * Construye y replaza la lista de equipos a paritir de datos importados 
     * @param crudos Lista de objetos con los datos a convertir
     */
    public void reemplazarDesdeCrudos(List<ArchivoProperties.EquipoRaw> crudos) {
        equipos.clear();
        if (crudos == null) {
            return;
        }
        for (ArchivoProperties.EquipoRaw raw : crudos) {
            Equipo e = new Equipo(raw.getNombre());
            for (ArchivoProperties.JugadorRaw jr : raw.getJugadores()) {
                e.getJugadores().add(new Jugador(jr.getNombre(), jr.getApodo()));
            }
            equipos.add(e);
        }
    }
    /**
     * Regresa una lista indificable de los datos almacenados
     * @return Lista inmodificalbe de los equipos
     */
    public List<Equipo> listar() {
        return Collections.unmodifiableList(equipos);
    }
    /**
     * Busca los equipos por el nombre 
     * @param nombre nombre del equipos a buscar 
     * @return El equipo que coincide con el nombre o null si no se encuetra
     */
    public Equipo buscarPorNombre(String nombre) {
        if (nombre == null) {
            return null;
        }
        for (Equipo e : equipos) {
            if (nombre.equals(e.getNombre())) {
                return e;
            }
        }
        return null;
    }
    /**
     * Regresa el numero total de equipos almacenados
     * @return Numero total de equipos almacenados
     */
    public int total() {
        return equipos.size();
    }
}
