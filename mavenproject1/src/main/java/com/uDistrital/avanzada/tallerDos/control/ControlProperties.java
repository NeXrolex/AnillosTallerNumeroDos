/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.control;

import com.uDistrital.avanzada.tallerDos.modelo.ArchivoProperties;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author santi
 */
public class ControlProperties {
  private final ArchivoProperties archivoProps = new ArchivoProperties();

  /** Devuelve equipos crudos (EquipoRaw) leídos del archivo. */
  public List<ArchivoProperties
        .EquipoRaw> cargarCrudo(File f) throws IOException {
    return archivoProps.leer(f);
  }
}
