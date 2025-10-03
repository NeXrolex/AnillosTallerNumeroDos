/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alex
 */
public class ArchivoSerializacion {

    private final File archivo;

    public ArchivoSerializacion(File archivo) {
        this.archivo = archivo;
    }

    public void guardar(List<Equipo> equipos) throws IOException {
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(new ArrayList<>(equipos));
        }
    }

    @SuppressWarnings("unchecked")
    public List<Equipo> cargar() throws IOException, ClassNotFoundException {
        if (!archivo.exists()) {
            return List.of();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Equipo>) ois.readObject();
        }
    }
}
