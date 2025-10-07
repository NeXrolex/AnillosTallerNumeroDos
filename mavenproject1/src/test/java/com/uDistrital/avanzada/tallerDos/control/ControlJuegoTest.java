
package com.uDistrital.avanzada.tallerDos.control;

import com.uDistrital.avanzada.tallerDos.modelo.Equipo;
import com.uDistrital.avanzada.tallerDos.modelo.Jugador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ControlJuegoTest {

    private ControlJuego controlJuego;
    private Equipo losLlaneros;
    private Equipo lasJaguas;
    /**
     * Configuracion inicial antes de la prueba para Crear dos equipos con sus
     * jugadores correspondientes
     */
    @BeforeEach
    void setUp() {
        controlJuego = new ControlJuego();

        losLlaneros = new Equipo("Los Llaneros");
        losLlaneros.getJugadores().add(new Jugador("Camilo", "Cami"));
        losLlaneros.getJugadores().add(new Jugador("Daniela", "Dani"));
        losLlaneros.getJugadores().add(new Jugador("Felipe", "Pipe"));
        losLlaneros.getJugadores().add(new Jugador("Sara", "Sari"));

        lasJaguas = new Equipo("Las Jaguas");
        lasJaguas.getJugadores().add(new Jugador("Rafael", "Mono"));
        lasJaguas.getJugadores().add(new Jugador("Lucia", "Chispa"));
        lasJaguas.getJugadores().add(new Jugador("Jairo", "Zurdo"));
        lasJaguas.getJugadores().add(new Jugador("Ana", "Flecha"));
    }
    /**
     * Se encarga de verificar una partida con equipos validos
     */
    @Test
    void testIniciarPartidaConEquiposValidos() {
        String resultado = controlJuego.iniciar(losLlaneros, lasJaguas);

        assertTrue(resultado.contains("Ronda 1 iniciada"));
        assertFalse(resultado.contains("Error"));
        assertFalse(resultado.contains("Seleccione dos equipos distintos"));
    }
    /**
     * Verifica el inicio de una partida con equipos no validos
     */
    @Test
    void testIniciarPartidaConEquiposInvalidos() {
        String resultado1 = controlJuego.iniciar(null, lasJaguas);
        assertTrue(resultado1.contains("Seleccione dos equipos distintos"));

        String resultado2 = controlJuego.iniciar(losLlaneros, losLlaneros);
        assertTrue(resultado2.contains("Seleccione dos equipos distintos"));
    }
    /**
     * Prueba un lanzamiento sin partida iniciada genera error
     */
    @Test
    void testSiguienteLanzamientoSinPartidaIniciada() {
        ControlJuego.Lanzamiento lanzamiento = controlJuego.siguienteLanzamiento();

        assertTrue(lanzamiento.hayError());
        assertTrue(lanzamiento.error.contains("Inicie la partida primero"));
    }
    /**
     * Prueba un lanzamiento de una partida en curso devuelve datos correctos
     */
    @Test
    void testSiguienteLanzamientoConPartidaIniciada() {
        controlJuego.iniciar(losLlaneros, lasJaguas);

        ControlJuego.Lanzamiento lanzamiento = controlJuego.siguienteLanzamiento();

        assertFalse(lanzamiento.hayError());
        assertNotNull(lanzamiento.equipo);
        assertNotNull(lanzamiento.jugador);
        assertTrue(lanzamiento.puntosLanzamiento >= 0 && lanzamiento.puntosLanzamiento <= 8);
        assertNotNull(lanzamiento.jugada);
        assertEquals(1, lanzamiento.ronda);
        assertFalse(lanzamiento.muerteSubita);
    }
    /**
     * Prueba el flujo de los lanzamientos en una mano verificando el orden 
     * de los jugadores y el cambio de turnos entre equipos
     */
    @Test
    void testFlujoCompletoDeUnaMano() {
        controlJuego.iniciar(losLlaneros, lasJaguas);

        for (int i = 0; i < 4; i++) {
            ControlJuego.Lanzamiento lanzamiento = controlJuego.siguienteLanzamiento();

            assertFalse(lanzamiento.hayError());
            assertEquals("Los Llaneros", lanzamiento.equipo.getNombre());

            String nombreJugadorEsperado = losLlaneros.getJugadores().get(i).getNombre();
            assertEquals(nombreJugadorEsperado, lanzamiento.jugador.getNombre());
        }

        ControlJuego.Lanzamiento lanzamiento = controlJuego.siguienteLanzamiento();
        assertFalse(lanzamiento.hayError());
        assertEquals("Las Jaguas", lanzamiento.equipo.getNombre());
    }
    /**
     * verifica que el puntaje y la jugada sean correctas con la logica
     */
    @Test
    void testPuntajesYJugadas() {
        controlJuego.iniciar(losLlaneros, lasJaguas);

        ControlJuego.Lanzamiento lanzamiento = controlJuego.siguienteLanzamiento();

        int puntos = lanzamiento.puntosLanzamiento;
        String jugada = lanzamiento.jugada;

        if (puntos == 8) {
            assertEquals("Moñona", jugada);
        } else if (puntos == 5) {
            assertEquals("Engarzada", jugada);
        } else if (puntos == 3) {
            assertEquals("Hueco", jugada);
        } else if (puntos == 2) {
            assertEquals("Palmo", jugada);
        } else if (puntos == 1) {
            assertEquals("Timbre", jugada);
        } else if (puntos == 0) {
            assertEquals("Otro", jugada);
        } else {
            fail("Puntaje no esperado: " + puntos);
        }

        assertTrue(puntos >= 0 && puntos <= 8);
    }
    /**
     * Prueba que al iniciar una nueva ronda se retorne el mensaje adecuado
     */
    @Test
    void testNuevaRonda() {
        controlJuego.iniciar(losLlaneros, lasJaguas);

        String resultado = controlJuego.nuevaRonda();

        assertNotNull(resultado);
        assertTrue(resultado.toLowerCase().contains("ronda"));
    }
    /**
     * Verifica que solicitar nueva ronda sin partida activa genere mensaje de 
     * error.
     */
    @Test
    void testNuevaRondaSinPartidaIniciada() {
        String resultado = controlJuego.nuevaRonda();

        assertTrue(resultado.contains("Inicie la partida primero"));
    }
    /**
     * Valida que los jugadores sean asignados en el orden correcto
     */
    @Test
    void testJugadoresEnOrdenCorrecto() {
        controlJuego.iniciar(losLlaneros, lasJaguas);

        ControlJuego.Lanzamiento lanzamiento1 = controlJuego.siguienteLanzamiento();
        assertEquals("Camilo", lanzamiento1.jugador.getNombre());
        assertEquals("Cami", lanzamiento1.jugador.getApodo());

        ControlJuego.Lanzamiento lanzamiento2 = controlJuego.siguienteLanzamiento();
        assertEquals("Daniela", lanzamiento2.jugador.getNombre());
        assertEquals("Dani", lanzamiento2.jugador.getApodo());

        ControlJuego.Lanzamiento lanzamiento3 = controlJuego.siguienteLanzamiento();
        assertEquals("Felipe", lanzamiento3.jugador.getNombre());
        assertEquals("Pipe", lanzamiento3.jugador.getApodo());

        ControlJuego.Lanzamiento lanzamiento4 = controlJuego.siguienteLanzamiento();
        assertEquals("Sara", lanzamiento4.jugador.getNombre());
        assertEquals("Sari", lanzamiento4.jugador.getApodo());
    }
    /**
     * Verifica que despues de cambiar una mano el turno cambie
     */
    @Test
    void testCambioDeEquipoDespuesDeMano() {
        controlJuego.iniciar(losLlaneros, lasJaguas);

        for (int i = 0; i < 4; i++) {
            controlJuego.siguienteLanzamiento();
        }

        ControlJuego.Lanzamiento lanzamiento = controlJuego.siguienteLanzamiento();
        assertEquals("Las Jaguas", lanzamiento.equipo.getNombre());
        assertEquals("Rafael", lanzamiento.jugador.getNombre());
    }
}
