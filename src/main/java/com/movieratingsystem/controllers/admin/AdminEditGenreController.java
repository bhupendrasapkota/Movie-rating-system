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

@WebServlet(name = "AdminEditGenreController", urlPatterns = {"/admin/genres/edit"})
public class AdminEditGenreController extends HttpServlet {

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

        // Get genre ID
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/genres");
            return;
        }

        try {
            int genreId = Integer.parseInt(idParam);
            Genre genre = genreService.getGenreById(genreId);

            if (genre == null) {
                response.sendRedirect(request.getContextPath() + "/admin/genres");
                return;
            }

            request.setAttribute("genre", genre);
            request.getRequestDispatcher("/WEB-INF/views/admin/edit-genre.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/genres");
        }
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
        String genreIdParam = request.getParameter("genreId");
        String genreName = request.getParameter("genreName");
        String description = request.getParameter("description");

        // Validate input
        if (genreIdParam == null || genreIdParam.trim().isEmpty() ||
            genreName == null || genreName.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/genres");
            return;
        }

        try {
            int genreId = Integer.parseInt(genreIdParam);
            Genre genre = genreService.getGenreById(genreId);

            if (genre == null) {
                response.sendRedirect(request.getContextPath() + "/admin/genres");
                return;
            }

            // Update genre object
            genre.setGenreName(genreName.trim());
            genre.setDescription(description != null ? description.trim() : null);

            // Update genre
            boolean success = genreService.updateGenre(genre);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/genres?success=Genre updated successfully");
            } else {
                request.setAttribute("error", "Failed to update genre");
                request.setAttribute("genre", genre);
                request.getRequestDispatcher("/WEB-INF/views/admin/edit-genre.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/genres");
        }
    }
} 