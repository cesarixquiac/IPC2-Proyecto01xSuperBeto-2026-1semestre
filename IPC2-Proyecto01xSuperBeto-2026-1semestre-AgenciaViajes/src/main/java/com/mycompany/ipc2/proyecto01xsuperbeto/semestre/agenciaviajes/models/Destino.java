/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models;

/**
 *
 * @author cesar
 */
public class Destino {
    
    private int idDestino;
    private String nombre;
    private String pais;
    private String descripcion;

    public Destino() {}

    public Destino(String nombre, String pais, String descripcion) {
        this.nombre = nombre;
        this.pais = pais;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getIdDestino() { return idDestino; }
    public void setIdDestino(int idDestino) { this.idDestino = idDestino; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}