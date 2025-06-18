package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.Genre;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.GenreService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminAddGenreController", urlPatterns = {"/admin/genres/add"})
public class AdminAddGenreController extends HttpServlet {

    private GenreService genreService;

    @Override
    public void init() throws ServletException {
        super.init();
        genreService = new GenreService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserModel user = (UserModel) session.getAttribute("user");
        if (user.getRole() != UserModel.Role.admin) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Forward to add genre page
        request.getRequestDispatcher("/WEB-INF/views/admin/add-genre.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserModel user = (UserModel) session.getAttribute("user");
        if (user.getRole() != UserModel.Role.admin) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Get form data
        String genreName = request.getParameter("genreName");
        String description = request.getParameter("description");

        // Validate input
        if (genreName == null || genreName.trim().isEmpty()) {
            request.setAttribute("error", "Genre name is required");
            request.getRequestDispatcher("/WEB-INF/views/admin/add-genre.jsp").forward(request, response);
            return;
        }

        // Create genre object
        Genre genre = new Genre();
        genre.setGenreName(genreName.trim());
        genre.setDescription(description != null ? description.trim() : null);

        // Add genre
        boolean success = genreService.addGenre(genre);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/genres?success=Genre added successfully");
        } else {
            request.setAttribute("error", "Failed to add genre");
            request.getRequestDispatcher("/WEB-INF/views/admin/add-genre.jsp").forward(request, response);
        }
    }
} 