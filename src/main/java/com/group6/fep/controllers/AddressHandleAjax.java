/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group6.fep.controllers;

import com.google.gson.Gson;
import com.group6.fep.dao.AddressDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kanek
 */
@WebServlet(name = "AddressHandleAjax", urlPatterns = {"/AddressHandleAjax"})
public class AddressHandleAjax extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String param = request.getParameter("param");
        AddressDAO addressDAO = new AddressDAO();
        Gson gson = new Gson();
        List<Map<String, String>> result = null;
        switch (param) {
            case "city": {
                try {
                    result = addressDAO.getCityIdAndName();

                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(AddressHandleAjax.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case "district": {
                String cityId = request.getParameter("cityId");
                try {
                    result = addressDAO.getDistrictIdAndName(Integer.parseInt(cityId));
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(AddressHandleAjax.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case "ward": {
                String districtId = request.getParameter("districtId");
                try {
                    result = addressDAO.getWardIdAndName(Integer.parseInt(districtId));
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(AddressHandleAjax.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        PrintWriter out = response.getWriter();
        out.println(gson.toJson(result));

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
