package com.movieratingsystem.controllers;

import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.UserService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.InputStream;

@WebServlet(name = "RegisterController", urlPatterns = {"/register"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 5,   // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class RegisterController extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Forward to register page
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate input
        if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                confirmPassword == null || confirmPassword.trim().isEmpty()) {

            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        // Create user object
        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        // Handle profile image upload
        Part filePart = request.getPart("profileImage");
        if (filePart != null && filePart.getSize() > 0) {
            InputStream fileContent = filePart.getInputStream();
            byte[] imageBytes = new byte[(int) filePart.getSize()];
            fileContent.read(imageBytes);
            user.setImage(imageBytes);
        }

        // Register user
        boolean success = userService.registerUser(user);

        if (success) {
            // Authenticate and create session
            UserModel authenticatedUser = userService.authenticateUser(email, password);
            if (authenticatedUser != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", authenticatedUser);
            }

            // Redirect to home page
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            request.setAttribute("error", "Registration failed. Email may already be in use.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}