/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.vista;

import com.uDistrital.avanzada.tallerDos.modelo.ArchivoAccesoAleatorio;
import com.uDistrital.avanzada.tallerDos.modelo.Equipo;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *Ventana principal de la aplicacion, se encarga de generar la interfas grafica
 * mediante dos "ventanas"principales
 * @author Alex, Jeison
 */
public class VentanaPrincipal extends JFrame {

    public final JButton btnCargarProps
            = new JButton("Cargar Equipos (.properties)");
    public final JButton btnIniciar = new JButton("Iniciar Partida");
    public final JButton btnLanzar = new JButton("Lanzar Argolla");
    public final JButton btnOtraRonda = new JButton("Jugar otra ronda");
    public final JButton btnHistorial = new JButton("Ver Historial");
    public final JButton btnSalir = new JButton("Salir");
    public final JButton btnVolver = new JButton("Volver");

    public final JComboBox<String> cbEquipoA = new JComboBox<>();
    public final JComboBox<String> cbEquipoB = new JComboBox<>();
    public final JTextArea consola = new JTextArea(12, 46);

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final JPanel pnlConfig = new JPanel();
    private final JPanel pnlJuego = new JPanel();
    /**
     * Constructor que inicializala ventana principal y sus componentes
     * configura y organiza en sus paneles correspondientes
     * establece propiedades de la ventana y la hace visible
     */
    public VentanaPrincipal() {
        super("Argolla Llanera – Taller 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        consola.setEditable(false);

        // Config
        pnlConfig.setLayout(new GridLayout(0, 1, 8, 8));
        pnlConfig.add(btnCargarProps);
        pnlConfig.add(new JLabel("Equipo A:"));
        pnlConfig.add(cbEquipoA);
        pnlConfig.add(new JLabel("Equipo B:"));
        pnlConfig.add(cbEquipoB);
        pnlConfig.add(btnIniciar);
        pnlConfig.add(btnSalir);

        // Juego
        pnlJuego.setLayout(new BorderLayout());
        JPanel top = new JPanel();
        top.add(btnLanzar);
        top.add(btnOtraRonda);
        top.add(btnHistorial);
        top.add(btnVolver);
        pnlJuego.add(top, BorderLayout.NORTH);
        pnlJuego.add(new JScrollPane(consola), BorderLayout.CENTER);

        root.add(pnlConfig, "CFG");
        root.add(pnlJuego, "GAME");
        setContentPane(root);
        cards.show(root, "CFG");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Cambia la vista a la configuracion de equipos
     */
    public void showConfig() {
        cards.show(root, "CFG");
    }
    /**
     * Cambiala vista a la pantalla del juego
     */
    public void showJuego() {
        cards.show(root, "GAME");
    }
    /**
     * Agrega el area de consola para mostrar informacion del juego
     * @param texto Texto a agregar al area de consola  
     */
    public void append(String texto) {
        consola.append(texto);
    }
    /**
     * Se encarga de actualizar el combobox con la lita de equipos disponibles  
     * @param equipos lista de equipos
     */
    public void setEquipos(List<Equipo> equipos) {
        cbEquipoA.removeAllItems();
        cbEquipoB.removeAllItems();
        for (Equipo e : equipos) {
            cbEquipoA.addItem(e.getNombre());
            cbEquipoB.addItem(e.getNombre());
        }
    }
    /**
     * Se encarga de mostrar un dialogo para buscar el archivo properties
     * @return archivo seleccionado
     */
    public File seleccionarProperties() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccione archivo .properties");
        int r = fc.showOpenDialog(this);
        return r == JFileChooser.APPROVE_OPTION ? fc.getSelectedFile() : null;
    }
    /**
     * Muestra un diálogo informativo con título y mensaje personalizados
     * @param titulo Titulo del dialogo
     * @param mensaje Contenido del mensaje
     */
    public void mostrarDialogo(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * Cierra la aplicacion y muestra los ganadores de la partida en consola
     * @param registros Registros del historial 
     */
    public void cerrarAplicacion(List<ArchivoAccesoAleatorio.Registro> registros) {
        dispose();
        System.out.println("++++++++++++++++++++");
        System.out.println("");
        System.out.println(registros);
        System.out.println("");
        System.out.println("++++++++++++++++++++");
        System.exit(0);
    }
}
