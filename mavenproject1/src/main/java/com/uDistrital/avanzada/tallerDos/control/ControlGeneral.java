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
 *  * Clase encargada de orquestar las funciones del sistema
 *
 * @author Alex
 * @author Jeison
 */
public class ControlGeneral {

    // Inyección: se pasa this para que cada controlador
    // pueda llamar de regreso a ControlGeneral si lo necesita
    private final ControlEquipos cEquipos = new ControlEquipos();
    private final ControlProperties cProps = new ControlProperties();
    private final ControlSerializacion cSer = new ControlSerializacion();
    private final ControlRAF cRaf = new ControlRAF();
    private final ControlJuego cJuego = new ControlJuego();

    private final ControlVista cVista;

    /**
     *
     * Construtor encargado de instanciar objetos e inyectarse a las otras
     * clases
     */
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
    /**
     * Carga equipos desde el archivo properties 
     * @param f Archivo properties
     * @return  Mensaje notificando de la carga del archivo
     */

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
     * Inicia una nueva partida por los dos equipos seleccionados por nombre 
     * @param nombreA Nombre del equipo A
     * @param nombreB Nombre del equipo A
     * @return Mensaje resultante del intento de iniciar la partida
     */
    public String iniciarPartida(String nombreA, String nombreB) {
        Equipo a = cEquipos.buscarPorNombre(nombreA);
        Equipo b = cEquipos.buscarPorNombre(nombreB);
        return cJuego.iniciar(a, b);
    }

    /**
     * Realiza el lanzamiento del siguiente juego
     * En caso de finalizar la partida registra un ganador
     * @return Resultado del siguiente lanzamiento
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
    /**
     * Inicia una nueva ronda en la partida actual
     * @return Mensaje de la nuea ronda iniciada
     */
    public String nuevaRonda() {
        return cJuego.nuevaRonda();
    }

    /**
     * Guarda el estado actual de los equipos
     */
    public void guardarEstado() {
        try {
            cSer.serializarEquipos(new ArrayList<>(cEquipos.listar()));
        } catch (Exception ignored) {
        }
    }

    /**
     * Recupera el acceso de las partidas almacenadas en el archivo
     * @return Lista de registro del historial
     */
public List<ArchivoAccesoAleatorio.Registro> obtenerHistorial() {
        try {
            return cRaf.leerHistorial();
        } catch (Exception e) {
            return List.of();
        }
    }
}
