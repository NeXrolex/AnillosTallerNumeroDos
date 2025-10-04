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
 * Clase que maneja la serializacion y desserializacion de objetos Permite
 * guardar y cargar una lista
 *
 * @author Alex
 */
public class ArchivoSerializacion {

    //Aqui se guardan y cargan los datos
    private final File archivo;

    /**
     * Constructir que recibe el archivo
     *
     * @param archivo Archivo
     */
    public ArchivoSerializacion(File archivo) {
        this.archivo = archivo;
    }

    /**
     * Guarda una lista de objetos en el archivo, Convierte la lista en un
     * ArrayLista antes de escribir para evitar problemas
     *
     * @param equipos Lista de equipos
     * @throws IOException Si ocurre un error de entrada o salida en el proceso
     */
    public void guardar(List<Equipo> equipos) throws IOException {
        //Si el directorio padre no existe entonces lo crea 
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }
        try (ObjectOutputStream oos
                = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(new ArrayList<>(equipos));
        }
    }

    /**
     * Carga una lista de objetos desde el archivo deseriaslizando su contenido
     * si el arivo no existe devuelve una lista vacia para el control de errores
     *
     * @return Lista de objetos cargados desde el archivo
     * @throws IOException Si ocurre un problema de entrda y salida
     * @throws ClassNotFoundException Si no encuentra la clase del objeto
     */
    @SuppressWarnings("unchecked")
    /*Suprime las advertencias que ocurren 
    en casteos entre tipos genericos y no genericos, asi no llenamos el 
    compilador de mensajes inncesarios
     */
    public List<Equipo> cargar() throws IOException, ClassNotFoundException {
        //Si el archivo no existe, retorna una lista vacia 
        if (!archivo.exists()) {
            return List.of();
        }
        try (ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Equipo>) ois.readObject();
        }
    }
}
