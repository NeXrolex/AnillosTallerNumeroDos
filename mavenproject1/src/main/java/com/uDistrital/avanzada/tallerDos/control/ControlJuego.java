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
 * @author Alex
 */
public class ControlJuego {

    /**
     * Estado del partido (modelo puro).
     */
    private Juego juego;

    /**
     * Inicia una partida con dos equipos (solo datos).
     *
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
     * Ejecuta UN lanzamiento (para animación lenta en la Vista).
     *
     * @return DTO con datos para mostrar (o error)
     */
    public Lanzamiento siguienteLanzamiento() {
        if (juego == null || juego.getEquipoA() == null || juego.getEquipoB() == null) {
            return Lanzamiento.error("Inicie la partida primero\n");
        }

        // Equipo en turno
        Equipo eq = juego.isTurnoEquipoA() ? juego.getEquipoA() : juego.getEquipoB();

        // Validación: si ya tiraron los 4 y no hay muerte súbita, no se puede lanzar
        if (juego.getIdxJugador() >= 4 && !juego.isEnMuerteSubita()) {
            return Lanzamiento.error("Mano ya completada, presione 'Otra ronda' o espere cambio de equipo\n");
        }

        // Selección de jugador (en muerte súbita usamos posición 0 como simplificación de parejas)
        Jugador jugador = jugadores(eq).get(juego.isEnMuerteSubita() ? 0 : juego.getIdxJugador());

        // Puntuación aleatoria según las probabilidades del taller
        int pts = puntajeAleatorio();
        String jugada = nombreJugada(pts, juego); // nombres usando los valores del modelo

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
            boolean finPartida = false;
            Equipo ganador = null;

            if (finMano) {
                if (juego.isTurnoEquipoA()) {
                    // Termina mano de A -> turno para B
                    juego.setTurnoEquipoA(false);
                    juego.setIdxJugador(0);
                } else {
                    // Termina mano de B -> evaluar meta de puntos (desde el modelo)
                    int meta = juego.getMetaPuntos(); // por defecto 21 (taller)
                    if (juego.getPuntosA() >= meta || juego.getPuntosB() >= meta) {
                        if (juego.getPuntosA() == juego.getPuntosB()) {
                            // Empate al llegar a la meta -> muerte súbita
                            juego.setEnMuerteSubita(true);
                            juego.setIdxJugador(0);
                            cambioEquipo = false; // seguimos en MS
                        } else {
                            // Hay ganador por superar la meta
                            finPartida = true;
                            ganador = (juego.getPuntosA() > juego.getPuntosB()) ? juego.getEquipoA() : juego.getEquipoB();
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
                        : (juego.getPuntosA() > juego.getPuntosB() ? juego.getEquipoA() : juego.getEquipoB());
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
     * Mensaje informativo (la regla real se aplica en siguienteLanzamiento).
     */
    public String nuevaRonda() {
        if (juego == null || juego.getEquipoA() == null || juego.getEquipoB() == null) {
            return "Inicie la partida primero\n";
        }
        // Mensaje más claro: la ronda avanza cuando B termina su mano
        return "Si ya jugaron A y B, la siguiente ronda se activará al finalizar la mano actual. Juega: "
                + (juego.isTurnoEquipoA() ? "Equipo A\n" : "Equipo B\n");
    }

    // ======== Utilidades privadas (lógica, no-UI) ========
    private List<Jugador> jugadores(Equipo e) {
        return e.getJugadores();
    }

    /**
     * Probabilidades aproximadas del taller.
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
     * Traduce puntaje a nombre usando los valores almacenados en el modelo
     * (POJO).
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

    // ======== DTO para la Vista ========
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

        public Lanzamiento(Equipo eq, Jugador j, int pts, String jugada, int totA, int totB,
                boolean finMano, boolean cambioEquipo, boolean finPartida, Equipo ganador,
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

        public static Lanzamiento error(String m) {
            return new Lanzamiento(m);
        }

        public boolean hayError() {
            return error != null;
        }
    }
}
