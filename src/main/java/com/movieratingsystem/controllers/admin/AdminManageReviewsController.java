package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.Review;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.ReviewService;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminReviewManagementController", urlPatterns = {"/admin/manage-reviews"})
public class AdminManageReviewsController extends HttpServlet {

    private ReviewService reviewService;
    private static final int ITEMS_PER_PAGE = 10;

    @Override
    public void init() throws ServletException {
        super.init();
        reviewService = new ReviewService();
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

        // Get page number
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                // Keep default page value
            }
        }

        // Get search term
        String searchTerm = request.getParameter("search");
        
        // Get reviews
        List<Review> reviews;
        int totalReviews;
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            reviews = reviewService.searchReviews(searchTerm.trim(), page, ITEMS_PER_PAGE);
            totalReviews = reviewService.getTotalSearchResults(searchTerm.trim());
        } else {
            reviews = reviewService.getRecentReviews(ITEMS_PER_PAGE);
            totalReviews = reviewService.getTotalReviews();
        }

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalReviews / ITEMS_PER_PAGE);
        
        // Set attributes
        request.setAttribute("reviews", reviews);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        
        // Forward to reviews page
        request.getRequestDispatcher("/WEB-INF/views/admin/reviews.jsp").forward(request, response);
    }
} 