/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.control;

import com.uDistrital.avanzada.tallerDos.modelo.Equipo;
import com.uDistrital.avanzada.tallerDos.modelo.Juego;
import com.uDistrital.avanzada.tallerDos.modelo.Jugador;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Alex, Santiago
 */
public class ControlJuego {

    /**
     * Estado del partido (modelo puro).
     */
    private Juego juego;
    private boolean finPartida;
    private int contadorRondas = 1;

    /**
     * Inicia una partida con dos equipos (solo datos).
     * @param equipoA primer equipo participante
     * @param equipoB segundo equipo participante
     * @return texto informativo para la vista
     */
    public String iniciar(Equipo equipoA, Equipo equipoB) {
        if (equipoA == null || equipoB == null || equipoA == equipoB) {
            return "Seleccione dos equipos distintos\n";
        }
        juego = new Juego();
        juego.setEquipoA(equipoA);
        juego.setEquipoB(equipoB);
        juego.setRondaActual(1);
        juego.setPuntosA(0);
        juego.setPuntosB(0);
        juego.setIdxJugador(0);
        juego.setTurnoEquipoA(true);
        juego.setEnMuerteSubita(false);
        return "Ronda 1 iniciada\n";
    }

    /**
     * Encargado de hacer un lanzamiento individual, tambien gestiona el flujo
     * del juego turnos, cambios de equipo,finalización de manos, evaluación de
     * ganadores y muerte súbita.
     * @return Lanzamiento con todos los datos correspondientes o error si no es
     */
    public Lanzamiento siguienteLanzamiento() {
        if (juego == null || juego.getEquipoA() == null
                || juego.getEquipoB() == null) {
            return Lanzamiento.error("Inicie la partida primero\n");
        }

        // Equipo en turno
        Equipo eq = juego.isTurnoEquipoA() ? juego
                .getEquipoA() : juego.getEquipoB();

        // Validación: si ya tiraron los 4 y no hay muerte súbita, 
        //no se puede lanzar
        if (juego.getIdxJugador() >= 4 && !juego.isEnMuerteSubita()) {
            return Lanzamiento.error("Mano ya completada, presione"
                    + " 'Otra ronda' o espere cambio de equipo\n");
        }

        // Selección de jugador (en muerte súbita usamos posición 
        //0 como simplificación de parejas)
        Jugador jugador = jugadores(eq).get(juego
                .isEnMuerteSubita() ? 0 : juego.getIdxJugador());

        // Puntuación aleatoria según las probabilidades del taller
        int pts = puntajeAleatorio();
        String jugada = nombreJugada(pts, juego); // nombres usando los valores 

        if (!juego.isEnMuerteSubita()) {
            // Suma puntos al equipo en turno
            if (juego.isTurnoEquipoA()) {
                juego.setPuntosA(juego.getPuntosA() + pts);
            } else {
                juego.setPuntosB(juego.getPuntosB() + pts);
            }

            // Avanza jugador dentro de la mano
            juego.setIdxJugador(juego.getIdxJugador() + 1);

            boolean finMano = (juego.getIdxJugador() == 4);
            boolean cambioEquipo = finMano;
            this.finPartida = false;
            Equipo ganador = null;

            if (finMano) {
                if (juego.isTurnoEquipoA()) {
                    // Termina mano de A -> turno para B
                    juego.setTurnoEquipoA(false);
                    juego.setIdxJugador(0);
                } else {
                    // Termina mano de B -> evaluar 
                    //meta de puntos (desde el modelo)
                    int meta = juego.getMetaPuntos(); // por defecto 21 (taller)
                    if (juego.getPuntosA() >= meta || juego
                            .getPuntosB() >= meta) {
                        if (juego.getPuntosA() == juego.getPuntosB()) {
                            // Empate al llegar a la meta -> muerte súbita
                            juego.setEnMuerteSubita(true);
                            juego.setIdxJugador(0);
                            cambioEquipo = false; // seguimos en MS
                        } else {
                            // Hay ganador por superar la meta
                            this.finPartida = true;
                            ganador = (juego.getPuntosA() > juego
                                    .getPuntosB()) ? juego.getEquipoA() : juego
                                            .getEquipoB();
                        }
                    } else {
                        // Nadie llegó a la meta -> nueva ronda, arranca A
                        juego.setRondaActual(juego.getRondaActual() + 1);
                        juego.setTurnoEquipoA(true);
                        juego.setIdxJugador(0);
                    }
                }
            }

            return new Lanzamiento(
                    eq, jugador, pts, jugada,
                    juego.getPuntosA(), juego.getPuntosB(),
                    finMano, cambioEquipo, finPartida, ganador,
                    juego.isEnMuerteSubita(), juego.getRondaActual(), null
            );

        } else {
            // *** MUERTE SÚBITA ***
            if (juego.isTurnoEquipoA()) {
                juego.setPuntosA(juego.getPuntosA() + pts);
                juego.setTurnoEquipoA(false); // ahora lanza B
                return new Lanzamiento(
                        eq, jugador, pts, jugada,
                        juego.getPuntosA(), juego.getPuntosB(),
                        false, true, false, null,
                        true, juego.getRondaActual(), "PAIR_A"
                );
            } else {
                juego.setPuntosB(juego.getPuntosB() + pts);
                Equipo ganador = (juego.getPuntosA() == juego.getPuntosB())
                        ? null
                        : (juego.getPuntosA() > juego.getPuntosB() ? juego
                        .getEquipoA() : juego.getEquipoB());
                boolean fin = (ganador != null);
                if (!fin) {
                    // Sigue MS
                    juego.setTurnoEquipoA(true);
                    return new Lanzamiento(
                            eq, jugador, pts, jugada,
                            juego.getPuntosA(), juego.getPuntosB(),
                            false, true, false, null,
                            true, juego.getRondaActual(), "PAIR_B_CONT"
                    );
                } else {
                    // Termina MS con ganador
                    return new Lanzamiento(
                            eq, jugador, pts, jugada,
                            juego.getPuntosA(), juego.getPuntosB(),
                            false, true, true, ganador,
                            true, juego.getRondaActual(), "PAIR_B_END"
                    );
                }
            }
        }
    }

