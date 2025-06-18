package com.movieratingsystem.controllers;

import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.Review;
import com.movieratingsystem.service.MovieService;
import com.movieratingsystem.service.ReviewService;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "HomeController", urlPatterns = {"", "/index", "/home"})
public class HomeController extends HttpServlet {

    private MovieService movieService;
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        super.init();
        movieService = new MovieService();
        reviewService = new ReviewService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get featured movies
        List<Movie> featuredMovies = movieService.getFeaturedMovies(6);
        request.setAttribute("featuredMovies", featuredMovies);

        // Get recent reviews
        List<Review> recentReviews = reviewService.getRecentReviews(5);
        request.setAttribute("recentReviews", recentReviews);

        // Forward to the home page
        request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
    }
}