/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.db.ConexionDB;
import java.sql.*;

/**
 *
 * @author cesar
 */
public class ReportesDAO {

    // Reporte de Ventas (Reservaciones confirmadas)
    public JsonArray reporteDeVentas(String fechaInicio, String fechaFin) {
        JsonArray lista = new JsonArray();
        
        String sql = "SELECT p.nombre AS paquete, r.cantidad_pasajeros, u.username AS agente, r.costo_total "
                + "FROM reservacion r "
                + "JOIN paquete p ON r.id_paquete = p.id_paquete "
                + "JOIN usuario u ON r.id_usuario_agente = u.id_usuario "
                + "WHERE r.estado = 'Confirmada' AND DATE(r.fecha_creacion) BETWEEN ? AND ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fechaInicio);
            pstmt.setString(2, fechaFin);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    JsonObject j = new JsonObject();
                    j.addProperty("paquete", rs.getString("paquete"));
                    j.addProperty("pasajeros", rs.getInt("cantidad_pasajeros"));
                    j.addProperty("agente", rs.getString("agente"));
                    j.addProperty("monto_total", rs.getDouble("costo_total"));
                    lista.add(j);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

   
    public JsonArray reporteDeCancelaciones(String fechaInicio, String fechaFin) {
    JsonArray lista = new JsonArray();
   
    String sql = "SELECT r.id_reservacion, rem.fecha_reembolso, rem.monto_devuelto, " +
                 "(r.costo_total - rem.monto_devuelto) AS dinero_retenido " +
                 "FROM reservacion r " +
                 "JOIN reembolso rem ON r.id_reservacion = rem.id_reservacion " +
                 "WHERE r.estado = 'Cancelada' AND rem.fecha_reembolso BETWEEN ? AND ?";

    try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, fechaInicio);
        pstmt.setString(2, fechaFin);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                JsonObject j = new JsonObject();
                j.addProperty("id_reservacion", rs.getInt("id_reservacion"));
                j.addProperty("fecha_cancelacion", rs.getString("fecha_reembolso")); // Usamos la fecha del reembolso
                j.addProperty("reembolsado", rs.getDouble("monto_devuelto"));
                j.addProperty("perdida_agencia", rs.getDouble("dinero_retenido")); 
                lista.add(j);
            }
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return lista;
}

    //Reporte de Ocupación por Destino
    public JsonArray reporteOcupacionDestino(String fechaInicio, String fechaFin) {
        JsonArray lista = new JsonArray();
        String sql = "SELECT d.nombre AS destino, SUM(r.cantidad_pasajeros) AS total_pasajeros " +
             "FROM reservacion r " +
             "JOIN paquete p ON r.id_paquete = p.id_paquete " +
             "JOIN destino d ON p.id_destino = d.id_destino " +
             "WHERE r.estado = 'Confirmada' AND DATE(r.fecha_creacion) BETWEEN ? AND ? " +
             "GROUP BY d.id_destino";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fechaInicio);
            pstmt.setString(2, fechaFin);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    JsonObject j = new JsonObject();
                    j.addProperty("destino", rs.getString("destino"));
                    j.addProperty("total_pasajeros", rs.getInt("total_pasajeros"));
                    lista.add(j);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    // Reporte de Ganancias (Brutas, Reembolsos y Neta)
   public JsonArray reporteGanancias(String fechaInicio, String fechaFin) {
        JsonArray lista = new JsonArray();
        
        String sql = "SELECT " +
                     "(SELECT IFNULL(SUM(costo_total), 0) FROM reservacion WHERE DATE(fecha_creacion) BETWEEN ? AND ?) AS ingresos_brutos, " +
                     "(SELECT IFNULL(SUM(monto_devuelto), 0) FROM reembolso WHERE DATE(fecha_reembolso) BETWEEN ? AND ?) AS total_reembolsos";
        
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fechaInicio);
            pstmt.setString(2, fechaFin);
           
            pstmt.setString(3, fechaInicio);
            pstmt.setString(4, fechaFin);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double ingresos = rs.getDouble("ingresos_brutos");
                    double reembolsos = rs.getDouble("total_reembolsos");
                    double neta = ingresos - reembolsos;

                    JsonObject j = new JsonObject();
                    j.addProperty("ingresos_brutos", ingresos);
                    j.addProperty("total_reembolsos", reembolsos);
                    j.addProperty("ganancia_neta", neta);
                    lista.add(j);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // Agente con más ventas y el listado de sus reservaciones
   public JsonArray reporteTopAgenteVentas(String fechaInicio, String fechaFin) {
        JsonArray lista = new JsonArray();
        
        String sql = "SELECT u.username AS agente, r.id_reservacion, p.nombre AS paquete, r.costo_total " +
                     "FROM reservacion r " +
                     "JOIN usuario u ON r.id_usuario_agente = u.id_usuario " +
                     "JOIN paquete p ON r.id_paquete = p.id_paquete " +
                     "WHERE u.id_usuario = (SELECT id_usuario_agente FROM reservacion WHERE estado = 'Confirmada' AND DATE(fecha_creacion) BETWEEN ? AND ? GROUP BY id_usuario_agente ORDER BY SUM(costo_total) DESC LIMIT 1) " +
                     "AND r.estado = 'Confirmada' AND DATE(r.fecha_creacion) BETWEEN ? AND ?";
        
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fechaInicio); pstmt.setString(2, fechaFin);
            pstmt.setString(3, fechaInicio); pstmt.setString(4, fechaFin);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    JsonObject j = new JsonObject();
                    j.addProperty("agente", rs.getString("agente"));
                    j.addProperty("id_reservacion", rs.getInt("id_reservacion"));
                    j.addProperty("paquete", rs.getString("paquete"));
                    j.addProperty("monto", rs.getDouble("costo_total"));
                    lista.add(j);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // Agente con más ganancias (Solo el total)
    public JsonArray reporteTopAgenteGanancias(String fechaInicio, String fechaFin) {
        JsonArray lista = new JsonArray();
        String sql = "SELECT u.username AS agente, SUM(r.costo_total) AS ganancias_generadas " +
                     "FROM reservacion r JOIN usuario u ON r.id_usuario_agente = u.id_usuario " +
                     "WHERE r.estado = 'Confirmada' AND DATE(r.fecha_creacion) BETWEEN ? AND ? " +
                     "GROUP BY r.id_usuario_agente ORDER BY ganancias_generadas DESC LIMIT 1";
        
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fechaInicio); pstmt.setString(2, fechaFin);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    JsonObject j = new JsonObject();
                    j.addProperty("agente", rs.getString("agente"));
                    j.addProperty("ganancias_generadas", rs.getDouble("ganancias_generadas"));
                    lista.add(j);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // Paquete MÁS o MENOS vendido con el detalle de sus reservaciones
    // Pasamos el parámetro 'orden' que será "DESC" para el más vendido y "ASC" para el menos vendido
    public JsonArray reportePaqueteExtremo(String fechaInicio, String fechaFin, String orden) {
        JsonArray lista = new JsonArray();
        String sql = "SELECT p.nombre AS paquete, r.id_reservacion, r.cantidad_pasajeros, r.costo_total " +
                     "FROM reservacion r JOIN paquete p ON r.id_paquete = p.id_paquete " +
                     "WHERE p.id_paquete = (SELECT id_paquete FROM reservacion WHERE estado = 'Confirmada' AND DATE(fecha_creacion) BETWEEN ? AND ? GROUP BY id_paquete ORDER BY COUNT(id_reservacion) " + orden + " LIMIT 1) " +
                     "AND r.estado = 'Confirmada' AND DATE(r.fecha_creacion) BETWEEN ? AND ?";
        
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fechaInicio); pstmt.setString(2, fechaFin);
            pstmt.setString(3, fechaInicio); pstmt.setString(4, fechaFin);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    JsonObject j = new JsonObject();
                    j.addProperty("paquete", rs.getString("paquete"));
                    j.addProperty("id_reservacion", rs.getInt("id_reservacion"));
                    j.addProperty("pasajeros", rs.getInt("cantidad_pasajeros"));
                    j.addProperty("monto", rs.getDouble("costo_total"));
                    lista.add(j);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
