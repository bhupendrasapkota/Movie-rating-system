package com.movieratingsystem.dao.impl;

import com.movieratingsystem.dao.MovieDAO;
import com.movieratingsystem.dao.ReviewDAO;
import com.movieratingsystem.dao.UserDAO;
import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.Review;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {

    private final UserDAO userDAO;
    private final MovieDAO movieDAO;

    public ReviewDAOImpl() {
        this.userDAO = new UserDAOImpl();
        this.movieDAO = new MovieDAOImpl();
    }

    @Override
    public Review getReviewById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Review review = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM reviews WHERE review_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                review = mapReview(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting review by ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return review;
    }

    @Override
    public List<Review> getReviewsByMovieId(int movieId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Review> reviews = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM reviews WHERE movie_id = ? ORDER BY review_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movieId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(mapReview(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting reviews by movie ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return reviews;
    }

    @Override
    public List<Review> getReviewsByUserId(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Review> reviews = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM reviews WHERE user_id = ? ORDER BY review_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(mapReview(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting reviews by user ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return reviews;
    }

    @Override
    public List<Review> getRecentReviews(int limit) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Review> reviews = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM reviews ORDER BY review_date DESC LIMIT ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(mapReview(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent reviews: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return reviews;
    }

    @Override
    public List<Review> searchReviews(String searchTerm, int page, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Review> reviews = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT r.* FROM reviews r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "JOIN movies m ON r.movie_id = m.movie_id " +
                    "WHERE r.comment LIKE ? OR u.name LIKE ? OR m.title LIKE ? " +
                    "ORDER BY r.review_date DESC LIMIT ? OFFSET ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            stmt.setString(3, "%" + searchTerm + "%");
            stmt.setInt(4, pageSize);
            stmt.setInt(5, (page - 1) * pageSize);
            rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(mapReview(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching reviews: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return reviews;
    }

    @Override
    public int getTotalReviews() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM reviews";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total reviews: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return count;
    }

    @Override
    public int getTotalSearchResults(String searchTerm) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM reviews r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "JOIN movies m ON r.movie_id = m.movie_id " +
                    "WHERE r.comment LIKE ? OR u.name LIKE ? OR m.title LIKE ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            stmt.setString(3, "%" + searchTerm + "%");
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total search results: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return count;
    }

    @Override
    public boolean addReview(Review review) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO reviews (user_id, movie_id, comment, review_date) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, review.getUser().getId());
            stmt.setInt(2, review.getMovie().getMovieId());
            stmt.setString(3, review.getComment());
            stmt.setTimestamp(4, new java.sql.Timestamp(review.getReviewDate().getTime()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    review.setReviewId(rs.getInt(1));
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding review: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return success;
    }

    @Override
    public boolean updateReview(Review review) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE reviews SET comment = ?, review_date = ? WHERE review_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, review.getComment());
            stmt.setTimestamp(2, new java.sql.Timestamp(review.getReviewDate().getTime()));
            stmt.setInt(3, review.getReviewId());

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating review: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    @Override
    public boolean deleteReview(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM reviews WHERE review_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting review: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    private Review mapReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getInt("review_id"));
        review.setComment(rs.getString("comment"));
        review.setReviewDate(rs.getTimestamp("review_date"));

        // Get the user
        int userId = rs.getInt("user_id");
        UserModel user = userDAO.getUserById(userId);
        review.setUser(user);

        // Get the movie
        int movieId = rs.getInt("movie_id");
        Movie movie = movieDAO.getMovieById(movieId);
        review.setMovie(movie);

        return review;
    }
}