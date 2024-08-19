package com.buysellplatform.controller;

import com.buysellplatform.dao.UserDAO;
import com.buysellplatform.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/auth")
public class AuthController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("register".equals(action)) {
            registerUser(request, response);
        } else if ("login".equals(action)) {
            loginUser(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String college = request.getParameter("college");
        String whatsappNumber = request.getParameter("whatsappNumber");
        String password = request.getParameter("password");

        // Validate email format
        String emailRegex = "^[a-zA-Z]+\\.[a-zA-Z]+\\.MBA23@said\\.oxford\\.edu$|^[a-zA-Z]+\\.[a-zA-Z]+\\.mba23@said\\.oxford\\.edu$";
        if (!email.matches(emailRegex)) {
            request.setAttribute("errorMessage", "Enter your SBS email");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setCollege(college);
        user.setWhatsappNumber(whatsappNumber);
        user.setPassword(password);

        boolean isRegistered = userDAO.registerUser(user);

        if (isRegistered) {
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("errorMessage", "Registration failed, please try again.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userDAO.loginUser(email, password);

        if (user != null) {
            request.getSession().setAttribute("user", user);
            response.sendRedirect("productList");
        } else {
            request.setAttribute("errorMessage", "Invalid email or password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
