/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.db.ConexionDB;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Paquete;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cesar
 */
public class PaqueteDAO {

    public boolean insertarPaquete(Paquete paquete) {
        String sql = "INSERT INTO Paquete (nombre, id_destino, duracion_dias, precio_venta, capacidad_max, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPaquete);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("precio_venta");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener precio del paquete: " + e.getMessage());
        }
        return 0.0;
    }

    public int crearPaqueteRetornandoId(Paquete paquete) {
        String sql = "INSERT INTO Paquete (nombre, id_destino, duracion_dias, precio_venta, capacidad_max, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

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

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); java.sql.ResultSet rs = pstmt.executeQuery()) {

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

    public List<JsonObject> obtenerPaquetes() {
        List<JsonObject> lista = new ArrayList<>();
        String sql = "SELECT p.*, d.nombre AS nombre_destino FROM paquete p JOIN destino d ON p.id_destino = d.id_destino";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                JsonObject j = new JsonObject();
                j.addProperty("id_paquete", rs.getInt("id_paquete"));
                j.addProperty("nombre", rs.getString("nombre"));
                j.addProperty("id_destino", rs.getInt("id_destino"));
                j.addProperty("nombre_destino", rs.getString("nombre_destino"));
                j.addProperty("duracion_dias", rs.getInt("duracion_dias"));
                j.addProperty("descripcion", rs.getString("descripcion"));
                j.addProperty("precio_venta", rs.getDouble("precio_venta"));
                j.addProperty("capacidad_max", rs.getInt("capacidad_max"));
                j.addProperty("estado", rs.getInt("estado"));
                lista.add(j);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean crearPaqueteConServicios(String nombre, int idDestino, int duracionDias, String descripcion, double precioVenta, int capacidadMax, JsonArray servicios) {
        String sqlPaquete = "INSERT INTO paquete (nombre, id_destino, duracion_dias, descripcion, precio_venta, capacidad_max, estado) VALUES (?, ?, ?, ?, ?, ?, 1)";
        String sqlServicio = "INSERT INTO servicio_paquete (id_paquete, id_proveedor, descripcion, costo_agencia) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);

            int idPaqueteGenerado = -1;
            try (PreparedStatement pstmtPaquete = conn.prepareStatement(sqlPaquete, Statement.RETURN_GENERATED_KEYS)) {
                pstmtPaquete.setString(1, nombre);
                pstmtPaquete.setInt(2, idDestino);
                pstmtPaquete.setInt(3, duracionDias);
                pstmtPaquete.setString(4, descripcion);
                pstmtPaquete.setDouble(5, precioVenta);
                pstmtPaquete.setInt(6, capacidadMax);
                pstmtPaquete.executeUpdate();

                try (ResultSet rs = pstmtPaquete.getGeneratedKeys()) {
                    if (rs.next()) {
                        idPaqueteGenerado = rs.getInt(1);
                    }
                }
            }

            if (idPaqueteGenerado == -1) {
                conn.rollback();
                return false;
            }

            try (PreparedStatement pstmtServicio = conn.prepareStatement(sqlServicio)) {
                for (int i = 0; i < servicios.size(); i++) {
                    JsonObject servicio = servicios.get(i).getAsJsonObject();

                    pstmtServicio.setInt(1, idPaqueteGenerado);
                    pstmtServicio.setInt(2, servicio.get("id_proveedor").getAsInt());
                    pstmtServicio.setString(3, servicio.get("descripcion").getAsString());
                    pstmtServicio.setDouble(4, servicio.get("costo_agencia").getAsDouble());

                    pstmtServicio.addBatch(); 
                }
                pstmtServicio.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("ERROR SQL AL CREAR PAQUETE: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public List<JsonObject> obtenerServiciosDePaquete(int idPaquete) {
    List<JsonObject> lista = new ArrayList<>();
    String sql = "SELECT sp.*, prov.nombre AS nombre_proveedor " +
                 "FROM servicio_paquete sp " +
                 "JOIN proveedor prov ON sp.id_proveedor = prov.id_proveedor " +
                 "WHERE sp.id_paquete = ?";
    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, idPaquete);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                JsonObject j = new JsonObject();
                j.addProperty("id_proveedor", rs.getInt("id_proveedor"));
                j.addProperty("nombre_proveedor", rs.getString("nombre_proveedor"));
                j.addProperty("descripcion", rs.getString("descripcion"));
                j.addProperty("costo_agencia", rs.getDouble("costo_agencia"));
                lista.add(j);
            }
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return lista;
}
    
    
    public boolean actualizarPaqueteConServicios(int idPaquete, String nombre, int idDestino, int duracion, String desc, double precio, int capacidad, int estado, JsonArray servicios) {
    String sqlUpdatePaquete = "UPDATE paquete SET nombre=?, id_destino=?, duracion_dias=?, descripcion=?, precio_venta=?, capacidad_max=?, estado=? WHERE id_paquete=?";
    String sqlDeleteServicios = "DELETE FROM servicio_paquete WHERE id_paquete=?";
    String sqlInsertServicio = "INSERT INTO servicio_paquete (id_paquete, id_proveedor, descripcion, costo_agencia) VALUES (?, ?, ?, ?)";

    Connection conn = null;
    try {
        conn = ConexionDB.getConnection();
        conn.setAutoCommit(false);

    
        try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdatePaquete)) {
            pstmt.setString(1, nombre);
            pstmt.setInt(2, idDestino);
            pstmt.setInt(3, duracion);
            pstmt.setString(4, desc);
            pstmt.setDouble(5, precio);
            pstmt.setInt(6, capacidad);
            pstmt.setInt(7, estado);
            pstmt.setInt(8, idPaquete);
            pstmt.executeUpdate();
        }

        
        try (PreparedStatement pstmtDelete = conn.prepareStatement(sqlDeleteServicios)) {
            pstmtDelete.setInt(1, idPaquete);
            pstmtDelete.executeUpdate();
        }

        
        try (PreparedStatement pstmtIns = conn.prepareStatement(sqlInsertServicio)) {
            for (int i = 0; i < servicios.size(); i++) {
                JsonObject s = servicios.get(i).getAsJsonObject();
                pstmtIns.setInt(1, idPaquete);
                pstmtIns.setInt(2, s.get("id_proveedor").getAsInt());
                pstmtIns.setString(3, s.get("descripcion").getAsString());
                pstmtIns.setDouble(4, s.get("costo_agencia").getAsDouble());
                pstmtIns.addBatch();
            }
            pstmtIns.executeBatch();
        }

        conn.commit(); 
        return true;
    } catch (SQLException e) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }
}
    
    
}
