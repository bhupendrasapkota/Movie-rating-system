package com.movieratingsystem.models;
import java.io.Serializable;
import java.util.Date;

public class Watchlist implements Serializable {
    private int watchlistId;
    private UserModel user;
    private Movie movie;
    private Date addedDate;

    public Watchlist() {
    }

    public Watchlist(int watchlistId, UserModel user, Movie movie, Date addedDate) {
        this.watchlistId = watchlistId;
        this.user = user;
        this.movie = movie;
        this.addedDate = addedDate;
    }

    // Getters and Setters
    public int getWatchlistId() {
        return watchlistId;
    }

    public void setWatchlistId(int watchlistId) {
        this.watchlistId = watchlistId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }
}