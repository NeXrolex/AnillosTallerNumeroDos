/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Clase para leer archivos properties que contienen informacion de equipos,
 * jugadore. Permite crgar equipos de hasta 4 jugadores
 *
 * @author Alex
 */
public class ArchivoProperties {

    //Lo mantenemos satic para no violar el SOLID
    /**
     * Datos crudos del jugador(Hace referencia solo a datos como nombre y
     * apodo);
     *
     */
    public static class JugadorRaw {

        private String nombre;
        private String apodo;

        public JugadorRaw(String nombre, String apodo) {
            this.nombre = nombre;
            this.apodo = apodo;
        }

        /**
         * Obtiene el nombre del jugador.
         *
         * @return Nombre del jugador
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * Establece el nombre del jugador.
         *
         * @param nombre Nuevo nombre para el jugador
         */
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        /**
         * Obtiene el apodo del jugador.
         *
         * @return Apodo del jugador
         */
        public String getApodo() {
            return apodo;
        }

        /**
         * Establece el apodo del jugador.
         *
         * @param apodo Nuevo apodo para el jugador
         */
        public void setApodo(String apodo) {
            this.apodo = apodo;
        }
    }

    /**
     * Datos crudo de equipo. Contiene 4 cugadores de orden 1 a 4.
     */
    public static class EquipoRaw {

        private String nombre;
        private final List<JugadorRaw> jugadores = new ArrayList<>(4);

        /**
         * Constructor por defecto que crea un equipo vacío.
         */
        public EquipoRaw() {
        }

        /**
         * Constructor que inicializa un equipo con nombre.
         *
         * @param nombre Nombre del equipo
         */
        public EquipoRaw(String nombre) {
            this.nombre = nombre;
        }

        /**
         * Obtiene el nombre del equipo.
         *
         * @return Nombre del equipo
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * Establece el nombre del equipo.
         *
         * @param nombre Nuevo nombre para el equipo
         */
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        /**
         * Obtiene la lista de jugadores del equipo. La lista está inicializada
         * para contener hasta 4 jugadores.
         *
         * @return Lista mutable de jugadores del equipo
         */
        public List<JugadorRaw> getJugadores() {
            return jugadores;
        }
    }

    /**
     * Lee y devuelve las lista desde 1 hasta la cantidad de equipos
     *
     * @param archivoProps Archivo properties a leer
     * @return Lista de equipos con 4 jugadores
     * @throws IOException Si el archivo no existe o los datos no son validos
     * @throws IllegalArgumentException Si el archivo es null
     * @throws FileNotFoundException Si el archivo no existe en el sistema
     */
    public List<EquipoRaw> leer(File archivoProps) throws IOException {
        //Validaciones basicas 
        if (archivoProps == null) {
            throw new IllegalArgumentException("Archivo .properties nulo");
        }
        if (!archivoProps.exists()) {
            throw new FileNotFoundException("No existe: " + archivoProps
                    .getAbsolutePath());
        }

        //Casrgamos los properties 
        Properties p = new Properties();
        try (var in = new FileInputStream(archivoProps)) {
            p.load(in);
        }
        //Detectamos n equipos continuos
        int n = contarEquiposContiguos(p);
        if (n == 0) {
            throw new IOException("No se encontraron equipos en el archivo"
                    + " (.name)");
        }

        //COnstruccion de datos crudo
        List<EquipoRaw> equipos = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            String prefix = "team." + i;
            String nomEquipo = p.getProperty(prefix + ".name", "Equipo" + i);

            EquipoRaw eq = new EquipoRaw(nomEquipo);
            //Equipos de 4 integrantes 
            for (int j = 1; j <= 4; j++) {
                String base = prefix + ".player." + j;
                String nombre = p.getProperty(base + ".name", "Jugador" + j);
                String apodo = p.getProperty(base + ".nickname", "Apodo" + j);
                eq.getJugadores().add(new JugadorRaw(nombre, apodo));
            }
            equipos.add(eq);
        }
        return equipos;
    }

    /**
     * Cuenta la cantidad de equipos contiguos existentes en el archivo de
     * propiedades. Busca claves con el patrón "team.i.name" desde 1 hasta
     * encontrar la primera ausente. Este método asegura que los equipos estén
     * numerados secuencialmente.
     *
     * @param p Properties ya cargados en memoria
     * @return Número de equipos detectados de forma contigua (0 si no hay
     * ninguno)
     */
    private int contarEquiposContiguos(Properties p) {
        int i = 1;
        while (p.containsKey("team." + i + ".name")) {
            i++;
        }
        return i - 1;
    }
}
