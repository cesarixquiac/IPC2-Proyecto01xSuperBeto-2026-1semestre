/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.UsuarioDAO;
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
@WebServlet(name = "LoginServlet", urlPatterns = {"/api/login"})
public class LoginServlet extends HttpServlet {

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
            out.println("<title>Servlet LoginServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
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

    
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configurarCORS(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configurarCORS(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
           
            JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);
            String username = body.get("username").getAsString();
            String password = body.get("password").getAsString();

          
            int idRol = usuarioDAO.validarLogin(username, password);

            JsonObject jsonResponse = new JsonObject();
            
            if (idRol != -1) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("rol", idRol);
                resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Usuario o contraseña incorrectos");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 No Autorizado
            }
            
            
            resp.getWriter().write(gson.toJson(jsonResponse));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request si mandan mal el JSON
        }
    }

    
    private void configurarCORS(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200"); // Permite a Angular
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}
