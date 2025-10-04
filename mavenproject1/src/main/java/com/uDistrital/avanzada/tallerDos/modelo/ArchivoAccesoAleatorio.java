/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.modelo;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**

 * @author Alex
 * @author Jeison
 */
public class ArchivoAccesoAleatorio implements Closeable {

    // Archivo de acceso biinario de acceso aleatorio
    private final RandomAccessFile raf;
    // Tamaño fijo de la clave en bites
    private static final int KEY = 8, STR = 32, RES = 1;

    /**
     * Constructor que abre o crea el archivo para la lectura y escritura
     *
     * @param f Archivo que recibe de acceso aleatorio
     * @throws FileNotFoundException Si no se logra abrir el archivo
     */
    public ArchivoAccesoAleatorio(File f) throws FileNotFoundException {
        this.raf = new RandomAccessFile(f, "rw");
    }

    /**
     *
     * @param clave Clave para identificacion de la cadena
     * @param equipo Nombre del equipo
     * @param j1 Nombre Jugador 1
     * @param j2 Nombre Jugador 2
     * @param j3 Nombre Jugador 3
     * @param j4 Nombre Jugador 4
     * @param gano Resultado boleano que indica si gano
     * @throws IOException Si ocurre un error de escritura
     */
    public synchronized void anexar(String clave, String equipo,
            String j1, String j2, String j3, String j4,
            boolean gano) throws IOException {
        raf.seek(raf.length());
        writeFixed(clave, KEY);
        writeFixed(equipo, STR);
        writeFixed(j1, STR);
        writeFixed(j2, STR);
        writeFixed(j3, STR);
        writeFixed(j4, STR);
        raf.writeBoolean(gano);
    }

    /**
     * Se encarga de leer los archivos de accesos aleatorio y devolverlos como
     * una lista de registro
     *
     * @return Retorna la lista de registros almacenados
     * @throws IOException Si ocurre un error Durante la lectura
     */
    public synchronized List<Registro> leerTodos() throws IOException {
        List<Registro> out = new ArrayList<>();
        long recSize = KEY + STR + STR * 4L + RES; // 8 + 32 + 128 + 1 = 169
        long total = raf.length() / recSize;
        raf.seek(0);
        for (long i = 0; i < total; i++) {
            String clave = readFixed(KEY);
            String equipo = readFixed(STR);
            String j1 = readFixed(STR);
            String j2 = readFixed(STR);
            String j3 = readFixed(STR);
            String j4 = readFixed(STR);
            boolean gano = raf.readBoolean();
            out.add(new Registro(clave, equipo, j1, j2, j3, j4, gano));
        }
        return out;
    }

    /**
     * Escibe una cadena de lomgitud fija en el archivo
     *
     * @param s Cadena a escribir
     * @param size Longitud fija en bites
     * @throws IOException Si Ocurre un error de escritura
     */
    private void writeFixed(String s, int size) throws IOException {
        byte[] d = s == null ? new byte[0] : s.getBytes(StandardCharsets.UTF_8);
        if (d.length > size) {
            raf.write(d, 0, size);
        } else {
            raf.write(d);
            raf.write(new byte[size - d.length]);
        }
    }

    /**
     * Lee una cadena de caracteres de longitud fija desde el archivo
     *
     * @param size Longitud fija a leer
     * @return Cadena leida
     * @throws IOException Si ocurre un error de lectura
     */
    private String readFixed(int size) throws IOException {
        byte[] buf = new byte[size];
        raf.readFully(buf);
        int end = buf.length;
        while (end > 0 && buf[end - 1] == 0) {
            end--;
        }
        return new String(buf, 0, end, StandardCharsets.UTF_8);
    }

    /**
     * Cierra el archivo
     *
     * @throws IOException si ocurre un error al cerrar el archivo
     */
    @Override
    public void close() throws IOException {
        raf.close();
    }

    /**
     * DTO de un registro del historial.
     */
    public static class Registro {

        /**
         * Clave identificadora, nombre del equipo y de los jugadores
         */
        public final String clave, equipo, j1, j2, j3, j4;
        /**
         * Resultado del juego
         */
        public final boolean gano;

        /**
         *
         * @param clave Clave para identificacion de la cadena
         * @param equipo Nombre del equipo
         * @param j1 Nombre Jugador 1
         * @param j2 Nombre Jugador 2
         * @param j3 Nombre Jugador 3
         * @param j4 Nombre Jugador 4
         * @param gano Resultado boleano que indica si gano
         */
        public Registro(String clave, String equipo, String j1, String j2,
                String j3, String j4, boolean gano) {
            this.clave = clave;
            this.equipo = equipo;
            this.j1 = j1;
            this.j2 = j2;
            this.j3 = j3;
            this.j4 = j4;
            this.gano = gano;
        }

        /**
         * Regrea una representacion legible del equipo
         *
         * @return Cadena con el resumen del registro
         */

        @Override
        public String toString() {
            return String.format("%s | %s | %s, %s, %s, %s | %s",
                    clave, equipo, j1, j2, j3, j4, gano ? "GANO" : "PERDIO");
        }
    }
}
