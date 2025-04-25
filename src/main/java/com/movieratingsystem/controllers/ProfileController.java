package com.movieratingsystem.controllers;

import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.Review;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.ReviewService;
import com.movieratingsystem.service.WatchlistService;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ProfileController", urlPatterns = {"/profile"})
public class ProfileController extends HttpServlet {

    private ReviewService reviewService;
    private WatchlistService watchlistService;

    @Override
    public void init() throws ServletException {
        super.init();
        reviewService = new ReviewService();
        watchlistService = new WatchlistService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserModel user = (UserModel) session.getAttribute("user");

        // Get user's reviews
        List<Review> userReviews = reviewService.getReviewsByUserId(user.getId());
        request.setAttribute("userReviews", userReviews);

        // Get user's watchlist
        List<Movie> watchlist = watchlistService.getWatchlistMoviesByUserId(user.getId());
        request.setAttribute("watchlist", watchlist);

        // Forward to profile page
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }
}