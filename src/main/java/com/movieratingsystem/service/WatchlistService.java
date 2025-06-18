package com.movieratingsystem.service;

import com.movieratingsystem.dao.WatchlistDAO;
import com.movieratingsystem.dao.impl.WatchlistDAOImpl;
import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.Watchlist;
import java.util.List;

public class WatchlistService {

    private final WatchlistDAO watchlistDAO;

    public WatchlistService() {
        this.watchlistDAO = new WatchlistDAOImpl();
    }

    public Watchlist getWatchlistById(int id) {
        return watchlistDAO.getWatchlistById(id);
    }

    public Watchlist getWatchlistByUserAndMovie(int userId, int movieId) {
        return watchlistDAO.getWatchlistByUserAndMovie(userId, movieId);
    }

    public List<Movie> getWatchlistMoviesByUserId(int userId) {
        return watchlistDAO.getWatchlistMoviesByUserId(userId);
    }

    public boolean isMovieInWatchlist(int userId, int movieId) {
        return watchlistDAO.isMovieInWatchlist(userId, movieId);
    }

    public boolean addToWatchlist(Watchlist watchlist) {
        if (watchlist.getUser() == null || watchlist.getUser().getId() <= 0) {
            return false;
        }

        if (watchlist.getMovie() == null || watchlist.getMovie().getMovieId() <= 0) {
            return false;
        }

        return watchlistDAO.addToWatchlist(watchlist);
    }

    public boolean removeFromWatchlist(int userId, int movieId) {
        return watchlistDAO.removeFromWatchlist(userId, movieId);
    }
}