package com.movieratingsystem.models;

import java.io.Serializable;
import java.util.Date;

public class Rating implements Serializable {
    private int ratingId;
    private UserModel user;
    private Movie movie;
    private int rating;
    private Date ratingDate;

    public Rating() {
    }

    public Rating(int ratingId, UserModel user, Movie movie, int rating, Date ratingDate) {
        this.ratingId = ratingId;
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.ratingDate = ratingDate;
    }

    // Getters and Setters
    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }
}