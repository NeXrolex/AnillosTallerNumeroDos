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
 * @author Alex
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

        vista.btnCargarProps.addActionListener(this);
        vista.btnIniciar.addActionListener(this);
        vista.btnLanzar.addActionListener(this);
        vista.btnOtraRonda.addActionListener(this);
        vista.btnHistorial.addActionListener(this);
        vista.btnSalir.addActionListener(this);
        vista.btnVolver.addActionListener(this);
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
        
        // CAMBIO: En lugar de vista.mostrarLanzamiento(...) 
        // Ahora la lógica está aquí en el controlador:
        
        // 1) Popup paso-a-paso
        vista.mostrarDialogo("Lanzamiento", jugadorNombre + " : " + jugada);

        // 2) Consola
        vista.append(String.format("[%s] %s obtuvo %d (%s). Totales A=%d, B=%d%n",
                equipoNombre, jugadorNombre, r.puntosLanzamiento, jugada, r.totalA, r.totalB));
        if (r.muerteSubita) {
            vista.append("** Muerte súbita **\n");
        }

        // 3) Resultado final
        if (r.finPartida && r.ganador != null) {
            String ganadorEquipo = r.ganador.getNombre();
            List<String> ganadorJugadores = new ArrayList<>();
            for (Jugador j : r.ganador.getJugadores()) {
                ganadorJugadores.add(j.getNombre());
            }
            
            StringBuilder sb = new StringBuilder("Ganó ").append(ganadorEquipo).append("\n");
            for (String nom : ganadorJugadores) {
                sb.append("- ").append(nom).append("\n");
            }
            vista.mostrarDialogo("Resultado", sb.toString());
        }
    }

    private void onOtraRonda() {
        vista.append(controlGeneral.nuevaRonda());
    }

    /**
     * CAMBIO: Se movió la lógica de formateo desde VentanaPrincipal a aquí.
     */
    private void onVerHistorial() {
        List<ArchivoAccesoAleatorio.Registro> registros = controlGeneral.obtenerHistorial();
        
        // CAMBIO: En lugar de vista.mostrarHistorial(registros)
        // Ahora la lógica está aquí:
        vista.append("\n=== HISTORIAL ===\n");
        for (var r : registros) {
            vista.append(r.toString());
            vista.append("\n");
        }
    }

    private void onSalir() {
        controlGeneral.guardarEstado();
        onVerHistorial();
        vista.cerrarAplicacion();
    }

    public void actualizarEquiposEnVista(List equipos) {
        vista.setEquipos(equipos);
    }

    public void mostrar() {
        SwingUtilities.invokeLater(() -> {
            vista.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            vista.setVisible(true);
        });
    }
    
    private void onVolver(){
        vista.showConfig();
    }

}
