package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.Genre;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.GenreService;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminGenresController", urlPatterns = {"/admin/genres"})
public class AdminGenresController extends HttpServlet {

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

        // Get search parameters
        String searchTerm = request.getParameter("search");
        int page = 1;
        int pageSize = 10;

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException e) {
            // Use default page value
        }

        // Get genres with pagination
        List<Genre> genres;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            genres = genreService.searchGenres(searchTerm, page, pageSize);
            request.setAttribute("totalPages", 
                (int) Math.ceil((double) genreService.getTotalSearchResults(searchTerm) / pageSize));
        } else {
            genres = genreService.getAllGenres();
            request.setAttribute("totalPages", 
                (int) Math.ceil((double) genreService.getTotalGenres() / pageSize));
        }

        request.setAttribute("genres", genres);
        request.setAttribute("currentPage", page);

        // Forward to genres page
        request.getRequestDispatcher("/WEB-INF/views/admin/genres.jsp").forward(request, response);
    }
} 