    /**
     * Prepara una nueva ronda e indica el jugador la cantidad maxima de
     * partidas que se pueden jugar.
     * 
     * @return Mensajes para la vista
     */
    public String nuevaRonda() {
        if (finPartida == true) {
            if (contadorRondas >= 2) {
                return "Ya no puedes jugar más rondas,"
                        + " por favor cambia de equipos.";
            }
            contadorRondas++;
            finPartida = false; // reinicia el estado del juego
            if (juego != null) {
                juego.setRondaActual(contadorRondas);
                juego.setPuntosA(0);
                juego.setPuntosB(0);
                juego.setIdxJugador(0);
                juego.setTurnoEquipoA(true);
                juego.setEnMuerteSubita(false);
            }
            return "Partida nueva iniciada \n Ronda "
                    + (contadorRondas) + " iniciada";

        }
        // Mensaje más claro: la ronda avanza cuando B termina su mano
        return "Si ya jugaron A y B, la siguiente ronda se"
                + " activará al finalizar la mano actual. Juega: "
                + (juego.isTurnoEquipoA() ? "Equipo A\n" : "Equipo B\n");
    }

    /**
     * Obtiene la lista de jugadores 
     * 
     * @param e Parametro auziliar que hace referencia al equipo
     * @return Devuelve la lista de jugadores
     */
    private List<Jugador> jugadores(Equipo e) {
        return e.getJugadores();
    }
    /**
     * Genera un puntaje aleatorio siguiendo los parametros del taller :)
     * jhoncito pedimos piedad 
     */
    private int puntajeAleatorio() {
        int r = ThreadLocalRandom.current().nextInt(100);
        if (r < 3) {
            return 8;  // Moñona
        }
        if (r < 9) {
            return 5;  // Engarzada
        }
        if (r < 24) {
            return 3;  // Hueco
        }
        if (r < 44) {
            return 2;  // Palmo
        }
        if (r < 64) {
            return 1;  // Timbre
        }
        return 0;              // Otro
    }

