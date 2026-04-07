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
    
    public int crearPaqueteRetornandoId(Paquete paquete) {
        String sql = "INSERT INTO Paquete (nombre, id_destino, duracion_dias, precio_venta, capacidad_max, estado) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, paquete.getNombre());
            pstmt.setInt(2, paquete.getIdDestino()); 
            pstmt.setInt(3, paquete.getDuracionDias());
            pstmt.setDouble(4, paquete.getPrecioVenta());
            pstmt.setInt(5, paquete.getCapacidadMax());
            pstmt.setBoolean(6, true);
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
               
                try (java.sql.ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Devolvemos el nuevo ID
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear paquete y obtener ID: " + e.getMessage());
        }
        return -1;
    }
    
    public java.util.List<com.google.gson.JsonObject> listarPaquetesDisponibles() {
        java.util.List<com.google.gson.JsonObject> lista = new java.util.ArrayList<>();
        // Traemos ID, nombre, precio y capacidad (solo los que están activos)
        String sql = "SELECT id_paquete, nombre, precio_venta, capacidad_max FROM paquete WHERE estado = true";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                com.google.gson.JsonObject json = new com.google.gson.JsonObject();
                json.addProperty("id_paquete", rs.getInt("id_paquete"));
                json.addProperty("nombre", rs.getString("nombre"));
                json.addProperty("precio_venta", rs.getDouble("precio_venta"));
                json.addProperty("capacidad_max", rs.getInt("capacidad_max"));
                lista.add(json);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar paquetes: " + e.getMessage());
        }
        return lista;
    }
    
    
    
    
}
