/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    
    private static final String URL = "jdbc:mysql://localhost:3310/agencia_viajes?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 
    
    public static Connection getConnection() {
        Connection conexion = null;
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver de MySQL no encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la Base de Datos: " + e.getMessage());
            e.printStackTrace();
        }
        return conexion;
    }
}
