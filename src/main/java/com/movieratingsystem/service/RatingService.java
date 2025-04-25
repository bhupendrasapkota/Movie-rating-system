package com.movieratingsystem.service;

import com.movieratingsystem.dao.RatingDAO;
import com.movieratingsystem.dao.impl.RatingDAOImpl;
import com.movieratingsystem.models.Rating;
import com.movieratingsystem.utils.ValidationUtil;
import java.util.List;

public class RatingService {

    private final RatingDAO ratingDAO;

    public RatingService() {
        this.ratingDAO = new RatingDAOImpl();
    }

    public Rating getRatingById(int id) {
        return ratingDAO.getRatingById(id);
    }

    public Rating getRatingByUserAndMovie(int userId, int movieId) {
        return ratingDAO.getRatingByUserAndMovie(userId, movieId);
    }

    public List<Rating> getRatingsByMovieId(int movieId) {
        return ratingDAO.getRatingsByMovieId(movieId);
    }

    public List<Rating> getRatingsByUserId(int userId) {
        return ratingDAO.getRatingsByUserId(userId);
    }

    public double getAverageRatingForMovie(int movieId) {
        return ratingDAO.getAverageRatingForMovie(movieId);
    }

    public int getTotalRatings() {
        return ratingDAO.getTotalRatings();
    }

    public boolean addRating(Rating rating) {
        // Validate rating data
        if (!ValidationUtil.isValidRating(rating.getRating())) {
            return false;
        }

        if (rating.getUser() == null || rating.getUser().getId() <= 0) {
            return false;
        }

        if (rating.getMovie() == null || rating.getMovie().getMovieId() <= 0) {
            return false;
        }

        return ratingDAO.addRating(rating);
    }

    public boolean updateRating(Rating rating) {
        // Validate rating data
        if (!ValidationUtil.isValidRating(rating.getRating())) {
            return false;
        }

        return ratingDAO.updateRating(rating);
    }

    public boolean deleteRating(int id) {
        return ratingDAO.deleteRating(id);
    }
}