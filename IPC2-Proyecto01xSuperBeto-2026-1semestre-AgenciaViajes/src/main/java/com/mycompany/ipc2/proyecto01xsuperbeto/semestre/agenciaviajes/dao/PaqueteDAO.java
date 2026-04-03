/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao;


import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.db.ConexionDB;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Paquete;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author cesar
 */
public class PaqueteDAO {
    public boolean insertarPaquete(Paquete paquete) {
        String sql = "INSERT INTO Paquete (nombre, id_destino, duracion_dias, precio_venta, capacidad_max, estado) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, paquete.getNombre());
            pstmt.setInt(2, paquete.getIdDestino()); // ID numerico
            pstmt.setInt(3, paquete.getDuracionDias());
            pstmt.setDouble(4, paquete.getPrecioVenta());
            pstmt.setInt(5, paquete.getCapacidadMax());
            pstmt.setBoolean(6, true);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar paquete '" + paquete.getNombre() + "': " + e.getMessage());
            return false;
        }
    }
}
