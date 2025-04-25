package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.MovieService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminDeleteMovieController", urlPatterns = {"/admin/movies/delete"})
public class AdminDeleteMovieController extends HttpServlet {

    private MovieService movieService;

    @Override
    public void init() throws ServletException {
        super.init();
        movieService = new MovieService();
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

        // Get movie ID
        String movieIdParam = request.getParameter("movieId");

        if (movieIdParam == null || movieIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/movies");
            return;
        }

        try {
            int movieId = Integer.parseInt(movieIdParam);

            // Delete movie
            boolean success = movieService.deleteMovie(movieId);

            if (success) {
                // Redirect to admin movies page with success message
                response.sendRedirect(request.getContextPath() + "/admin/movies?success=Movie deleted successfully");
            } else {
                // Redirect to admin movies page with error message
                response.sendRedirect(request.getContextPath() + "/admin/movies?error=Failed to delete movie");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/movies");
        }
    }
}