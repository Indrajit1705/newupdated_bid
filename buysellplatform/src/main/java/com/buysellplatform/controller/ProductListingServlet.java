package com.buysellplatform.controller;

import com.buysellplatform.dao.ProductDAO;
import com.buysellplatform.model.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProductListingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = productDAO.getAllProducts();
        System.out.println("Number of products retrieved: " + products.size());
        
        
        if (products == null) {
            System.out.println("Products list is null");
        }

        request.setAttribute("products", products);
        request.getRequestDispatcher("productList.jsp").forward(request, response);
    }

}
