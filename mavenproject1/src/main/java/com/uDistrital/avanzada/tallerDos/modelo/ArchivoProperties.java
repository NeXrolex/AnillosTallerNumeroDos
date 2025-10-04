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
 *
 * @author Alex
 */
public class ArchivoProperties {

  /** DTO crudo de jugador (sin lógica de dominio). */
  public static class JugadorRaw {
    private String nombre;
    private String apodo;
    public JugadorRaw() {}
    public JugadorRaw(String nombre, String apodo) { this.nombre = nombre; this.apodo = apodo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApodo() { return apodo; }
    public void setApodo(String apodo) { this.apodo = apodo; }
  }

  /** DTO crudo de equipo (sin lógica de dominio). */
  public static class EquipoRaw {
    private String nombre;
    private final List<JugadorRaw> jugadores = new ArrayList<>(4);
    public EquipoRaw() {}
    public EquipoRaw(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<JugadorRaw> getJugadores() { return jugadores; }
  }

  /** Lee y devuelve lista 1..N de equipos crudos. */
  public List<EquipoRaw> leer(File archivoProps) throws IOException {
    if (archivoProps == null) throw new IllegalArgumentException("Archivo .properties nulo");
    if (!archivoProps.exists()) throw new FileNotFoundException("No existe: " + archivoProps.getAbsolutePath());

    Properties p = new Properties();
    try (var in = new FileInputStream(archivoProps)) {
      p.load(in);
    }

    int n = contarEquiposContiguos(p);
    if (n == 0) throw new IOException("No se encontraron equipos en el archivo (.name)");

    List<EquipoRaw> equipos = new ArrayList<>(n);
    for (int i = 1; i <= n; i++) {
      String prefix = "team." + i;
      String nomEquipo = p.getProperty(prefix + ".name", "Equipo" + i);

      EquipoRaw eq = new EquipoRaw(nomEquipo);
      for (int j = 1; j <= 4; j++) {
        String base = prefix + ".player." + j;
        String nombre = p.getProperty(base + ".name", "Jugador" + j);
        String apodo  = p.getProperty(base + ".nickname", "Apodo" + j);
        eq.getJugadores().add(new JugadorRaw(nombre, apodo));
      }
      equipos.add(eq);
    }
    return equipos;
  }

  /** Cuenta equipos 1..N mientras exista team.i.name (contiguo). */
  private int contarEquiposContiguos(Properties p) {
    int i = 1;
    while (p.containsKey("team." + i + ".name")) i++;
    return i - 1;
  }
}
