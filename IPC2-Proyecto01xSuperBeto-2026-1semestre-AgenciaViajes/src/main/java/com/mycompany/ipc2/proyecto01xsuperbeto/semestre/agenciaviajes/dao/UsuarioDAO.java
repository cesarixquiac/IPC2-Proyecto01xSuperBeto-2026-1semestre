/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao;
import com.google.gson.JsonObject;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.db.ConexionDB;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cesar
 */
public class UsuarioDAO {

    public boolean insertarUsuario(Usuario usuario) {
        
        String sql = "INSERT INTO Usuario (username, password, id_rol) VALUES (?, ?, ?)";
        

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getPassword());
            pstmt.setInt(3, usuario.getIdRol());
            
           
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
           
            System.err.println("Error al insertar usuario en BD: " + e.getMessage());
            return false;
        }
    }
    
    public int obtenerIdPorUsername(String username) {
        String sql = "SELECT id_usuario FROM usuario WHERE username = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id_usuario");
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return -1;
    }
    
    public int validarLogin(String username, String password) {
        String sql = "SELECT id_rol FROM usuario WHERE username = ? AND password = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_rol"); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar login: " + e.getMessage());
        }
        
        return -1; 
    }
    
    // Método para obtener todos los usuarios
    public List<JsonObject> listarUsuarios() {
        List<JsonObject> lista = new ArrayList<>();
        
        String sql = "SELECT username, id_rol, estado FROM usuario"; 
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                JsonObject json = new JsonObject();
                json.addProperty("username", rs.getString("username"));
                json.addProperty("id_rol", rs.getInt("id_rol"));
                
                
                json.addProperty("estado", rs.getString("estado")); 
                
                lista.add(json);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
    
    public boolean crearUsuario(String username, String password, int idRol) {
    String sql = "INSERT INTO usuario (username, password, id_rol, estado) VALUES (?, ?, ?, 'ACTIVO')";
    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setInt(3, idRol);
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) { return false; }
}

public boolean actualizarUsuario(String username, int idRol, String estado) {
        String sql = "UPDATE usuario SET id_rol = ?, estado = ? WHERE username = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idRol);
            pstmt.setString(2, estado);
            pstmt.setString(3, username);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { 
            System.err.println(" ERROR SQL al actualizar usuario: " + e.getMessage());
            return false; 
        }
    }
}
