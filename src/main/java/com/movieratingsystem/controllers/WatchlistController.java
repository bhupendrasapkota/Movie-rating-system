package com.movieratingsystem.controllers;

import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.models.Watchlist;
import com.movieratingsystem.service.MovieService;
import com.movieratingsystem.service.WatchlistService;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "WatchlistController", urlPatterns = {"/watchlist"})
public class WatchlistController extends HttpServlet {

    private WatchlistService watchlistService;

    @Override
    public void init() throws ServletException {
        super.init();
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

        // Get user's watchlist
        List<Movie> watchlist = watchlistService.getWatchlistMoviesByUserId(user.getId());
        request.setAttribute("watchlist", watchlist);

        // Forward to watchlist page
        request.getRequestDispatcher("/WEB-INF/views/watchlist.jsp").forward(request, response);
    }
}