/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.control;

import com.uDistrital.avanzada.tallerDos.modelo.ArchivoSerializacion;
import com.uDistrital.avanzada.tallerDos.modelo.Equipo;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 
 *
 * @author Alex
 */
public class ControlSerializacion {

    /**
     * Ruta fija del archivo donde se serializan los equipos.
     */
    private final File archivoSerial = new File("Specs/data/equipos.ser");

    /**
     * Componente de persistencia que sabe leer/escribir el archivo.
     */
    private final ArchivoSerializacion serial = 
            new ArchivoSerializacion(archivoSerial);

    /**
     * Guarda equipos en disco mediante serialización binaria.
     *
     * @param equipos lista de equipos a guardar
     */
    public void serializarEquipos(List<Equipo> equipos) throws IOException {
        serial.guardar(equipos);
    }

    /**
     * Carga equipos desde disco.
     *
     * @return lista de equipos si existe el archivo, o lista vacía si no existe
     */
    public List<Equipo> deserializarEquipos() throws Exception {
        return serial.cargar();
    }
}
