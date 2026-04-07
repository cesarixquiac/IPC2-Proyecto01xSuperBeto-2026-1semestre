/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.controllers;

import com.google.gson.*;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.PagoDAO;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.ReembolsoDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cesar
 */
@WebServlet(name = "ReembolsoServlet", urlPatterns = {"/api/reembolsos"})
public class ReembolsoServlet extends HttpServlet {

    private final ReembolsoDAO reembolsoDAO = new ReembolsoDAO();
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
            out.println("<title>Servlet RembolsoServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RembolsoServlet at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setContentType("application/json");

        try {
            JsonObject json = gson.fromJson(req.getReader(), JsonObject.class);
            int idReservacion = json.get("id_reservacion").getAsInt();
            String fechaViajeStr = json.get("fecha_viaje").getAsString();
            double totalPagado = pagoDAO.obtenerTotalPagado(idReservacion);

         
            LocalDate hoy = LocalDate.now();
            LocalDate fechaViaje = LocalDate.parse(fechaViajeStr);
            long diasDiferencia = ChronoUnit.DAYS.between(hoy, fechaViaje);

            
            int porcentaje = 0;
            if (diasDiferencia >= 15) porcentaje = 100;
            else if (diasDiferencia >= 10) porcentaje = 70;
            else if (diasDiferencia >= 5) porcentaje = 40;
            else porcentaje = 0;

            double montoADevolver = totalPagado * (porcentaje / 100.0);

            boolean exito = reembolsoDAO.procesarCancelacion(idReservacion, montoADevolver, porcentaje, hoy.toString());

            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("success", exito);
            respuesta.addProperty("monto_devuelto", montoADevolver);
            respuesta.addProperty("porcentaje_aplicado", porcentaje);
            respuesta.addProperty("dias_anticipacion", diasDiferencia);

            resp.getWriter().write(gson.toJson(respuesta));

        } catch (Exception e) {
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
