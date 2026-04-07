/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.controllers;

import com.google.gson.*;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.ClienteDAO;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Cliente;
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
@WebServlet(name = "ClienteServlet", urlPatterns = {"/api/clientes"})
public class ClienteServlet extends HttpServlet {
    
    private final ClienteDAO clienteDAO = new ClienteDAO();
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
            out.println("<title>Servlet ClienteServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ClienteServlet at " + request.getContextPath() + "</h1>");
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

        if (dpi != null && !dpi.isEmpty()) {
            JsonObject cliente = clienteDAO.buscarPorDPI(dpi);
            if (cliente != null) {
                // Cliente encontrado
                resp.getWriter().write(gson.toJson(cliente));
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                // Cliente NO encontrado
                JsonObject error = new JsonObject();
                error.addProperty("error", "Cliente no encontrado");
                resp.getWriter().write(gson.toJson(error));
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // Error 404
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
            
            String dpi = json.get("dpi").getAsString();
            String nombre = json.get("nombre_completo").getAsString();
            String fechaNac = json.get("fecha_nacimiento").getAsString(); // Angular formato YYYY-MM-DD
            String telefono = json.get("telefono").getAsString();
            String email = json.get("email").getAsString();
            String nacionalidad = json.get("nacionalidad").getAsString();

            Cliente nuevoCliente = new Cliente(dpi, nombre, fechaNac, telefono, email, nacionalidad);
            
            if (clienteDAO.insertarClienteWeb(nuevoCliente)) {
                JsonObject respuesta = new JsonObject();
                respuesta.addProperty("success", true);
                resp.getWriter().write(gson.toJson(respuesta));
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (Exception e) {
            System.err.println("Error en POST Cliente: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
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
    
    private void configurarCORS(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
    }
}
