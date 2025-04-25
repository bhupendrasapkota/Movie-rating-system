package com.movieratingsystem.controllers;

import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.Rating;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.MovieService;
import com.movieratingsystem.service.RatingService;
import java.io.IOException;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "RatingAddController", urlPatterns = {"/rating/add"})
public class RatingAddController extends HttpServlet {

    private RatingService ratingService;
    private MovieService movieService;

    @Override
    public void init() throws ServletException {
        super.init();
        ratingService = new RatingService();
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

        // Get parameters
        String movieIdParam = request.getParameter("movieId");
        String ratingParam = request.getParameter("rating");

        if (movieIdParam == null || movieIdParam.trim().isEmpty() ||
                ratingParam == null || ratingParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/movies");
            return;
        }

        try {
            int movieId = Integer.parseInt(movieIdParam);
            int ratingValue = Integer.parseInt(ratingParam);

            // Validate rating value
            if (ratingValue < 1 || ratingValue > 10) {
                response.sendRedirect(request.getContextPath() + "/movie?id=" + movieId);
                return;
            }

            // Get movie
            Movie movie = movieService.getMovieById(movieId);

            if (movie == null) {
                response.sendRedirect(request.getContextPath() + "/movies");
                return;
            }

            // Create rating
            Rating rating = new Rating();
            rating.setUser(user);
            rating.setMovie(movie);
            rating.setRating(ratingValue);
            rating.setRatingDate(new Date());

            // Add rating
            ratingService.addRating(rating);

            // Redirect back to movie details
            response.sendRedirect(request.getContextPath() + "/movie?id=" + movieId);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/movies");
        }
    }
}