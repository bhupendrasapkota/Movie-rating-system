package com.movieratingsystem.controllers;

import com.movieratingsystem.models.Review;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.ReviewService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ReviewDeleteController", urlPatterns = {"/review/delete", "/admin/reviews/delete"})
public class ReviewDeleteController extends HttpServlet {

    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        super.init();
        reviewService = new ReviewService();
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

        // Get review ID
        String reviewIdParam = request.getParameter("reviewId");

        if (reviewIdParam == null || reviewIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        try {
            int reviewId = Integer.parseInt(reviewIdParam);

            // Get review
            Review review = reviewService.getReviewById(reviewId);

            if (review == null) {
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }

            // Check if the review belongs to the user or user is admin
            if (review.getUser().getId() != user.getId() && user.getRole() != UserModel.Role.admin) {
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }

            // Delete review
            boolean success = reviewService.deleteReview(reviewId);

            // Get referer to determine where to redirect
            String referer = request.getHeader("Referer");
            String successMessage = success ? "Review deleted successfully" : "Failed to delete review";

            if (referer != null && referer.contains("/admin/reviews")) {
                // Redirect back to admin reviews page
                response.sendRedirect(request.getContextPath() + "/admin/reviews?success=" + successMessage);
            } else {
                // Redirect to profile
                response.sendRedirect(request.getContextPath() + "/profile?success=" + successMessage);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }
}