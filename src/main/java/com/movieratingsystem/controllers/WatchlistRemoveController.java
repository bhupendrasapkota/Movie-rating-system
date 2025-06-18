package com.movieratingsystem.controllers;

import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.WatchlistService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "WatchlistRemoveController", urlPatterns = {"/watchlist/remove"})
public class WatchlistRemoveController extends HttpServlet {

    private WatchlistService watchlistService;

    @Override
    public void init() throws ServletException {
        super.init();
        watchlistService = new WatchlistService();
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
            response.sendRedirect(request.getContextPath() + "/watchlist");
            return;
        }

        try {
            int movieId = Integer.parseInt(movieIdParam);

            // Remove from watchlist
            watchlistService.removeFromWatchlist(user.getId(), movieId);

            // Get referer to determine where to redirect
            String referer = request.getHeader("Referer");

            if (referer != null && referer.contains("/movie?id=")) {
                // Redirect back to movie details
                response.sendRedirect(referer);
            } else {
                // Redirect to watchlist
                response.sendRedirect(request.getContextPath() + "/watchlist");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/watchlist");
        }
    }
}