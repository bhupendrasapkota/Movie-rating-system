package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.MovieService;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminMoviesController", urlPatterns = {"/admin/movies"})
public class AdminMoviesController extends HttpServlet {

    private MovieService movieService;

    @Override
    public void init() throws ServletException {
        super.init();
        movieService = new MovieService();
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

        // Get search parameters
        String searchTerm = request.getParameter("search");
        String pageParam = request.getParameter("page");

        // Parse page parameter
        int page = 1;
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                // Ignore invalid page
            }
        }

        // Set page size
        int pageSize = 10;

        // Get movies
        List<Movie> movies = movieService.searchMovies(searchTerm, null, null, null, page, pageSize);
        request.setAttribute("movies", movies);

        // Get total pages for pagination
        int totalMovies = movieService.getTotalSearchResults(searchTerm, null, null, null);
        int totalPages = (int) Math.ceil((double) totalMovies / pageSize);

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // Forward to admin movies page
        request.getRequestDispatcher("/WEB-INF/views/admin/movies.jsp").forward(request, response);
    }
}