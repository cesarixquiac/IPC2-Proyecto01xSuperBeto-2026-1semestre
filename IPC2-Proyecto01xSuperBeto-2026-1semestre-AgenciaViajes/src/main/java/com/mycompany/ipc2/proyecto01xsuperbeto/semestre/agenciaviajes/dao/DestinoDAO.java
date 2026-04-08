/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao;

import com.google.gson.JsonObject;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.db.ConexionDB;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Destino;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * @author cesar
 */
public class DestinoDAO {

    public boolean insertarDestino(Destino destino) {
        String sql = "INSERT INTO Destino (nombre, pais, descripcion) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, destino.getNombre());
            pstmt.setString(2, destino.getPais());
            pstmt.setString(3, destino.getDescripcion());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar destino '" + destino.getNombre() + "': " + e.getMessage());
            return false;
        }
    }
    
    public int obtenerIdPorNombre(String nombre) {
        String sql = "SELECT id_destino FROM Destino WHERE nombre = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
         
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_destino"); // Si lo encuentra, devuelve el ID 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar ID del destino '" + nombre + "': " + e.getMessage());
        }
        return -1; // Retornamos -1 si el destino no existe
    }
    
    public java.util.List<com.google.gson.JsonObject> listarDestinos() {
        java.util.List<com.google.gson.JsonObject> lista = new java.util.ArrayList<>();
        String sql = "SELECT id_destino, nombre, pais FROM Destino";
        
        try (java.sql.Connection conn = ConexionDB.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                com.google.gson.JsonObject json = new com.google.gson.JsonObject();
                json.addProperty("id_destino", rs.getInt("id_destino"));
                json.addProperty("nombre", rs.getString("nombre"));
                json.addProperty("pais", rs.getString("pais"));
                lista.add(json);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error al listar destinos: " + e.getMessage());
        }
        return lista;
    }
    
    public List<JsonObject> obtenerDestinos() {
        List<JsonObject> lista = new ArrayList<>();
        String sql = "SELECT * FROM destino";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                JsonObject j = new JsonObject();
                j.addProperty("id_destino", rs.getInt("id_destino"));
                j.addProperty("nombre", rs.getString("nombre"));
                j.addProperty("pais", rs.getString("pais"));
                j.addProperty("descripcion", rs.getString("descripcion"));
                j.addProperty("clima_epoca_ideal", rs.getString("clima_epoca_ideal"));
                j.addProperty("imagen_url", rs.getString("imagen_url"));
                lista.add(j);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean crearDestino(String nombre, String pais, String desc, String clima, String url) {
        String sql = "INSERT INTO destino (nombre, pais, descripcion, clima_epoca_ideal, imagen_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, pais);
            pstmt.setString(3, desc);
            pstmt.setString(4, clima);
            pstmt.setString(5, url);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    
    
}