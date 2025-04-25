package com.movieratingsystem.service;

import com.movieratingsystem.dao.ReviewDAO;
import com.movieratingsystem.dao.impl.ReviewDAOImpl;
import com.movieratingsystem.models.Review;
import com.movieratingsystem.utils.ValidationUtil;
import java.util.List;

public class ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewService() {
        this.reviewDAO = new ReviewDAOImpl();
    }

    public Review getReviewById(int id) {
        return reviewDAO.getReviewById(id);
    }

    public List<Review> getReviewsByMovieId(int movieId) {
        return reviewDAO.getReviewsByMovieId(movieId);
    }

    public List<Review> getReviewsByUserId(int userId) {
        return reviewDAO.getReviewsByUserId(userId);
    }

    public List<Review> getRecentReviews(int limit) {
        return reviewDAO.getRecentReviews(limit);
    }

    public List<Review> searchReviews(String searchTerm, int page, int pageSize) {
        return reviewDAO.searchReviews(searchTerm, page, pageSize);
    }

    public int getTotalReviews() {
        return reviewDAO.getTotalReviews();
    }

    public int getTotalSearchResults(String searchTerm) {
        return reviewDAO.getTotalSearchResults(searchTerm);
    }

    public boolean addReview(Review review) {
        // Validate review data
        if (!ValidationUtil.isValidComment(review.getComment())) {
            return false;
        }

        if (review.getUser() == null || review.getUser().getId() <= 0) {
            return false;
        }

        if (review.getMovie() == null || review.getMovie().getMovieId() <= 0) {
            return false;
        }

        return reviewDAO.addReview(review);
    }

    public boolean updateReview(Review review) {
        // Validate review data
        if (!ValidationUtil.isValidComment(review.getComment())) {
            return false;
        }

        return reviewDAO.updateReview(review);
    }

    public boolean deleteReview(int id) {
        return reviewDAO.deleteReview(id);
    }
}