/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.PaqueteDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cesar
 */
@WebServlet(name = "PaqueteOperacionesServlet", urlPatterns = {"/api/paquetes-operaciones"})
public class PaqueteOperacionesServlet extends HttpServlet {

    private PaqueteDAO paqueteDAO = new PaqueteDAO();
    private Gson gson = new Gson();

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configurarCORS(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void configurarCORS(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, ,PUT, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
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
            out.println("<title>Servlet PaqueteOperacionesServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PaqueteOperacionesServlet at " + request.getContextPath() + "</h1>");
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
        String idParam = req.getParameter("id");

        if (idParam != null) {
            // Si hay ID, devolvemos los servicios de ese paquete
            int id = Integer.parseInt(idParam);
            resp.getWriter().write(gson.toJson(paqueteDAO.obtenerServiciosDePaquete(id)));
        } else {
            // Si no hay ID, devolvemos la lista de paquetes (como ya lo hacías)
            resp.getWriter().write(gson.toJson(paqueteDAO.obtenerPaquetes()));
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
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");

        try {
            // Recibimos todo el "paquete" de información desde Angular
            JsonObject json = gson.fromJson(req.getReader(), JsonObject.class);

            String nombre = json.get("nombre").getAsString();
            int idDestino = json.get("id_destino").getAsInt();
            int duracionDias = json.get("duracion_dias").getAsInt();
            String descripcion = json.has("descripcion") ? json.get("descripcion").getAsString() : "";
            double precioVenta = json.get("precio_venta").getAsDouble();
            int capacidadMax = json.get("capacidad_max").getAsInt();

            // Extraemos el arreglo de servicios
            JsonArray servicios = json.getAsJsonArray("servicios");

            // Mandamos todo al DAO
            boolean exito = paqueteDAO.crearPaqueteConServicios(nombre, idDestino, duracionDias, descripcion, precioVenta, capacidadMax, servicios);

            resp.setStatus(exito ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            System.err.println(" ERROR PARSEO JSON PAQUETE: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    @Override
protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
    resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
    
    try {
        JsonObject json = gson.fromJson(req.getReader(), JsonObject.class);
        int id = json.get("id_paquete").getAsInt();
        String nombre = json.get("nombre").getAsString();
        int idDestino = json.get("id_destino").getAsInt();
        int duracion = json.get("duracion_dias").getAsInt();
        String desc = json.has("descripcion") ? json.get("descripcion").getAsString() : "";
        double precio = json.get("precio_venta").getAsDouble();
        int capacidad = json.get("capacidad_max").getAsInt();
        int estado = json.get("estado").getAsInt();
        JsonArray servicios = json.getAsJsonArray("servicios");

        boolean exito = paqueteDAO.actualizarPaqueteConServicios(id, nombre, idDestino, duracion, desc, precio, capacidad, estado, servicios);
        resp.setStatus(exito ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
    

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

/**
 * Returns a short description of the servlet.
 *
 * @return a String containing servlet description
 */
