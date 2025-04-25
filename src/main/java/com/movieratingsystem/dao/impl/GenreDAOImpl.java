package com.movieratingsystem.dao.impl;

import com.movieratingsystem.dao.GenreDAO;
import com.movieratingsystem.models.Genre;
import com.movieratingsystem.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GenreDAOImpl implements GenreDAO {

    @Override
    public Genre getGenreById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Genre genre = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM genres WHERE genre_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                genre = mapGenre(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting genre by ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return genre;
    }

    @Override
    public List<Genre> getAllGenres() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Genre> genres = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM genres ORDER BY genre_name";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                genres.add(mapGenre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all genres: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return genres;
    }

    @Override
    public List<Genre> getGenresByMovieId(int movieId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Genre> genres = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT g.* FROM genres g " +
                    "JOIN movie_genres mg ON g.genre_id = mg.genre_id " +
                    "WHERE mg.movie_id = ? " +
                    "ORDER BY g.genre_name";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movieId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                genres.add(mapGenre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting genres by movie ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return genres;
    }

    @Override
    public List<Genre> searchGenres(String searchTerm, int page, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Genre> genres = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM genres WHERE genre_name LIKE ? ORDER BY genre_name LIMIT ? OFFSET ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setInt(2, pageSize);
            stmt.setInt(3, (page - 1) * pageSize);
            rs = stmt.executeQuery();

            while (rs.next()) {
                genres.add(mapGenre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching genres: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return genres;
    }

    @Override
    public int getTotalGenres() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM genres";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total genres: " + e.getMessage());
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
            String sql = "SELECT COUNT(*) FROM genres WHERE genre_name LIKE ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
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
    public boolean addGenre(Genre genre) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO genres (genre_name, description) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, genre.getGenreName());
            stmt.setString(2, genre.getDescription());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    genre.setGenreId(rs.getInt(1));
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding genre: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return success;
    }

    @Override
    public boolean updateGenre(Genre genre) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE genres SET genre_name = ?, description = ? WHERE genre_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, genre.getGenreName());
            stmt.setString(2, genre.getDescription());
            stmt.setInt(3, genre.getGenreId());

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating genre: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    @Override
    public boolean deleteGenre(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            // First delete related records in movie_genres
            String sql = "DELETE FROM movie_genres WHERE genre_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            DBUtil.closePreparedStatement(stmt);

            // Then delete the genre
            sql = "DELETE FROM genres WHERE genre_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting genre: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    @Override
    public List<Integer> getGenreIdsByMovieId(int movieId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Integer> genreIds = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT genre_id FROM movie_genres WHERE movie_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movieId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                genreIds.add(rs.getInt("genre_id"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting genre IDs by movie ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return genreIds;
    }

    private Genre mapGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setGenreId(rs.getInt("genre_id"));
        genre.setGenreName(rs.getString("genre_name"));
        genre.setDescription(rs.getString("description"));
        return genre;
    }
}