
package com.uDistrital.avanzada.tallerDos.control;

import com.uDistrital.avanzada.tallerDos.modelo.ArchivoAccesoAleatorio;
import com.uDistrital.avanzada.tallerDos.modelo.ArchivoProperties;
import com.uDistrital.avanzada.tallerDos.modelo.Equipo;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase se encarga de coordinar la comunicación entre los diferentes
 * módulos de control del programa: equipos, propiedades, serialización,
 * archivos de acceso aleatorio, juego y vista.
 *
 * @author Alex
 * @author Santiago
 */
public class ControlGeneral {

    private final ControlEquipos cEquipos = new ControlEquipos();
    private final ControlProperties cProps = new ControlProperties();
    private final ControlSerializacion cSer = new ControlSerializacion();
    private final ControlRAF cRaf = new ControlRAF();
    private final ControlJuego cJuego = new ControlJuego();
    private final ControlVista cVista;
    
    /**
     * Constructor de la clase.
     * Inicializa la vista principal y carga los equipos previamente
     * serializados (si existen). Si no hay registros, la lista de equipos
     * comienza vacía.
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
     * Carga equipos desde un archivo .properties.
     * Vista -> General -> Properties (crudo) -> Equipos (crear entidades) ->
     * Vista
     */
    public String cargarEquiposDesdeProperties(java.io.File f) {
        try {
            List<ArchivoProperties.EquipoRaw> crudos = cProps.cargarCrudo(f); // datos crudos del archivo
            cEquipos.reemplazarDesdeCrudos(crudos);                           // crea y administra entidades
            cVista.actualizarEquiposEnVista(cEquipos.listar());
            return "Equipos cargados: " + cEquipos.total() + "\n";
        } catch (Exception ex) {
            return "Error cargando equipos: " + ex.getMessage() + "\n";
        }
    }

    /**
     * Inicia una partida entre dos equipos seleccionados.
     *
     * Flujo de datos:
     * Vista -> ControlGeneral -> ControlEquipos -> ControlJuego
     *
     * @param nombreA nombre del equipo A
     * @param nombreB nombre del equipo B
     * @return mensaje indicando el inicio de la partida o error
     */
    public String iniciarPartida(String nombreA, String nombreB) {
        Equipo a = cEquipos.buscarPorNombre(nombreA);
        Equipo b = cEquipos.buscarPorNombre(nombreB);
        return cJuego.iniciar(a, b);
    }

    /**
     * Ejecuta un lanzamiento en la partida actual.
     *
     * Flujo de datos:
     * Vista -> ControlGeneral -> ControlJuego
     * Si la partida termina, se guarda el resultado usando ControlRAF.
     *
     * @return objeto Lanzamiento con los datos del tiro
     */
    public ControlJuego.Lanzamiento siguienteLanzamiento() {
        var r = cJuego.siguienteLanzamiento();
        if (!r.hayError() && r.finPartida && r.ganador != null) {
            try {
                cRaf.escribirRegistroRonda("RND%05d".formatted(r.ronda), r.ganador, true);
            } catch (Exception ignored) {
            }
        }
        return r;
    }
    /**
     * Inicia una nueva ronda si la partida anterior ha finalizado.
     *
     * @return mensaje informativo sobre el estado de la nueva ronda
     */
    public String nuevaRonda() {
        return cJuego.nuevaRonda();
    }

    /**
     * Guarda el estado actual de los equipos mediante serialización.
     *
     * Flujo de datos:
     * Vista -> ControlGeneral -> ControlSerializacion
     */
    public void guardarEstado() {
        try {
            cSer.serializarEquipos(new ArrayList<>(cEquipos.listar()));
        } catch (Exception ignored) {
        }
    }

    /**
     * Obtiene el historial de partidas almacenado en archivos de acceso aleatorio.
     *
     * Flujo de datos:
     * Vista -> ControlGeneral -> ControlRAF
     *
     * @return lista de registros del historial o lista vacía si ocurre un error
     */
    public List<ArchivoAccesoAleatorio.Registro> obtenerHistorial() {
        try {
            return cRaf.leerHistorial();
        } catch (Exception e) {
            return List.of();
        }
    }
}
