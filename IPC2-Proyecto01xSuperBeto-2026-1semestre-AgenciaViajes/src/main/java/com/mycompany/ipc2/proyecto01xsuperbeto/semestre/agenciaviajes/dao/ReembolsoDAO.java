/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao;

import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.db.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author cesar
 */
public class ReembolsoDAO {
    
    public boolean procesarCancelacion(int idReservacion, double monto, int porcentaje, String fecha) {
        String sqlReembolso = "INSERT INTO reembolso (id_reservacion, monto_devuelto, porcentaje_reembolso, fecha_reembolso, observaciones) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateReserva = "UPDATE reservacion SET estado = 'Cancelada' WHERE id_reservacion = ?";
        
        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciamos la transacción

            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlReembolso)) {
                pstmt1.setInt(1, idReservacion);
                pstmt1.setDouble(2, monto);
                pstmt1.setInt(3, porcentaje);
                pstmt1.setString(4, fecha);
                pstmt1.setString(5, "Cancelación solicitada por el cliente");
                pstmt1.executeUpdate();
            }

            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlUpdateReserva)) {
                pstmt2.setInt(1, idReservacion);
                pstmt2.executeUpdate();
            }

            conn.commit(); 
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Error al procesar cancelación: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}
    
    

