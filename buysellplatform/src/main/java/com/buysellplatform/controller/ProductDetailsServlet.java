package com.buysellplatform.controller;

import com.buysellplatform.dao.ProductDAO;
import com.buysellplatform.model.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        // Initialize the ProductDAO object
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            // Handle the case where no ID is provided
            response.sendRedirect("error.jsp?message=Product ID is missing");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);
            Product product = productDAO.getProductDetails(productId);

            if (product == null) {
                // Handle the case where the product is not found
                response.sendRedirect("error.jsp?message=Product not found");
                return;
            }

            // Set the product as a request attribute and forward to the JSP
            request.setAttribute("product", product);
            request.getRequestDispatcher("productDetails.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Handle the case where the ID parameter is not a valid integer
            response.sendRedirect("error.jsp?message=Invalid Product ID format");
        } catch (Exception e) {
            // Handle any other exceptions that may occur
            e.printStackTrace(); // Log the exception
            response.sendRedirect("error.jsp?message=An error occurred while retrieving product details");
        }
    }
}
