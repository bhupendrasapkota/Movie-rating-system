package com.movieratingsystem.dao.impl;

import com.movieratingsystem.dao.MovieDAO;
import com.movieratingsystem.dao.UserDAO;
import com.movieratingsystem.dao.WatchlistDAO;
import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.models.Watchlist;
import com.movieratingsystem.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WatchlistDAOImpl implements WatchlistDAO {

    private final UserDAO userDAO;
    private final MovieDAO movieDAO;

    public WatchlistDAOImpl() {
        this.userDAO = new UserDAOImpl();
        this.movieDAO = new MovieDAOImpl();
    }

    @Override
    public Watchlist getWatchlistById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Watchlist watchlist = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM watchlist WHERE watchlist_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                watchlist = mapWatchlist(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting watchlist by ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return watchlist;
    }

    @Override
    public Watchlist getWatchlistByUserAndMovie(int userId, int movieId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Watchlist watchlist = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM watchlist WHERE user_id = ? AND movie_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, movieId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                watchlist = mapWatchlist(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting watchlist by user and movie: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return watchlist;
    }

    @Override
    public List<Movie> getWatchlistMoviesByUserId(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Movie> movies = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT m.* FROM movies m " +
                    "JOIN watchlist w ON m.movie_id = w.movie_id " +
                    "WHERE w.user_id = ? " +
                    "ORDER BY w.added_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setReleaseDate(rs.getDate("release_date"));
                movie.setMinutes(rs.getInt("minutes"));
                movie.setImage(rs.getBytes("image"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.err.println("Error getting watchlist movies by user ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return movies;
    }

    @Override
    public boolean isMovieInWatchlist(int userId, int movieId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean inWatchlist = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM watchlist WHERE user_id = ? AND movie_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, movieId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                inWatchlist = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if movie is in watchlist: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return inWatchlist;
    }

    @Override
    public boolean addToWatchlist(Watchlist watchlist) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();

            // Check if the movie is already in the watchlist
            if (isMovieInWatchlist(watchlist.getUser().getId(), watchlist.getMovie().getMovieId())) {
                return true; // Already in watchlist, consider it a success
            }

            String sql = "INSERT INTO watchlist (user_id, movie_id, added_date) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, watchlist.getUser().getId());
            stmt.setInt(2, watchlist.getMovie().getMovieId());
            stmt.setTimestamp(3, new java.sql.Timestamp(watchlist.getAddedDate().getTime()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    watchlist.setWatchlistId(rs.getInt(1));
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding to watchlist: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return success;
    }

    @Override
    public boolean removeFromWatchlist(int userId, int movieId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM watchlist WHERE user_id = ? AND movie_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, movieId);

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error removing from watchlist: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    private Watchlist mapWatchlist(ResultSet rs) throws SQLException {
        Watchlist watchlist = new Watchlist();
        watchlist.setWatchlistId(rs.getInt("watchlist_id"));
        watchlist.setAddedDate(rs.getTimestamp("added_date"));

        // Get the user
        int userId = rs.getInt("user_id");
        UserModel user = userDAO.getUserById(userId);
        watchlist.setUser(user);

        // Get the movie
        int movieId = rs.getInt("movie_id");
        Movie movie = movieDAO.getMovieById(movieId);
        watchlist.setMovie(movie);

        return watchlist;
    }
}