    /**
     * Traduce el valor numerico de puntos a su nombre textual
     * 
     * @param pts Numero de puntos 
     * @param j Referencia de juego
     * @return Jugada
     */
    private String nombreJugada(int pts, Juego j) {
        if (pts == j.getPuntajeMonona()) {
            return "Moñona";
        }
        if (pts == j.getPuntajeEngarzada()) {
            return "Engarzada";
        }
        if (pts == j.getPuntajeHueco()) {
            return "Hueco";
        }
        if (pts == j.getPuntajePalmo()) {
            return "Palmo";
        }
        if (pts == j.getPuntajeTimbre()) {
            return "Timbre";
        }
        return "Otro";
    }

    /**
     * Data Transfer Object (DTO) que encapsula toda la información de un
     * lanzamiento realizado dentro de la partida.
     *
     * Su finalidad es transmitir datos a la capa de vista
     * sin exponer directamente el modelo interno.
     */
    public static class Lanzamiento {
        
        public final Equipo equipo;
        public final Jugador jugador;
        public final int puntosLanzamiento;
        public final String jugada;
        public final int totalA, totalB;
        public final boolean finMano, cambioEquipo, finPartida;
        public final Equipo ganador;
        public final boolean muerteSubita;
        public final int ronda;
        public final String meta;
        public final String error;
        
        /**
         * Constructor principal del DTO.
         *
         * @param eq Equipo que lanza.
         * @param j Jugador que lanza.
         * @param pts Puntos obtenidos.
         * @param jugada Nombre de la jugada.
         * @param totA Total acumulado del equipo A.
         * @param totB Total acumulado del equipo B.
         * @param finMano Si la mano terminó.
         * @param cambioEquipo Si se produce cambio de turno.
         * @param finPartida Si la partida terminó.
         * @param ganador Equipo ganador (si lo hay).
         * @param muerteSubita Si el juego está en modo muerte súbita.
         * @param ronda Ronda actual del juego.
         * @param meta Texto auxiliar (por ejemplo,
         * indicadores de emparejamiento).
         */
        public Lanzamiento(Equipo eq, Jugador j, int pts,
                String jugada, int totA, int totB,
                boolean finMano, boolean cambioEquipo, boolean finPartida,
                Equipo ganador,
                boolean muerteSubita, int ronda, String meta) {
            this.equipo = eq;
            this.jugador = j;
            this.puntosLanzamiento = pts;
            this.jugada = jugada;
            this.totalA = totA;
            this.totalB = totB;
            this.finMano = finMano;
            this.cambioEquipo = cambioEquipo;
            this.finPartida = finPartida;
            this.ganador = ganador;
            this.muerteSubita = muerteSubita;
            this.ronda = ronda;
            this.meta = meta;
            this.error = null;
        }
        /**
         * Constructor interno para representar lanzamientos con errores.
         *
         * @param error Mensaje descriptivo del error.
         */
        private Lanzamiento(String error) {
            this.equipo = null;
            this.jugador = null;
            this.puntosLanzamiento = 0;
            this.jugada = null;
            this.totalA = 0;
            this.totalB = 0;
            this.finMano = false;
            this.cambioEquipo = false;
            this.finPartida = false;
            this.ganador = null;
            this.muerteSubita = false;
            this.ronda = 0;
            this.meta = null;
            this.error = error;
        }
        
        /**
         * Crea un objeto {@link Lanzamiento} de error con un mensaje.
         *
         * @param m Mensaje de error.
         * @return Instancia de {@link Lanzamiento} con error.
         */
        public static Lanzamiento error(String m) {
            return new Lanzamiento(m);
        }
        
        /**
         * Indica si la instancia representa un caso de error.
         *
         * @return true si contiene error, false en caso contrario.
         */
        public boolean hayError() {
            return error != null;
        }

    }
}
