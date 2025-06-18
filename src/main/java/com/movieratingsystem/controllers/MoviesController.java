package com.movieratingsystem.controllers;

import com.movieratingsystem.models.Genre;
import com.movieratingsystem.models.Movie;
import com.movieratingsystem.service.GenreService;
import com.movieratingsystem.service.MovieService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "MoviesController", urlPatterns = {"/movies"})
public class MoviesController extends HttpServlet {

    private MovieService movieService;
    private GenreService genreService;

    @Override
    public void init() throws ServletException {
        super.init();
        movieService = new MovieService();
        genreService = new GenreService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get search parameters
        String searchTerm = request.getParameter("search");
        String[] genreParams = request.getParameterValues("genre");
        String yearFromParam = request.getParameter("yearFrom");
        String yearToParam = request.getParameter("yearTo");
        String pageParam = request.getParameter("page");

        // Parse parameters
        List<Integer> genreIds = new ArrayList<>();
        if (genreParams != null) {
            for (String genreId : genreParams) {
                try {
                    genreIds.add(Integer.parseInt(genreId));
                } catch (NumberFormatException e) {
                    // Ignore invalid genre IDs
                }
            }
        }

        Integer yearFrom = null;
        if (yearFromParam != null && !yearFromParam.trim().isEmpty()) {
            try {
                yearFrom = Integer.parseInt(yearFromParam);
            } catch (NumberFormatException e) {
                // Ignore invalid year
            }
        }

        Integer yearTo = null;
        if (yearToParam != null && !yearToParam.trim().isEmpty()) {
            try {
                yearTo = Integer.parseInt(yearToParam);
            } catch (NumberFormatException e) {
                // Ignore invalid year
            }
        }

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
        int pageSize = 12;

        // Get movies based on search criteria
        List<Movie> movies = movieService.searchMovies(searchTerm, genreIds, yearFrom, yearTo, page, pageSize);
        request.setAttribute("movies", movies);

        // Get all genres for filter
        List<Genre> genres = genreService.getAllGenres();
        request.setAttribute("genres", genres);

        // Get total pages for pagination
        int totalMovies = movieService.getTotalSearchResults(searchTerm, genreIds, yearFrom, yearTo);
        int totalPages = (int) Math.ceil((double) totalMovies / pageSize);

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // Forward to movies page
        request.getRequestDispatcher("/WEB-INF/views/movies.jsp").forward(request, response);
    }
}