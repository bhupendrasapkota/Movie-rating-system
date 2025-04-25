package com.movieratingsystem.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ContactController", urlPatterns = {"/contact"})
public class ContactController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        // Validate input
        if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                subject == null || subject.trim().isEmpty() ||
                message == null || message.trim().isEmpty()) {

            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(request, response);
            return;
        }

        // In a real application, you would send an email or save the message to the database
        // For this example, we'll just show a success message

        request.setAttribute("success", "Your message has been sent. We'll get back to you soon!");
        request.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(request, response);
    }
}