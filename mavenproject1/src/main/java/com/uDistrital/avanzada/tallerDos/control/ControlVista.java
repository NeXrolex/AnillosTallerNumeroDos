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
 *
 * @author Alex
 */
public class ControlVista implements ActionListener {

    private final VentanaPrincipal vista;
    private final ControlGeneral controlGeneral;

    public ControlVista(ControlGeneral general) {
        this.controlGeneral = general;
        this.vista = new VentanaPrincipal();

        vista.btnCargarProps.addActionListener(this);
        vista.btnIniciar.addActionListener(this);
        vista.btnLanzar.addActionListener(this);
        vista.btnOtraRonda.addActionListener(this);
        vista.btnHistorial.addActionListener(this);
        vista.btnVolver.addActionListener(this);
        vista.btnSalir.addActionListener(this);
    }

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

    private void onCargarProps() {
        File f = vista.seleccionarProperties(); // JFileChooser en la Vista
        if (f == null) {
            return;
        }
        String msg = controlGeneral.cargarEquiposDesdeProperties(f);
        vista.append(msg);
    }

    private void onIniciar() {
        String a = (String) vista.cbEquipoA.getSelectedItem();
        String b = (String) vista.cbEquipoB.getSelectedItem();
        String msg = controlGeneral.iniciarPartida(a, b);
        if (msg.startsWith("Ronda")) {
            vista.showJuego();
        }
        vista.append(msg);
    }

    /**
     * Un lanzamiento por clic; la Vista muestra todo con JOptionPane y consola.
     */
    private void onLanzarPaso() {
        var r = controlGeneral.siguienteLanzamiento();
        if (r.hayError()) {
            vista.append(r.error);
            return;
        }

        String equipoNombre = (r.equipo != null ? r.equipo.getNombre() : "Equipo");
        String jugadorNombre = (r.jugador != null ? r.jugador.getNombre() : "Jugador");
        String jugada = (r.jugada != null ? r.jugada : "");

        String ganadorEquipo = null;
        List<String> ganadorJugadores = new ArrayList<>();
        if (r.finPartida && r.ganador != null) {
            ganadorEquipo = r.ganador.getNombre();
            for (Jugador j : r.ganador.getJugadores()) {
                ganadorJugadores.add(j.getNombre());
            }
        }

        vista.mostrarLanzamiento(
                equipoNombre,
                jugadorNombre,
                jugada,
                r.puntosLanzamiento,
                r.totalA,
                r.totalB,
                r.muerteSubita,
                r.finPartida,
                ganadorEquipo,
                ganadorJugadores
        );
    }

    private void onOtraRonda() {
        vista.append(controlGeneral.nuevaRonda());
    }

    private void onVerHistorial() {
        List<ArchivoAccesoAleatorio.Registro> registros = controlGeneral.obtenerHistorial();
        vista.mostrarHistorial(registros);
    }

    private void onSalir() {
        controlGeneral.guardarEstado();
        onVerHistorial();
        vista.cerrarAplicacion();
    }

    public void actualizarEquiposEnVista(List<com.uDistrital.avanzada.tallerDos.modelo.Equipo> equipos) {
        vista.setEquipos(equipos);
    }

    private void onVolver() {
       vista.showConfig();
    }
}
