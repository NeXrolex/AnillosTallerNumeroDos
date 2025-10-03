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
 *
 * @author Alex
 */
public class ArchivoAccesoAleatorio implements Closeable {

    private final RandomAccessFile raf;
    private static final int KEY = 8, STR = 32, RES = 1;

    public ArchivoAccesoAleatorio(File f) throws FileNotFoundException {
        this.raf = new RandomAccessFile(f, "rw");
    }

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

    private void writeFixed(String s, int size) throws IOException {
        byte[] d = s == null ? new byte[0] : s.getBytes(StandardCharsets.UTF_8);
        if (d.length > size) {
            raf.write(d, 0, size);
        } else {
            raf.write(d);
            raf.write(new byte[size - d.length]);
        }
    }

    private String readFixed(int size) throws IOException {
        byte[] buf = new byte[size];
        raf.readFully(buf);
        int end = buf.length;
        while (end > 0 && buf[end - 1] == 0) {
            end--;
        }
        return new String(buf, 0, end, StandardCharsets.UTF_8);
    }

    @Override
    public void close() throws IOException {
        raf.close();
    }

    /**
     * DTO de un registro del historial.
     */
    public static class Registro {

        public final String clave, equipo, j1, j2, j3, j4;
        public final boolean gano;

        public Registro(String clave, String equipo, String j1, String j2, String j3, String j4, boolean gano) {
            this.clave = clave;
            this.equipo = equipo;
            this.j1 = j1;
            this.j2 = j2;
            this.j3 = j3;
            this.j4 = j4;
            this.gano = gano;
        }

        @Override
        public String toString() {
            return String.format("%s | %s | %s, %s, %s, %s | %s",
                    clave, equipo, j1, j2, j3, j4, gano ? "GANO" : "PERDIO");
        }
    }
}
