package com.movieratingsystem.models;

import java.io.Serializable;
import java.util.Date;

public class Cast implements Serializable {
    private int castId;
    private String castName;
    private Date birthDate;
    private String gender;
    private String biography;
    private byte[] photo;
    private Movie movie;
    private String charName;

    public Cast() {
    }

    public Cast(int castId, String castName, Date birthDate, String gender, String biography, byte[] photo, Movie movie, String charName) {
        this.castId = castId;
        this.castName = castName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.biography = biography;
        this.photo = photo;
        this.movie = movie;
        this.charName = charName;
    }

    // Getters and Setters
    public int getCastId() {
        return castId;
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    public String getCastName() {
        return castName;
    }

    public void setCastName(String castName) {
        this.castName = castName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }
}