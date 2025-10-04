/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uDistrital.avanzada.tallerDos.control;

import com.uDistrital.avanzada.tallerDos.modelo.ArchivoAccesoAleatorio;
import com.uDistrital.avanzada.tallerDos.modelo.Equipo;
import java.io.File;
import java.util.List;

/**
 *
 * @author santi
 */
public class ControlRAF {

  /** Ruta del archivo RAF donde se guarda el historial. */
  private final File archivoRaf = new File("Specs/data/resultados.raf");

  /**
   * Anexa un registro de ronda/partida al RAF.
   * @param clave   clave fija (ej: "RND00001", 8 chars máx. según formato RAF)
   * @param ganador equipo ganador (se leerán nombres de los 4 jugadores)
   * @param gano    bandera de resultado (true = ganó)
   */
  public void escribirRegistroRonda(String clave, Equipo ganador,
          boolean gano) throws Exception {
    if (archivoRaf.getParentFile() != null) {
      archivoRaf.getParentFile().mkdirs();
    }
    try (var raf = new ArchivoAccesoAleatorio(archivoRaf)) {
      var js = ganador.getJugadores();
      // El formato del registro en ArchivoAccesoAleatorio es:
      // clave[8] | equipo[32] | j1[32] | j2[32] | j3[32] | j4[32] | gano[1]
      raf.anexar(
          clave,
          ganador.getNombre(),
          js.get(0).getNombre(),
          js.get(1).getNombre(),
          js.get(2).getNombre(),
          js.get(3).getNombre(),
          gano
      );
    }
  }

  /**
   * Lee todos los registros del historial (en orden de escritura).
   * @return lista inmutable de registros DTO del RAF
   */
  public List<ArchivoAccesoAleatorio.Registro> leerHistorial() throws
          Exception {
    if (!archivoRaf.exists()) {
      return List.of();
    }
    try (var raf = new ArchivoAccesoAleatorio(archivoRaf)) {
      return raf.leerTodos();
    }
  }
}
