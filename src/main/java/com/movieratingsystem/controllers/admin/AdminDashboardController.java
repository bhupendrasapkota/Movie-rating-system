package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.MovieService;
import com.movieratingsystem.service.RatingService;
import com.movieratingsystem.service.ReviewService;
import com.movieratingsystem.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminDashboardController", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardController extends HttpServlet {

    private MovieService movieService;
    private UserService userService;
    private ReviewService reviewService;
    private RatingService ratingService;

    @Override
    public void init() throws ServletException {
        super.init();
        movieService = new MovieService();
        userService = new UserService();
        reviewService = new ReviewService();
        ratingService = new RatingService();
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

        // Get statistics
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalMovies", movieService.getTotalMovies());
        stats.put("totalUsers", userService.getTotalUsers());
        stats.put("totalReviews", reviewService.getTotalReviews());
        stats.put("totalRatings", ratingService.getTotalRatings());

        request.setAttribute("stats", stats);

        // Forward to admin dashboard
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
}