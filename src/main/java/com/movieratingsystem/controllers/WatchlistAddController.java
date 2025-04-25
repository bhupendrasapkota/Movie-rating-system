package com.movieratingsystem.controllers;

import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.models.Watchlist;
import com.movieratingsystem.service.MovieService;
import com.movieratingsystem.service.WatchlistService;
import java.io.IOException;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "WatchlistAddController", urlPatterns = {"/watchlist/add"})
public class WatchlistAddController extends HttpServlet {

    private WatchlistService watchlistService;
    private MovieService movieService;

    @Override
    public void init() throws ServletException {
        super.init();
        watchlistService = new WatchlistService();
        movieService = new MovieService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserModel user = (UserModel) session.getAttribute("user");

        // Get movie ID from request
        String movieIdParam = request.getParameter("movieId");

        if (movieIdParam == null || movieIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/movies");
            return;
        }

        try {
            int movieId = Integer.parseInt(movieIdParam);

            // Get movie
            Movie movie = movieService.getMovieById(movieId);

            if (movie == null) {
                response.sendRedirect(request.getContextPath() + "/movies");
                return;
            }

            // Create watchlist entry
            Watchlist watchlist = new Watchlist();
            watchlist.setUser(user);
            watchlist.setMovie(movie);
            watchlist.setAddedDate(new Date());

            // Add to watchlist
            watchlistService.addToWatchlist(watchlist);

            // Redirect back to movie details
            response.sendRedirect(request.getContextPath() + "/movie?id=" + movieId);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/movies");
        }
    }
}