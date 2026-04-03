/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models;

/**
 *
 * @author cesar
 */
public class Usuario {
    private int idUsuario;
    private String username;
    private String password;
    private int idRol;

    // Constructor vacío
    public Usuario() {}

    // Constructor para insertar (sin ID, ya que es auto-incremental)
    public Usuario(String username, String password, int idRol) {
        this.username = username;
        this.password = password;
        this.idRol = idRol;
    }

   
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }
}
