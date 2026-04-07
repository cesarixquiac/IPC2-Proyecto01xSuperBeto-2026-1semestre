/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.ReservacionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cesar
 */
@WebServlet(name = "ReservacionServlet", urlPatterns = {"/api/reservaciones"})
public class ReservacionServlet extends HttpServlet {
    
    private final ReservacionDAO reservacionDAO = new ReservacionDAO();
    private final Gson gson = new Gson();
    
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configurarCORS(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ReservacionServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReservacionServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configurarCORS(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String dpi = req.getParameter("dpi");

        try {
            if (dpi != null && !dpi.isEmpty()) {
                // Buscamos el historial
                java.util.List<JsonObject> historial = reservacionDAO.obtenerHistorialPorDPI(dpi);
                resp.getWriter().write(gson.toJson(historial));
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            System.err.println("Error en doGet Reservacion: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configurarCORS(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            JsonObject json = gson.fromJson(req.getReader(), JsonObject.class);
            
            int idPaquete = json.get("id_paquete").getAsInt();
            int idUsuario = json.get("id_usuario").getAsInt(); // El ID del Agente
            String fechaViaje = json.get("fecha_viaje").getAsString();
            int cantidadPasajeros = json.get("cantidad_pasajeros").getAsInt();
            double costoTotal = json.get("costo_total").getAsDouble();

            JsonArray pasajerosArray = json.getAsJsonArray("pasajeros");
            String[] dpis = new String[pasajerosArray.size()];
            for (int i = 0; i < pasajerosArray.size(); i++) {
                dpis[i] = pasajerosArray.get(i).getAsString();
            }

            int idReservacion = reservacionDAO.crearReservacion(idPaquete, idUsuario, fechaViaje, cantidadPasajeros, costoTotal);

            JsonObject respuesta = new JsonObject();
            if (idReservacion != -1) {
                reservacionDAO.insertarPasajeros(idReservacion, dpis);
                
                respuesta.addProperty("success", true);
                respuesta.addProperty("id_reservacion", idReservacion);
                respuesta.addProperty("mensaje", "Reservación en estado PENDIENTE creada con éxito.");
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                respuesta.addProperty("success", false);
                respuesta.addProperty("error", "Error al crear la reservación en la base de datos.");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            
            resp.getWriter().write(gson.toJson(respuesta));

        } catch (Exception e) {
            System.err.println("Error en doPost Reservacion: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void configurarCORS(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
