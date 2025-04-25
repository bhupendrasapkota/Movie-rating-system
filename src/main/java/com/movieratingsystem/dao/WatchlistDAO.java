package com.movieratingsystem.dao;

import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.Watchlist;
import java.util.List;

public interface WatchlistDAO {
    Watchlist getWatchlistById(int id);
    Watchlist getWatchlistByUserAndMovie(int userId, int movieId);
    List<Movie> getWatchlistMoviesByUserId(int userId);
    boolean isMovieInWatchlist(int userId, int movieId);
    boolean addToWatchlist(Watchlist watchlist);
    boolean removeFromWatchlist(int userId, int movieId);
}