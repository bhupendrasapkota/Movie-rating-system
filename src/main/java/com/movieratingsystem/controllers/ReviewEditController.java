package com.movieratingsystem.controllers;

import com.movieratingsystem.models.Review;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.ReviewService;
import java.io.IOException;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ReviewEditController", urlPatterns = {"/review/edit"})
public class ReviewEditController extends HttpServlet {

    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        super.init();
        reviewService = new ReviewService();
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

        // Get review ID
        String reviewIdParam = request.getParameter("id");

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

            // Check if the review belongs to the user
            if (review.getUser().getId() != user.getId() && user.getRole() != UserModel.Role.admin) {
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }

            // Set review in request
            request.setAttribute("review", review);

            // Forward to edit review page
            request.getRequestDispatcher("/WEB-INF/views/edit-review.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/profile");
        }
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
        String reviewIdParam = request.getParameter("reviewId");
        String comment = request.getParameter("comment");

        if (reviewIdParam == null || reviewIdParam.trim().isEmpty() ||
                comment == null || comment.trim().isEmpty()) {
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

            // Check if the review belongs to the user
            if (review.getUser().getId() != user.getId() && user.getRole() != UserModel.Role.admin) {
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }

            // Update review
            review.setComment(comment);
            review.setReviewDate(new Date());

            reviewService.updateReview(review);

            // Redirect to profile
            response.sendRedirect(request.getContextPath() + "/profile");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }
}