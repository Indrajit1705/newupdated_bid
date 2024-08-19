package com.buysellplatform.controller;

import com.buysellplatform.dao.BidDAO;
import com.buysellplatform.dao.ProductDAO;
import com.buysellplatform.model.Bid;
import com.buysellplatform.model.Product;
import com.buysellplatform.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BidServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private BidDAO bidDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        bidDAO = new BidDAO();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        double bidPrice = Double.parseDouble(request.getParameter("bidPrice"));
        User buyer = (User) request.getSession().getAttribute("user");

        Product product = productDAO.getProductDetails(productId);

        if (product != null && bidPrice >= product.getCurrentBidPrice() + 5) {
            Bid bid = new Bid();
            bid.setProductId(productId);
            bid.setBuyerId(buyer.getId());
            bid.setBidPrice(bidPrice);

            boolean isBidPlaced = bidDAO.placeBid(bid);

            if (isBidPlaced) {
                product.setCurrentBidPrice(bidPrice);
                productDAO.updateCurrentBidPrice(product);
                response.sendRedirect("product-details.jsp?id=" + productId);
            } else {
                request.setAttribute("errorMessage", "Bid placement failed. Please try again.");
                request.getRequestDispatcher("product-details.jsp?id=" + productId).forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Bid price must be at least $5 more than the current bid.");
            request.getRequestDispatcher("product-details.jsp?id=" + productId).forward(request, response);
        }
    }
}
