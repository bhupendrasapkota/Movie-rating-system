package com.movieratingsystem.models;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {
    private int reviewId;
    private UserModel user;
    private Movie movie;
    private String comment;
    private Date reviewDate;

    public Review() {
    }

    public Review(int reviewId, UserModel user, Movie movie, String comment, Date reviewDate) {
        this.reviewId = reviewId;
        this.user = user;
        this.movie = movie;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
}