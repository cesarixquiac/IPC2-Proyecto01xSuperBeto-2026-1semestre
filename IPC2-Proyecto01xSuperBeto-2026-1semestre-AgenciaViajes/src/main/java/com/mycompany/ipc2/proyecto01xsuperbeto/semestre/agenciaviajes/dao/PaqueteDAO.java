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
    
    public int obtenerIdPorNombre(String nombre) {
        String sql = "SELECT id_paquete FROM paquete WHERE nombre = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
         
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_paquete"); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar ID del paquete '" + nombre + "': " + e.getMessage());
        }
        return -1; 
    }

    public boolean insertarServicioPaquete(int idPaquete, int idProveedor, String descripcion, double costoAgencia) {
        String sql = "INSERT INTO servicio_paquete (id_paquete, id_proveedor, descripcion, costo_agencia) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPaquete);
            pstmt.setInt(2, idProveedor);
            pstmt.setString(3, descripcion);
            pstmt.setDouble(4, costoAgencia);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar servicio al paquete: " + e.getMessage());
            return false;
        }
    }
    
    public double obtenerPrecioVenta(int idPaquete) {
        String sql = "SELECT precio_venta FROM paquete WHERE id_paquete = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPaquete);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getDouble("precio_venta");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener precio del paquete: " + e.getMessage());
        }
        return 0.0;
    }
}
