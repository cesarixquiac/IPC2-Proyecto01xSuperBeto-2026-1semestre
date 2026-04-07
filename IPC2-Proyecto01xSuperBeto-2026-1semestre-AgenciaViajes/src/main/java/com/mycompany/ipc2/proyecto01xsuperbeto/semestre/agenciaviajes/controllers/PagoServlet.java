/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.PagoDAO;
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
@WebServlet(name = "PagoServlet", urlPatterns = {"/api/pagos"})
public class PagoServlet extends HttpServlet {
    
    private final PagoDAO pagoDAO = new PagoDAO();
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
            out.println("<title>Servlet PagoServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PagoServlet at " + request.getContextPath() + "</h1>");
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

        String codigo = req.getParameter("codigo");

        if (codigo != null && !codigo.isEmpty()) {
            double[] datos = pagoDAO.obtenerDatosReservaPorCodigo(codigo);
            
            if (datos != null) {
                int idReservacion = (int) datos[0];
                double costoTotal = datos[1];
                
                // Calculamos cuánto ha pagado y cuánto debe
                double totalPagado = pagoDAO.obtenerTotalPagado(idReservacion);
                double saldoPendiente = costoTotal - totalPagado;

                JsonObject respuesta = new JsonObject();
                respuesta.addProperty("id_reservacion", idReservacion);
                respuesta.addProperty("costo_total", costoTotal);
                respuesta.addProperty("total_pagado", totalPagado);
                respuesta.addProperty("saldo_pendiente", saldoPendiente);

                resp.getWriter().write(gson.toJson(respuesta));
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                // Si no existe ese código (RES-0000X)
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND); 
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
            
            int idReservacion = json.get("id_reservacion").getAsInt();
            double monto = json.get("monto").getAsDouble();
            int metodoPago = json.get("metodo_pago").getAsInt();
            String fechaPago = json.get("fecha_pago").getAsString(); // YYYY-MM-DD
            double costoTotal = json.get("costo_total").getAsDouble(); // Lo enviamos desde Angular para comparar

            if (pagoDAO.registrarPago(idReservacion, monto, metodoPago, fechaPago)) {
                
               
                double nuevoTotalPagado = pagoDAO.obtenerTotalPagado(idReservacion);
                boolean seConfirmoReserva = false;

                if (nuevoTotalPagado >= costoTotal) {
                    pagoDAO.actualizarEstadoReserva(idReservacion, "Confirmada");
                    seConfirmoReserva = true;
                }

                JsonObject respuesta = new JsonObject();
                respuesta.addProperty("success", true);
                respuesta.addProperty("reserva_confirmada", seConfirmoReserva); 
                respuesta.addProperty("nuevo_saldo", costoTotal - nuevoTotalPagado);

                resp.getWriter().write(gson.toJson(respuesta));
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            System.err.println("Error en doPost Pago: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void configurarCORS(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
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
