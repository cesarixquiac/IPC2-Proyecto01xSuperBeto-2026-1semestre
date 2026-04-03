/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao;

import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.db.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author cesar
 */
public class PagoDAO {

    // Devuelve un arreglo: [0] = id_reservacion, [1] = costo_total
    public double[] obtenerDatosReservaPorCodigo(String codigoReserva) {
        String sql = "SELECT id_reservacion, costo_total FROM reservacion WHERE codigo_reserva = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigoReserva);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new double[]{rs.getInt("id_reservacion"), rs.getDouble("costo_total")};
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reserva: " + e.getMessage());
        }
        return null;
    }

    public boolean registrarPago(int idReservacion, double monto, int metodoPago, String fechaPago) {
        String sql = "INSERT INTO pago (id_reservacion, monto, metodo_pago, fecha_pago) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idReservacion);
            pstmt.setDouble(2, monto);
            pstmt.setInt(3, metodoPago);
            pstmt.setString(4, fechaPago);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar pago: " + e.getMessage());
            return false;
        }
    }

    public double obtenerTotalPagado(int idReservacion) {
        String sql = "SELECT SUM(monto) AS total FROM pago WHERE id_reservacion = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idReservacion);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular total pagado: " + e.getMessage());
        }
        return 0.0;
    }

    public void actualizarEstadoReserva(int idReservacion, String estado) {
        String sql = "UPDATE reservacion SET estado = ? WHERE id_reservacion = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estado);
            pstmt.setInt(2, idReservacion);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
        }
    }
}

