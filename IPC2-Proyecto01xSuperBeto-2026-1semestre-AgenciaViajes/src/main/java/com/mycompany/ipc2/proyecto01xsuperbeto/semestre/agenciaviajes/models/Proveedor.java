/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models;

/**
 *
 * @author cesar
 */
public class Proveedor {
    private int idProveedor;
    private String nombre;
    private int tipo;
    private String pais;

    public Proveedor() {}

    public Proveedor(String nombre, int tipo, String pais) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.pais = pais;
    }

    // Getters y Setters
    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getTipo() { return tipo; }
    public void setTipo(int tipo) { this.tipo = tipo; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
}
