/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao;


import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.db.ConexionDB;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProveedorDAO {

    public boolean insertarProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO Proveedor (nombre, tipo, pais) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, proveedor.getNombre());
            pstmt.setInt(2, proveedor.getTipo());
            pstmt.setString(3, proveedor.getPais());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar proveedor '" + proveedor.getNombre() + "': " + e.getMessage());
            return false;
        }
    }
    
    public int obtenerIdPorNombre(String nombre) {
        String sql = "SELECT id_proveedor FROM proveedor WHERE nombre = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
         
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_proveedor"); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar ID del proveedor '" + nombre + "': " + e.getMessage());
        }
        return -1; // Retorna -1 si no existe
    }
}
