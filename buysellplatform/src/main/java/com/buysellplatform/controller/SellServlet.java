package com.buysellplatform.controller;

import com.buysellplatform.dao.ProductDAO;
import com.buysellplatform.model.Product;
import com.buysellplatform.model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/sell")
@MultipartConfig
public class SellServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;
    private static final String UPLOAD_DIRECTORY = "uploads";

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            String title = null, description = null, minBidPriceStr = null, auctionEndDate = null, auctionEndTime = null, fileName = null;

            for (FileItem item : multiparts) {
                if (!item.isFormField()) {
                    fileName = new File(item.getName()).getName();
                    File file = new File(uploadPath + File.separator + fileName);
                    item.write(file);
                    System.out.println("Uploaded File Name: " + fileName);
                } else {
                    switch (item.getFieldName()) {
                        case "title":
                            title = item.getString();
                            System.out.println("Title: " + title);
                            break;
                        case "description":
                            description = item.getString();
                            System.out.println("Description: " + description);
                            break;
                        case "minBidPrice":
                            minBidPriceStr = item.getString();
                            System.out.println("Min Bid Price: " + minBidPriceStr);
                            break;
                        case "auctionEndDate":
                            auctionEndDate = item.getString();
                            System.out.println("Auction End Date: " + auctionEndDate);
                            break;
                        case "auctionEndTime":
                            auctionEndTime = item.getString();
                            System.out.println("Auction End Time: " + auctionEndTime);
                            break;
                    }
                }
            }

            if (title != null && description != null && minBidPriceStr != null && auctionEndDate != null && auctionEndTime != null) {
                double minBidPrice = Double.parseDouble(minBidPriceStr);
                User seller = (User) request.getSession().getAttribute("user");

                if (seller == null) {
                    // Redirect to login if user is not logged in
                    response.sendRedirect("login.jsp");
                    return;
                }

                // Parse the auction end date and time
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                LocalDateTime auctionEndDateTime = null;

                try {
                    LocalDateTime date = LocalDateTime.parse(auctionEndDate, dateFormatter);
                    LocalDateTime time = LocalDateTime.parse(auctionEndTime, timeFormatter);
                    auctionEndDateTime = LocalDateTime.of(date.toLocalDate(), time.toLocalTime());
                } catch (Exception e) {
                    // Handle the exception and provide a default value
                    System.out.println("Date-time parsing failed: " + e.getMessage());
                    auctionEndDateTime = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);
                }

                String formattedEndDate = auctionEndDateTime.format(outputFormatter);
                Timestamp auctionEndTimestamp = Timestamp.valueOf(auctionEndDateTime);

                Product product = new Product();
                product.setTitle(title);
                product.setDescription(description);
                product.setMinBidPrice(minBidPrice);
                product.setAuctionEndDate(auctionEndTimestamp);
                product.setImage(fileName);
                product.setSellerId(seller.getId());

                System.out.println("Product Details:");
                System.out.println("Title: " + product.getTitle());
                System.out.println("Description: " + product.getDescription());
                System.out.println("Min Bid Price: " + product.getMinBidPrice());
                System.out.println("Auction End Date: " + product.getAuctionEndDate());
                System.out.println("Image: " + product.getImage());
                System.out.println("Seller ID: " + product.getSellerId());

                boolean isProductListed = productDAO.listProduct(product);

                if (isProductListed) {
                    System.out.println("Product listed successfully.");
                    response.sendRedirect("productList");
                } else {
                    System.out.println("Product listing failed.");
                    request.setAttribute("errorMessage", "Product listing failed. Please try again.");
                    request.getRequestDispatcher("sell.jsp").forward(request, response);
                }
            } else {
                System.out.println("All fields are required.");
                request.setAttribute("errorMessage", "All fields are required.");
                request.getRequestDispatcher("sell.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            System.out.println("File upload failed due to: " + ex.getMessage());
            request.setAttribute("errorMessage", "File upload failed due to: " + ex.getMessage());
            request.getRequestDispatcher("sell.jsp").forward(request, response);
        }
    }
}
