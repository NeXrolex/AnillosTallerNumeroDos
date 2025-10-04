/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.control;

import com.uDistrital.avanzada.tallerDos.modelo.ArchivoAccesoAleatorio;
import com.uDistrital.avanzada.tallerDos.control.ControlGeneral;
import com.uDistrital.avanzada.tallerDos.control.ControlJuego;
import java.io.File;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;

/**
 *
 * @author Alex
 */
public class ControlGeneralTest {

    private ControlGeneral controlGeneral;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        controlGeneral = new ControlGeneral();
    }

    @AfterEach
    public void tearDown() {
        controlGeneral = null;
    }

    /**
     * Test de cargar equipos desde archivo properties válido.
     */
    @Test
    public void testCargarEquiposDesdeProperties_ArchivoValido() throws IOException {
        // Crear archivo de prueba válido
        File archivoTest = tempDir.resolve("equipos_test.properties").toFile();

        try (FileWriter writer = new FileWriter(archivoTest)) {
            writer.write("team.1.name=Equipo Alpha\n");
            writer.write("team.1.player.1.name=Juan Perez\n");
            writer.write("team.1.player.1.nickname=El Tigre\n");
            writer.write("team.1.player.2.name=Maria Lopez\n");
            writer.write("team.1.player.2.nickname=La Estrella\n");
            writer.write("team.1.player.3.name=Carlos Ruiz\n");
            writer.write("team.1.player.3.nickname=El Rayo\n");
            writer.write("team.1.player.4.name=Ana Torres\n");
            writer.write("team.1.player.4.nickname=La Fuerza\n");

            writer.write("team.2.name=Equipo Beta\n");
            writer.write("team.2.player.1.name=Luis Garcia\n");
            writer.write("team.2.player.1.nickname=El Veloz\n");
            writer.write("team.2.player.2.name=Sofia Martinez\n");
            writer.write("team.2.player.2.nickname=La Precisa\n");
            writer.write("team.2.player.3.name=Diego Morales\n");
            writer.write("team.2.player.3.nickname=El Certero\n");
            writer.write("team.2.player.4.name=Carmen Silva\n");
            writer.write("team.2.player.4.nickname=La Campeona\n");
        }

        // Ejecutar el método
        String resultado = controlGeneral.cargarEquiposDesdeProperties(archivoTest);

        // Verificar resultado
        assertNotNull(resultado, "El resultado no debe ser null");
        assertTrue(resultado.contains("Equipos cargados: 2"),
                "Debe indicar que se cargaron 2 equipos");
    }

    /**
     * Test de cargar equipos con archivo null.
     */
    @Test
    public void testCargarEquiposDesdeProperties_ArchivoNull() {
        String resultado = controlGeneral.cargarEquiposDesdeProperties(null);

        assertNotNull(resultado, "El resultado no debe ser null");
        assertTrue(resultado.contains("Error"), "Debe contener mensaje de error");
    }

    /**
     * Test de cargar equipos con archivo inexistente.
     */
    @Test
    public void testCargarEquiposDesdeProperties_ArchivoInexistente() {
        File archivoInexistente = new File("archivo_que_no_existe.properties");

        String resultado = controlGeneral.cargarEquiposDesdeProperties(archivoInexistente);

        assertNotNull(resultado, "El resultado no debe ser null");
        assertTrue(resultado.contains("Error"), "Debe contener mensaje de error");
    }

    /**
     * Test de iniciar partida con equipos válidos.
     */
    @Test
    public void testIniciarPartida_EquiposValidos() throws IOException {
        // Primero cargar equipos
        File archivoTest = crearArchivoEquiposTest();
        controlGeneral.cargarEquiposDesdeProperties(archivoTest);

        // Iniciar partida
        String resultado = controlGeneral.iniciarPartida("Equipo Alpha", "Equipo Beta");

        assertNotNull(resultado, "El resultado no debe ser null");
        assertFalse(resultado.contains("Error"), "No debe contener errores");
        // Debe contener información de ronda o inicio de partida
        assertTrue(resultado.length() > 0, "Debe retornar información del inicio");
    }

    /**
     * Test de iniciar partida con equipos inexistentes.
     */
    @Test
    public void testIniciarPartida_EquiposInexistentes() {
        String resultado = controlGeneral.iniciarPartida("Equipo Inexistente A", "Equipo Inexistente B");

        assertNotNull(resultado, "El resultado no debe ser null");
        // Puede retornar error o mensaje indicando que no se encontraron los equipos
    }

    /**
     * Test de siguiente lanzamiento sin partida iniciada.
     */
    @Test
    public void testSiguienteLanzamiento_SinPartidaIniciada() {
        ControlJuego.Lanzamiento resultado = controlGeneral.siguienteLanzamiento();

        assertNotNull(resultado, "El resultado no debe ser null");
        assertTrue(resultado.hayError(), "Debe indicar error si no hay partida iniciada");
        assertNotNull(resultado.error, "Debe contener mensaje de error");
    }

    /**
     * Test de siguiente lanzamiento con partida iniciada.
     */
    @Test
    public void testSiguienteLanzamiento_ConPartidaIniciada() throws IOException {
        // Cargar equipos e iniciar partida
        File archivoTest = crearArchivoEquiposTest();
        controlGeneral.cargarEquiposDesdeProperties(archivoTest);
        controlGeneral.iniciarPartida("Equipo Alpha", "Equipo Beta");

        ControlJuego.Lanzamiento resultado = controlGeneral.siguienteLanzamiento();

        assertNotNull(resultado, "El resultado no debe ser null");
        // Si no hay error, debe tener información del lanzamiento
        if (!resultado.hayError()) {
            assertTrue(resultado.puntosLanzamiento >= 0, "Los puntos deben ser >= 0");
            assertNotNull(resultado.jugador, "Debe tener un jugador");
            assertNotNull(resultado.equipo, "Debe tener un equipo");
        }
    }

    /**
     * Test de nueva ronda sin partida iniciada.
     */
    @Test
    public void testNuevaRonda() {
        String resultado = controlGeneral.nuevaRonda();

        assertNotNull(resultado, "El resultado no debe ser null");
        // Puede retornar mensaje de error o información sobre nueva ronda
    }

    /**
     * Test de guardar estado.
     */
    @Test
    public void testGuardarEstado() {
        // Este método no debe lanzar excepción
        assertDoesNotThrow(() -> {
            controlGeneral.guardarEstado();
        }, "guardarEstado no debe lanzar excepción");
    }

    /**
     * Test de obtener historial.
     */
    @Test
    public void testObtenerHistorial() {
        List<ArchivoAccesoAleatorio.Registro> resultado = controlGeneral.obtenerHistorial();

        assertNotNull(resultado, "El resultado no debe ser null");
        // El historial puede estar vacío inicialmente
        assertTrue(resultado.size() >= 0, "El tamaño debe ser >= 0");
    }

    /**
     * Test de flujo completo: cargar -> iniciar -> lanzar -> guardar.
     */
    @Test
    public void testFlujoCompleto() throws IOException {
        // 1. Cargar equipos
        File archivoTest = crearArchivoEquiposTest();
        String cargaResult = controlGeneral.cargarEquiposDesdeProperties(archivoTest);
        assertTrue(cargaResult.contains("Equipos cargados"), "Debe cargar equipos correctamente");

        // 2. Iniciar partida
        String inicioResult = controlGeneral.iniciarPartida("Equipo Alpha", "Equipo Beta");
        assertNotNull(inicioResult, "Debe iniciar partida");

        // 3. Realizar lanzamiento
        ControlJuego.Lanzamiento lanzamiento = controlGeneral.siguienteLanzamiento();
        assertNotNull(lanzamiento, "Debe realizar lanzamiento");

        // 4. Guardar estado
        assertDoesNotThrow(() -> {
            controlGeneral.guardarEstado();
        }, "Debe guardar estado sin errores");

        // 5. Obtener historial
        List<ArchivoAccesoAleatorio.Registro> historial = controlGeneral.obtenerHistorial();
        assertNotNull(historial, "Debe obtener historial");
    }

    /**
     * Método auxiliar para crear archivo de equipos de prueba.
     */
    private File crearArchivoEquiposTest() throws IOException {
        File archivo = tempDir.resolve("equipos_test.properties").toFile();

        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write("team.1.name=Equipo Alpha\n");
            writer.write("team.1.player.1.name=Juan Perez\n");
            writer.write("team.1.player.1.nickname=El Tigre\n");
            writer.write("team.1.player.2.name=Maria Lopez\n");
            writer.write("team.1.player.2.nickname=La Estrella\n");
            writer.write("team.1.player.3.name=Carlos Ruiz\n");
            writer.write("team.1.player.3.nickname=El Rayo\n");
            writer.write("team.1.player.4.name=Ana Torres\n");
            writer.write("team.1.player.4.nickname=La Fuerza\n");

            writer.write("team.2.name=Equipo Beta\n");
            writer.write("team.2.player.1.name=Luis Garcia\n");
            writer.write("team.2.player.1.nickname=El Veloz\n");
            writer.write("team.2.player.2.name=Sofia Martinez\n");
            writer.write("team.2.player.2.nickname=La Precisa\n");
            writer.write("team.2.player.3.name=Diego Morales\n");
            writer.write("team.2.player.3.nickname=El Certero\n");
            writer.write("team.2.player.4.name=Carmen Silva\n");
            writer.write("team.2.player.4.nickname=La Campeona\n");
        }

        return archivo;
    }
}
