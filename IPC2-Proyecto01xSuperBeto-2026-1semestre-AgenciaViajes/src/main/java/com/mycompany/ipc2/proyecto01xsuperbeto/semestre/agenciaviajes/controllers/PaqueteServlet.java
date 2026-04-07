/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.PaqueteDAO;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Paquete;
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
@WebServlet(name = "PaqueteServlet", urlPatterns = {"/api/paquetes"})
public class PaqueteServlet extends HttpServlet {
    
    private final PaqueteDAO paqueteDAO = new PaqueteDAO();
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
            out.println("<title>Servlet PaqueteServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PaqueteServlet at " + request.getContextPath() + "</h1>");
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

        try {

            java.util.List<JsonObject> paquetes = paqueteDAO.listarPaquetesDisponibles();
            resp.getWriter().write(gson.toJson(paquetes));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
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
            
          
            String nombre = json.get("nombre").getAsString();
            int idDestino = json.get("id_destino").getAsInt();
            int duracion = json.get("duracion_dias").getAsInt();
            double precio = json.get("precio_venta").getAsDouble();
            int capacidad = json.get("capacidad_max").getAsInt();
            
            Paquete nuevoPaquete = new Paquete(nombre, idDestino, duracion, precio, capacidad);
            
          
            int idPaqueteGenerado = paqueteDAO.crearPaqueteRetornandoId(nuevoPaquete);
            
            JsonObject respuesta = new JsonObject();

            if (idPaqueteGenerado != -1) {
               
                JsonArray servicios = json.getAsJsonArray("servicios");
                int serviciosGuardados = 0;
                
                for (JsonElement elemento : servicios) {
                    JsonObject servicio = elemento.getAsJsonObject();
                    int idProveedor = servicio.get("id_proveedor").getAsInt();
                    String descripcion = servicio.get("descripcion").getAsString();
                    double costo = servicio.get("costo_agencia").getAsDouble();
                    
                    if(paqueteDAO.insertarServicioPaquete(idPaqueteGenerado, idProveedor, descripcion, costo)){
                        serviciosGuardados++;
                    }
                }
                
                respuesta.addProperty("success", true);
                respuesta.addProperty("mensaje", "Paquete creado con " + serviciosGuardados + " servicios.");
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                respuesta.addProperty("success", false);
                respuesta.addProperty("error", "Error al guardar el paquete (¿Nombre duplicado?).");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            
            resp.getWriter().write(gson.toJson(respuesta));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    private void configurarCORS(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
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
