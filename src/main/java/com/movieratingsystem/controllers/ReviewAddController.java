package com.movieratingsystem.controllers;

import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.Review;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.MovieService;
import com.movieratingsystem.service.ReviewService;
import java.io.IOException;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ReviewAddController", urlPatterns = {"/review/add"})
public class ReviewAddController extends HttpServlet {

    private ReviewService reviewService;
    private MovieService movieService;

    @Override
    public void init() throws ServletException {
        super.init();
        reviewService = new ReviewService();
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
        String comment = request.getParameter("comment");

        if (movieIdParam == null || movieIdParam.trim().isEmpty() ||
                comment == null || comment.trim().isEmpty()) {
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

            // Create review
            Review review = new Review();
            review.setUser(user);
            review.setMovie(movie);
            review.setComment(comment);
            review.setReviewDate(new Date());

            // Add review
            reviewService.addReview(review);

            // Redirect back to movie details
            response.sendRedirect(request.getContextPath() + "/movie?id=" + movieId);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/movies");
        }
    }
}