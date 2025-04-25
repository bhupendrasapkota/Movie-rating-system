package com.movieratingsystem.dao;

import com.movieratingsystem.models.Review;
import java.util.List;

public interface ReviewDAO {
    Review getReviewById(int id);
    List<Review> getReviewsByMovieId(int movieId);
    List<Review> getReviewsByUserId(int userId);
    List<Review> getRecentReviews(int limit);
    List<Review> searchReviews(String searchTerm, int page, int pageSize);
    int getTotalReviews();
    int getTotalSearchResults(String searchTerm);
    boolean addReview(Review review);
    boolean updateReview(Review review);
    boolean deleteReview(int id);
}