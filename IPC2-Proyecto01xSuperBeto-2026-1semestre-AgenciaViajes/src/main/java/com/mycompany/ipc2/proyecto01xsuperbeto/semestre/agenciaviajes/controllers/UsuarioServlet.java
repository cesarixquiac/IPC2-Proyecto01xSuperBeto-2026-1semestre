/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.controllers;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.UsuarioDAO;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 *
 * @author cesar
 */
@WebServlet(name = "UsuarioServlet", urlPatterns = {"/api/usuarios"})
public class UsuarioServlet extends HttpServlet {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Gson gson = new Gson();

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
            out.println("<title>Servlet UsuarioServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UsuarioServlet at " + request.getContextPath() + "</h1>");
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
            // Obtenemos la lista de usuarios desde el DAO
            List<JsonObject> usuarios = usuarioDAO.listarUsuarios();
            
            // La convertimos a texto JSON y la enviamos a Angular
            String jsonRespuesta = gson.toJson(usuarios);
            resp.getWriter().write(jsonRespuesta);
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
            // Leemos el JSON que manda el modal de Angular
            JsonObject json = gson.fromJson(req.getReader(), JsonObject.class);
            
            System.out.println("--- RECIBIENDO PETICION POST (NUEVO USUARIO) ---");
            
            String username = json.get("username").getAsString();
            String password = json.get("password").getAsString();
            int idRol = json.get("id_rol").getAsInt();

           
            if (password.length() < 6) {
                System.err.println("Rechazado: Contraseña muy corta.");
                JsonObject error = new JsonObject();
                error.addProperty("error", "La contraseña debe tener al menos 6 caracteres.");
                resp.getWriter().write(gson.toJson(error));
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return; 
            }

            System.out.println("Intentando guardar en MySQL a: " + username);
            
            // Llamamos al DAO
            boolean creado = usuarioDAO.crearUsuario(username, password, idRol);

            JsonObject respuesta = new JsonObject();
            if (creado) {
                System.out.println("¡Usuario " + username + " creado con éxito!");
                respuesta.addProperty("success", true);
                resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            } else {
                System.err.println("¡Falló la creación! El nombre de usuario ya existe.");
                respuesta.addProperty("success", false);
                respuesta.addProperty("error", "El nombre de usuario ya existe en el sistema.");
                resp.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict
            }
            
            resp.getWriter().write(gson.toJson(respuesta));

        } catch (Exception e) {
            System.err.println("Error grave en doPost: " + e.getMessage());
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
    
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configurarCORS(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
    
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configurarCORS(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            java.io.BufferedReader reader = req.getReader();
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            
            System.out.println("--- RECIBIENDO PETICION PUT ---");
            System.out.println("JSON Recibido: " + json.toString());

            String username = json.get("username").getAsString();
            int idRol = json.get("id_rol").getAsInt();
            String estado = json.has("estado") ? json.get("estado").getAsString() : "ACTIVO";

            System.out.println("Intentando actualizar a " + username + " con rol " + idRol);

            boolean actualizado = usuarioDAO.actualizarUsuario(username, idRol, estado);

            JsonObject respuesta = new JsonObject();
            if (actualizado) {
                System.out.println("¡Actualización exitosa en BD!");
                respuesta.addProperty("success", true);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                System.err.println("¡Falló la actualización en BD!");
                respuesta.addProperty("success", false);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            resp.getWriter().write(gson.toJson(respuesta));

        } catch (Exception e) {
            System.err.println("Error en doPut: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
    private void configurarCORS(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS, PUT");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
        
}
