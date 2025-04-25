package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.GenreService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminDeleteGenreController", urlPatterns = {"/admin/genres/delete"})
public class AdminDeleteGenreController extends HttpServlet {

    private GenreService genreService;

    @Override
    public void init() throws ServletException {
        super.init();
        genreService = new GenreService();
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

        // Get genre ID
        String genreIdParam = request.getParameter("genreId");
        if (genreIdParam == null || genreIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/genres");
            return;
        }

        try {
            int genreId = Integer.parseInt(genreIdParam);

            // Delete genre
            boolean success = genreService.deleteGenre(genreId);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/genres?success=Genre deleted successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/genres?error=Failed to delete genre");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/genres");
        }
    }
} 