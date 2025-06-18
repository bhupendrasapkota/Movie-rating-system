package com.movieratingsystem.dao;

import com.movieratingsystem.models.Rating;
import java.util.List;

public interface RatingDAO {
    Rating getRatingById(int id);
    Rating getRatingByUserAndMovie(int userId, int movieId);
    List<Rating> getRatingsByMovieId(int movieId);
    List<Rating> getRatingsByUserId(int userId);
    double getAverageRatingForMovie(int movieId);
    int getTotalRatings();
    boolean addRating(Rating rating);
    boolean updateRating(Rating rating);
    boolean deleteRating(int id);
}