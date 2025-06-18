package com.movieratingsystem.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LogoutController", urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {

    private static final String REMEMBER_ME_COOKIE = "rememberMe";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear remember me cookie
        Cookie rememberMeCookie = new Cookie(REMEMBER_ME_COOKIE, "");
        rememberMeCookie.setMaxAge(0); // Delete the cookie
        rememberMeCookie.setPath(request.getContextPath());
        response.addCookie(rememberMeCookie);

        // Redirect to home page
        response.sendRedirect(request.getContextPath()+"/");
    }
}
