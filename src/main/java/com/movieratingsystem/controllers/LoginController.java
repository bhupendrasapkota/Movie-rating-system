package com.movieratingsystem.controllers;

import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.UserService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Base64;

@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    private UserService userService;
    private static final String REMEMBER_ME_COOKIE = "rememberMe";
    private static final int COOKIE_MAX_AGE = 30 * 24 * 60 * 60; // 30 days in seconds

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

        // Forward to login page
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");

        // Validate input
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and password are required");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        // Authenticate user
        UserModel user = userService.authenticateUser(email, password);

        if (user != null) {
            // Create session and store user
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Handle remember me
            if (remember != null && remember.equals("on")) {
                // Create remember me cookie
                String cookieValue = Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
                Cookie rememberMeCookie = new Cookie(REMEMBER_ME_COOKIE, cookieValue);
                rememberMeCookie.setMaxAge(COOKIE_MAX_AGE);
                rememberMeCookie.setPath(request.getContextPath());
                rememberMeCookie.setHttpOnly(true);
                rememberMeCookie.setSecure(request.isSecure()); // Set secure flag if using HTTPS
                response.addCookie(rememberMeCookie);
            }

            // Redirect to home page
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
