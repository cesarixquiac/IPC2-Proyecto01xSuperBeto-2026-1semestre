/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models;

/**
 *
 * @author cesar
 */
public class Paquete {
    private int idPaquete;
    private String nombre;
    private int idDestino; 
    private int duracionDias;
    private double precioVenta;
    private int capacidadMax;
    private boolean estado;

    public Paquete() {}

    public Paquete(String nombre, int idDestino, int duracionDias, double precioVenta, int capacidadMax) {
        this.nombre = nombre;
        this.idDestino = idDestino;
        this.duracionDias = duracionDias;
        this.precioVenta = precioVenta;
        this.capacidadMax = capacidadMax;
        this.estado = true; 
    }

   
    public String getNombre() { return nombre; }
    public int getIdDestino() { return idDestino; }
    public int getDuracionDias() { return duracionDias; }
    public double getPrecioVenta() { return precioVenta; }
    public int getCapacidadMax() { return capacidadMax; }
}
