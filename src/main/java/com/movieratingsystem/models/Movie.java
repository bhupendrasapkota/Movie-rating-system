package com.movieratingsystem.models;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable {
    private int movieId;
    private String title;
    private Date releaseDate;
    private int minutes;
    private byte[] image;

    public Movie() {
    }

    public Movie(int movieId, String title, Date releaseDate, int minutes, byte[] image) {
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.minutes = minutes;
        this.image = image;
    }

    // Getters and Setters
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image =image;
    }
}
