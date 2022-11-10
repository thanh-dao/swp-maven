/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group6.fep.controllers;

import com.google.gson.Gson;
import com.group6.fep.dao.ProductDAO;
import com.group6.fep.dto.ProductDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.group6.fep.utils.Constants;

/**
 *
 * @author kanek
 */
@WebServlet(name = "GetProductAjax", urlPatterns = {"/GetProductAjax"})
public class GetProductAjax extends HttpServlet {

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
        response.setContentType("application/json;charset=UTF-8");
        String function = request.getParameter("func");
        List<ProductDTO> productResult = null;
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        System.out.println(function);
        try {
            switch (function) {
                case "getSortedProductList": {
                    String cateIdStr = request.getParameter("cateId");
                    System.out.println("cateId" + cateIdStr);
                    int pageNumber = Integer.parseInt(request.getParameter("pageNum")) - 1;
                    System.out.println("pageNumber " + pageNumber);
                    if (cateIdStr.isEmpty()) {
                        productResult = getProductListByProductName(pageNumber, request);
                    } else {
                        productResult = getSortedProductList(0, pageNumber, request);
                    }
                    System.out.println(gson.toJson(productResult));
                    out.print(gson.toJson(productResult));
                    break;
                }
                case "getSearchResult": {
                    String productName = request.getParameter("productName");
                    productResult = getSearchResult(productName, request, response);
                    out.print(gson.toJson(productResult));
                    break;
                }
                case "deleteProduct": {
                    String productId = request.getParameter("productId");
                    String emailAdmin = request.getParameter("emailAdmin");
                    boolean b = new ProductDAO().deleteProduct(productId, emailAdmin);
                    out.print(b);
                    break;
                }
                case "getProduct": {
                    int pId = Integer.parseInt(request.getParameter("pId"));
                    if (!request.getParameter("func").equalsIgnoreCase("u")) {
                        throw new Exception();
                    }
                    out.print(gson.toJson(new ProductDAO().getProductById(pId)));
                    break;
                }
                case "init productName": {
                    String name = request.getParameter("name");
                    request.getSession().setAttribute("productName", name);
                    out.print(gson.toJson(name));
                    break;
                }
                default: {
                    throw new IllegalArgumentException("invalid function name in GetProductAjax");
                }
            }   
        } catch (NullPointerException ex) {
            System.out.println("123");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<ProductDTO> getProductListByProductName(int pageNumber, HttpServletRequest request)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        String name = request.getParameter("name");
        ProductDAO proDAO = new ProductDAO();
        int sortBy = ProductDAO.SOLD_COUNT;
        boolean dataTrend = ProductDAO.DESC;
        String sortOption = request.getParameter("option");
        if (sortOption != null && !sortOption.isEmpty()) {
            switch (Integer.parseInt(sortOption)) {
                case 1: {
                    sortBy = ProductDAO.PRICE;
                    dataTrend = ProductDAO.ASC;
                    break;
                }
                case 2: {
                    sortBy = ProductDAO.PRICE;
                    dataTrend = ProductDAO.DESC;
                    break;
                }
                case 3: {
                    sortBy = ProductDAO.NAME;
                    dataTrend = ProductDAO.ASC;
                    break;
                }
                case 4: {
                    sortBy = ProductDAO.NAME;
                    dataTrend = ProductDAO.DESC;
                    break;
                }
                case 5: {
                    sortBy = ProductDAO.SOLD_COUNT;
                    dataTrend = ProductDAO.DESC;
                    break;
                }

            }
        } else {
            sortBy = ProductDAO.NAME;
            dataTrend = ProductDAO.ASC;
        }
        return proDAO.getProductListByProductName(pageNumber, name, sortBy, dataTrend);
    }

    public List<ProductDTO> getSortedProductList(int cateId, int pageNumber, HttpServletRequest request)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        String sortOption = request.getParameter("option");
        ProductDAO proDAO = new ProductDAO();
        int sortBy = 0;
        System.out.println("function pageNumber" + pageNumber);
        boolean dataTrend = true;
        if (sortOption != null && !sortOption.isEmpty()) {
            switch (Integer.parseInt(sortOption)) {
                case 1: {
                    sortBy = ProductDAO.PRICE;
                    dataTrend = ProductDAO.ASC;
                    break;
                }
                case 2: {
                    sortBy = ProductDAO.PRICE;
                    dataTrend = ProductDAO.DESC;
                    break;
                }
                case 3: {
                    sortBy = ProductDAO.NAME;
                    dataTrend = ProductDAO.ASC;
                    break;
                }
                case 4: {
                    sortBy = ProductDAO.NAME;
                    dataTrend = ProductDAO.DESC;
                    break;
                }
                case 5: {
                    sortBy = ProductDAO.SOLD_COUNT;
                    dataTrend = ProductDAO.DESC;
                    break;
                }

            }
        } else {
            sortBy = ProductDAO.NAME;
            dataTrend = ProductDAO.ASC;
        }
        System.out.println(proDAO.getProductList(pageNumber, sortBy, dataTrend, cateId).size());
        return proDAO.getProductList(pageNumber, sortBy, dataTrend, cateId);
    }

    public List<ProductDTO> getSearchResult(String productName, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        ProductDAO proDAO = new ProductDAO();
        return proDAO.getProductList(Constants.RESULT_PER_SEARCH, productName);
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
