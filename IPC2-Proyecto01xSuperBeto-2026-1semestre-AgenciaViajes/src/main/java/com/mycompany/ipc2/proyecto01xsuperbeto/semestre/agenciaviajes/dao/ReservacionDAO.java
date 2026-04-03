/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao;


import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.db.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 *
 * @author cesar
 */
public class ReservacionDAO {

   
    public int crearReservacion(int idPaquete, int idUsuario, String fechaViaje, int cantidadPasajeros, double costoTotal) {
        String sql = "INSERT INTO reservacion (id_paquete, id_usuario_agente, fecha_viaje, cantidad_pasajeros, costo_total, estado) VALUES (?, ?, ?, ?, ?, 'Pendiente')";
        int idGenerado = -1;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, idPaquete);
            pstmt.setInt(2, idUsuario);
            pstmt.setString(3, fechaViaje); 
            pstmt.setInt(4, cantidadPasajeros);
            pstmt.setDouble(5, costoTotal);
            
            int afectadas = pstmt.executeUpdate();
            if (afectadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                        
                        String codigo = "RES-" + String.format("%05d", idGenerado);
                        actualizarCodigoReserva(conn, idGenerado, codigo);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear reservacion: " + e.getMessage());
        }
        return idGenerado;
    }

    private void actualizarCodigoReserva(Connection conn, int idReservacion, String codigo) throws SQLException {
        String sql = "UPDATE reservacion SET codigo_reserva = ? WHERE id_reservacion = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            pstmt.setInt(2, idReservacion);
            pstmt.executeUpdate();
        }
    }

    public void insertarPasajeros(int idReservacion, String[] dpis) {
        String sql = "INSERT INTO pasajero_reservacion (id_reservacion, dpi_cliente) VALUES (?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (String dpi : dpis) {
                pstmt.setInt(1, idReservacion);
                pstmt.setString(2, dpi);
                pstmt.addBatch(); 
            }
            pstmt.executeBatch(); 
        } catch (SQLException e) {
            System.err.println("Error al insertar pasajeros: " + e.getMessage());
        }
    }
}
