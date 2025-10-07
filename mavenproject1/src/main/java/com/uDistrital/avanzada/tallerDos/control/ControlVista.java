/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.control;

import com.uDistrital.avanzada.tallerDos.modelo.ArchivoAccesoAleatorio;
import com.uDistrital.avanzada.tallerDos.modelo.Jugador;
import com.uDistrital.avanzada.tallerDos.vista.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 * Controlador de vitsa que maneja la logica de presentacion
 *
 * @author Alex, Jeison
 */
public class ControlVista implements ActionListener {

    private final VentanaPrincipal vista;
    private final ControlGeneral controlGeneral;

    /**
     * Constructor que recibe la inyeccion de controlGeneral e inicializa la
     * ventana. Configura los ebventos de los botones
     *
     * @param general Inyeccion del controlGeneral
     */
    public ControlVista(ControlGeneral general) {
        this.controlGeneral = general;
        this.vista = new VentanaPrincipal();

        //Seperacion de clases, le damos acciones a los botones 
        vista.btnCargarProps.addActionListener(this);
        vista.btnIniciar.addActionListener(this);
        vista.btnLanzar.addActionListener(this);
        vista.btnOtraRonda.addActionListener(this);
        vista.btnHistorial.addActionListener(this);
        vista.btnSalir.addActionListener(this);
        vista.btnVolver.addActionListener(this);
    }

    /**
     * Metodo que maneja la delegacion de eventos de click de botones
     *
     *
     * @param e evento generado al hacer click en un boton
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == vista.btnCargarProps) {
            onCargarProps();
        } else if (src == vista.btnIniciar) {
            onIniciar();
        } else if (src == vista.btnLanzar) {
            onLanzarPaso();
        } else if (src == vista.btnOtraRonda) {
            onOtraRonda();
        } else if (src == vista.btnHistorial) {
            onVerHistorial();
        } else if (src == vista.btnSalir) {
            onSalir();
        } else if (src == vista.btnVolver) {
            onVolver();
        }
    }

    /**
     * Ayuda a la comunicacion para que el JFileChooser se comunique con el
     * modelo Si no hay archivo no cumple la accion
     *
     */
    private void onCargarProps() {
        File f = vista.seleccionarProperties(); // JFileChooser en la Vista
        if (f == null) {
            return;
        }
        String msg = controlGeneral.cargarEquiposDesdeProperties(f);
        vista.append(msg);
    }

    /**
     * Inicia la partida tomando los equipos seleccionados en la vista
     *
     */
    private void onIniciar() {
        String a = (String) vista.cbEquipoA.getSelectedItem();
        String b = (String) vista.cbEquipoB.getSelectedItem();
        String msg = controlGeneral.iniciarPartida(a, b);
        if (msg.startsWith("Seleccione")) {
            vista.mostrarDialogo("Advertencia", msg);
            return; // No continúa si hay error
        }
        if (msg.startsWith("Ronda")) {
            vista.showJuego();
        }
        vista.append(msg);
    }

    /**
     * Ejecuta un lanzamiento y presenta resultados y mensajes. Controla la
     * presentacion de los lanzamientos paso a paso. Muestra el resultado final
     * cuando la partioda termina.
     */
    private void onLanzarPaso() {
        var r = controlGeneral.siguienteLanzamiento();
        if (r.hayError()) {
            vista.append(r.error);
            return;
        }

        //Preparar datos para mostrar paso a paso 
        String equipoNombre = (r.equipo != null ? r.equipo
                .getNombre() : "Equipo");
        String jugadorNombre = (r.jugador != null ? r.jugador
                .getNombre() : "Jugador");
        String jugada = (r.jugada != null ? r.jugada : "");

        // Muestra el dialogo con la accion del jugador
        vista.mostrarDialogo("Lanzamiento", jugadorNombre + " : " + jugada);

        //Info del partido en el momento
        vista.append(String
                .format("[%s] %s obtuvo %d (%s). Totales A=%d, B=%d%n",
                        equipoNombre, jugadorNombre, r.puntosLanzamiento,
                        jugada, r.totalA, r.totalB));
        if (r.muerteSubita) {
            vista.append("** Muerte súbita **\n");
        }

        // Muestra el resultado final y el final de la carrera 
        if (r.finPartida && r.ganador != null) {
            String ganadorEquipo = r.ganador.getNombre();
            List<String> ganadorJugadores = new ArrayList<>();
            for (Jugador j : r.ganador.getJugadores()) {
                ganadorJugadores.add(j.getNombre());
            }

            StringBuilder sb = new StringBuilder("Ganó ").
                    append(ganadorEquipo).append("\n");
            for (String nom : ganadorJugadores) {
                sb.append("- ").append(nom).append("\n");
            }
            vista.mostrarDialogo("Resultado", sb.toString());
        }
    }

    /**
     * Inicia una nueva ronda y muestra el mensaje en pantalla
     *
     */
    private void onOtraRonda() {
        vista.append(controlGeneral.nuevaRonda());
    }

    /**
     * Re4cupera el historial de lanzamientos y muestra la informacion
     *
     */
    private void onVerHistorial() {
        List<ArchivoAccesoAleatorio.Registro> registros = controlGeneral.
                obtenerHistorial();

        // CAMBIO: En lugar de vista.mostrarHistorial(registros)
        // Ahora la lógica está aquí:
        vista.append("\n=== HISTORIAL ===\n");
        for (var r : registros) {
            vista.append(r.toString());
            vista.append("\n");
        }
    }

    /**
     * Guarda el estado actual, muestra el historial y cierra la aplicacion
     */
    private void onSalir() {
        controlGeneral.guardarEstado();
        onVerHistorial();
        vista.cerrarAplicacion();
    }

    /**
     * Actualiza la lista de equipos mostrados en la interfaz grafica para la
     * seleccion.
     *
     *
     * @param equipos Lista de nombres de equipos disponibles
     */

    public void actualizarEquiposEnVista(List equipos) {
        vista.setEquipos(equipos);
    }
    /**
     * Invoca la interfaz grafica
     */
    public void mostrar() {
        SwingUtilities.invokeLater(() -> {
            vista.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            vista.setVisible(true);
        });
    }
    
    /**
     * Regresa a la pantalla de configuracion inicial
     */
    private void onVolver() {
        vista.showConfig();
    }

}
