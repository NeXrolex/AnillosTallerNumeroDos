/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.control;

import com.uDistrital.avanzada.tallerDos.modelo.ArchivoAccesoAleatorio;
import com.uDistrital.avanzada.tallerDos.modelo.ArchivoProperties;
import com.uDistrital.avanzada.tallerDos.modelo.Equipo;
import java.util.ArrayList;
import java.util.List;

/**
 * Se responsabiliza de hacer las conexiones entre los controles y continuar la
 * comunicacion
 *
 * @author Alex
 */
public class ControlGeneral {

    private final ControlEquipos cEquipos = new ControlEquipos();
    private final ControlProperties cProps = new ControlProperties();
    private final ControlSerializacion cSer = new ControlSerializacion();
    private final ControlRAF cRaf = new ControlRAF();
    private final ControlJuego cJuego = new ControlJuego();

    private final ControlVista cVista;

    public ControlGeneral() {
        this.cVista = new ControlVista(this);
        try {
            var rec = cSer.deserializarEquipos();
            if (!rec.isEmpty()) {
                cEquipos.reemplazarTodos(rec);
            }
            cVista.actualizarEquiposEnVista(cEquipos.listar());
        } catch (Exception ignored) {
        }
    }

    
    public String cargarEquiposDesdeProperties(java.io.File f) {
        try {
            // datos crudos del archivo
            List<ArchivoProperties.EquipoRaw> crudos = cProps.cargarCrudo(f); 
            // crea y administra entidades
            cEquipos.reemplazarDesdeCrudos(crudos);                           
            cVista.actualizarEquiposEnVista(cEquipos.listar());
            return "Equipos cargados: " + cEquipos.total() + "\n";
        } catch (Exception ex) {
            return "Error cargando equipos: " + ex.getMessage() + "\n";
        }
    }

    /**
     * Vista -> General -> Equipos (buscar por nombre) -> Juego (iniciar con
     * DATOS).
     */
    public String iniciarPartida(String nombreA, String nombreB) {
        Equipo a = cEquipos.buscarPorNombre(nombreA);
        Equipo b = cEquipos.buscarPorNombre(nombreB);
        return cJuego.iniciar(a, b);
    }

    /**
     * Vista -> General -> Juego; si termina, General -> RAF.
     */
    public ControlJuego.Lanzamiento siguienteLanzamiento() {
        var r = cJuego.siguienteLanzamiento();
        if (!r.hayError() && r.finPartida && r.ganador != null) {
            try {
                cRaf.escribirRegistroRonda("RND%05d"
                        .formatted(r.ronda), r.ganador, true);
            } catch (Exception ignored) {
            }
        }
        return r;
    }

    public String nuevaRonda() {
        return cJuego.nuevaRonda();
    }

    /**
     * Vista -> General -> Serialización (guardar equipos).
     */
    public void guardarEstado() {
        try {
            cSer.serializarEquipos(new ArrayList<>(cEquipos.listar()));
        } catch (Exception ignored) {
        }
    }

    /**
     * Vista -> General -> RAF (lectura historial).
     */
    public List<ArchivoAccesoAleatorio.Registro> obtenerHistorial() {
        try {
            return cRaf.leerHistorial();
        } catch (Exception e) {
            return List.of();
        }
    }
}
