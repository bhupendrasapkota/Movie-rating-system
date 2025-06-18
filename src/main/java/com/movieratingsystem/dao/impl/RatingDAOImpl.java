package com.movieratingsystem.dao.impl;

import com.movieratingsystem.dao.MovieDAO;
import com.movieratingsystem.dao.RatingDAO;
import com.movieratingsystem.dao.UserDAO;
import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.Rating;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RatingDAOImpl implements RatingDAO {

    private final UserDAO userDAO;
    private final MovieDAO movieDAO;

    public RatingDAOImpl() {
        this.userDAO = new UserDAOImpl();
        this.movieDAO = new MovieDAOImpl();
    }

    @Override
    public Rating getRatingById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Rating rating = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM ratings WHERE rating_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                rating = mapRating(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting rating by ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return rating;
    }

    @Override
    public Rating getRatingByUserAndMovie(int userId, int movieId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Rating rating = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM ratings WHERE user_id = ? AND movie_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, movieId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                rating = mapRating(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting rating by user and movie: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return rating;
    }

    @Override
    public List<Rating> getRatingsByMovieId(int movieId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Rating> ratings = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM ratings WHERE movie_id = ? ORDER BY rating_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movieId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ratings.add(mapRating(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting ratings by movie ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return ratings;
    }

    @Override
    public List<Rating> getRatingsByUserId(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Rating> ratings = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM ratings WHERE user_id = ? ORDER BY rating_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ratings.add(mapRating(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting ratings by user ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return ratings;
    }

    @Override
    public double getAverageRatingForMovie(int movieId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double averageRating = 0.0;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT AVG(rating) FROM ratings WHERE movie_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movieId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                averageRating = rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting average rating for movie: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return averageRating;
    }

    @Override
    public int getTotalRatings() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM ratings";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total ratings: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return count;
    }

    @Override
    public boolean addRating(Rating rating) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();

            // Check if the user has already rated this movie
            Rating existingRating = getRatingByUserAndMovie(rating.getUser().getId(), rating.getMovie().getMovieId());

            if (existingRating != null) {
                // Update existing rating
                String updateSql = "UPDATE ratings SET rating = ?, rating_date = ? WHERE rating_id = ?";
                stmt = conn.prepareStatement(updateSql);
                stmt.setInt(1, rating.getRating());
                stmt.setTimestamp(2, new java.sql.Timestamp(rating.getRatingDate().getTime()));
                stmt.setInt(3, existingRating.getRatingId());

                int affectedRows = stmt.executeUpdate();
                success = affectedRows > 0;
            } else {
                // Insert new rating
                String insertSql = "INSERT INTO ratings (user_id, movie_id, rating, rating_date) VALUES (?, ?, ?, ?)";
                stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, rating.getUser().getId());
                stmt.setInt(2, rating.getMovie().getMovieId());
                stmt.setInt(3, rating.getRating());
                stmt.setTimestamp(4, new java.sql.Timestamp(rating.getRatingDate().getTime()));

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        rating.setRatingId(rs.getInt(1));
                        success = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding rating: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return success;
    }

    @Override
    public boolean updateRating(Rating rating) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE ratings SET rating = ?, rating_date = ? WHERE rating_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, rating.getRating());
            stmt.setTimestamp(2, new java.sql.Timestamp(rating.getRatingDate().getTime()));
            stmt.setInt(3, rating.getRatingId());

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating rating: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    @Override
    public boolean deleteRating(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM ratings WHERE rating_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting rating: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    private Rating mapRating(ResultSet rs) throws SQLException {
        Rating rating = new Rating();
        rating.setRatingId(rs.getInt("rating_id"));
        rating.setRating(rs.getInt("rating"));
        rating.setRatingDate(rs.getTimestamp("rating_date"));

        // Get the user
        int userId = rs.getInt("user_id");
        UserModel user = userDAO.getUserById(userId);
        rating.setUser(user);

        // Get the movie
        int movieId = rs.getInt("movie_id");
        Movie movie = movieDAO.getMovieById(movieId);
        rating.setMovie(movie);

        return rating;
    }
}