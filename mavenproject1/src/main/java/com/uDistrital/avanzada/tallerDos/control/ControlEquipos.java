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
 *
 * @author Alex
 */
public class ControlEquipos {

    private final List<Equipo> equipos = new ArrayList<>();

    /**
     * Reemplaza toda la lista con entidades ya construidas.
     */
    public void reemplazarTodos(List<Equipo> nuevos) {
        equipos.clear();
        if (nuevos != null) {
            equipos.addAll(nuevos);
        }
    }

    /**
     * Construye entidades desde crudos y reemplaza la lista administrada.
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

    public List<Equipo> listar() {
        return Collections.unmodifiableList(equipos);
    }

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

    public int total() {
        return equipos.size();
    }
}
