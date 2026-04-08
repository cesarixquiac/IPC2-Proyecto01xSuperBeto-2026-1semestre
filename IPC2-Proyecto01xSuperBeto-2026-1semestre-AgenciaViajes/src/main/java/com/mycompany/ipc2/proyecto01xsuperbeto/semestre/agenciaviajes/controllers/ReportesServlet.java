/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.controllers;

import com.google.gson.Gson;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.ReportesDAO;
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
@WebServlet(name = "ReportesServlet", urlPatterns = {"/api/reportes"})
public class ReportesServlet extends HttpServlet {

    private ReportesDAO reportesDAO = new ReportesDAO();
    private Gson gson = new Gson();

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configurarCORS(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void configurarCORS(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
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
            out.println("<title>Servlet ReportesServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReportesServlet at " + request.getContextPath() + "</h1>");
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
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String tipo = req.getParameter("tipo");
        String fechaInicio = req.getParameter("inicio");
        String fechaFin = req.getParameter("fin");

        if (tipo == null || fechaInicio == null || fechaFin == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Faltan parámetros (tipo, inicio o fin)\"}");
            return;
        }

        try {
            String jsonResult = "[]";

            switch (tipo) {
                case "ventas":
                    jsonResult = gson.toJson(reportesDAO.reporteDeVentas(fechaInicio, fechaFin));
                    break;
                case "cancelaciones":
                    jsonResult = gson.toJson(reportesDAO.reporteDeCancelaciones(fechaInicio, fechaFin));
                    break;
                case "ocupacion":
                    jsonResult = gson.toJson(reportesDAO.reporteOcupacionDestino(fechaInicio, fechaFin));
                    break;
                case "ganancias":
                    jsonResult = gson.toJson(reportesDAO.reporteGanancias(fechaInicio, fechaFin));
                    break;
                case "top-agente-ventas":
                    jsonResult = gson.toJson(reportesDAO.reporteTopAgenteVentas(fechaInicio, fechaFin));
                    break;
                case "top-agente-ganancias":
                    jsonResult = gson.toJson(reportesDAO.reporteTopAgenteGanancias(fechaInicio, fechaFin));
                    break;
                case "paquete-mas-vendido":
                    jsonResult = gson.toJson(reportesDAO.reportePaqueteExtremo(fechaInicio, fechaFin, "DESC"));
                    break;
                case "paquete-menos-vendido":
                    jsonResult = gson.toJson(reportesDAO.reportePaqueteExtremo(fechaInicio, fechaFin, "ASC"));
                    break;
                default:
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
            }

            resp.getWriter().write(jsonResult);
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            e.printStackTrace();
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
