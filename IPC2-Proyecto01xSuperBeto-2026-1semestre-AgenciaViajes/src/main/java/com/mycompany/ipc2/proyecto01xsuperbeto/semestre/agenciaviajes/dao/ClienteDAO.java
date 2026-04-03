/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao;


import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.db.ConexionDB;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 *
 * @author cesar
 */
public class ClienteDAO {
    public boolean insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO Cliente (dpi, nombre_completo, fecha_nacimiento, telefono, email, nacionalidad) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            
            SimpleDateFormat from = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd");
            String fechaMySQL = to.format(from.parse(cliente.getFechaNacimiento()));

            pstmt.setString(1, cliente.getDpi());
            pstmt.setString(2, cliente.getNombreCompleto());
            pstmt.setString(3, fechaMySQL);
            pstmt.setString(4, cliente.getTelefono());
            pstmt.setString(5, cliente.getEmail());
            pstmt.setString(6, cliente.getNacionalidad());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.err.println("Error al insertar cliente " + cliente.getDpi() + ": " + e.getMessage());
            return false;
        }
    }
}
