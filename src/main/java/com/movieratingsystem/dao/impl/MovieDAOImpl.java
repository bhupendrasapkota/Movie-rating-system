package com.movieratingsystem.dao.impl;

import com.movieratingsystem.dao.MovieDAO;
import com.movieratingsystem.models.Movie;
import com.movieratingsystem.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovieDAOImpl implements MovieDAO {

    @Override
    public Movie getMovieById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Movie movie = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM movies WHERE movie_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                movie = mapMovie(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting movie by ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return movie;
    }

    @Override
    public List<Movie> getAllMovies() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Movie> movies = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM movies ORDER BY title";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                movies.add(mapMovie(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all movies: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return movies;
    }

    @Override
    public List<Movie> getFeaturedMovies(int limit) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Movie> movies = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            // Modified query to show movies even without ratings
            String sql = "SELECT m.*, COALESCE(AVG(r.rating), 0) as avg_rating FROM movies m " +
                    "LEFT JOIN ratings r ON m.movie_id = r.movie_id " +
                    "GROUP BY m.movie_id " +
                    "ORDER BY avg_rating DESC, m.release_date DESC " +
                    "LIMIT ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            rs = stmt.executeQuery();

            while (rs.next()) {
                movies.add(mapMovie(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting featured movies: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return movies;
    }

    @Override
    public List<Movie> searchMovies(String searchTerm, List<Integer> genreIds, Integer yearFrom, Integer yearTo, int page, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Movie> movies = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            StringBuilder sqlBuilder = new StringBuilder("SELECT DISTINCT m.* FROM movies m");

            // Join with movie_genres if genre filter is applied
            if (genreIds != null && !genreIds.isEmpty()) {
                sqlBuilder.append(" JOIN movie_genres mg ON m.movie_id = mg.movie_id");
            }

            sqlBuilder.append(" WHERE 1=1");

            // Add search condition
            if (searchTerm != null && !searchTerm.isEmpty()) {
                sqlBuilder.append(" AND m.title LIKE ?");
            }

            // Add genre condition
            if (genreIds != null && !genreIds.isEmpty()) {
                sqlBuilder.append(" AND mg.genre_id IN (");
                for (int i = 0; i < genreIds.size(); i++) {
                    if (i > 0) {
                        sqlBuilder.append(",");
                    }
                    sqlBuilder.append("?");
                }
                sqlBuilder.append(")");
            }

            // Add year range conditions
            if (yearFrom != null) {
                sqlBuilder.append(" AND EXTRACT(YEAR FROM m.release_date) >= ?");
            }

            if (yearTo != null) {
                sqlBuilder.append(" AND EXTRACT(YEAR FROM m.release_date) <= ?");
            }

            // Add order by and pagination
            sqlBuilder.append(" ORDER BY m.title LIMIT ? OFFSET ?");

            stmt = conn.prepareStatement(sqlBuilder.toString());

            int paramIndex = 1;

            // Set search parameter
            if (searchTerm != null && !searchTerm.isEmpty()) {
                stmt.setString(paramIndex++, "%" + searchTerm + "%");
            }

            // Set genre parameters
            if (genreIds != null && !genreIds.isEmpty()) {
                for (Integer genreId : genreIds) {
                    stmt.setInt(paramIndex++, genreId);
                }
            }

            // Set year range parameters
            if (yearFrom != null) {
                stmt.setInt(paramIndex++, yearFrom);
            }

            if (yearTo != null) {
                stmt.setInt(paramIndex++, yearTo);
            }

            // Set pagination parameters
            stmt.setInt(paramIndex++, pageSize);
            stmt.setInt(paramIndex, (page - 1) * pageSize);

            rs = stmt.executeQuery();

            while (rs.next()) {
                movies.add(mapMovie(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching movies: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return movies;
    }

    @Override
    public int getTotalMovies() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM movies";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total movies: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return count;
    }

    @Override
    public int getTotalSearchResults(String searchTerm, List<Integer> genreIds, Integer yearFrom, Integer yearTo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(DISTINCT m.movie_id) FROM movies m");

            // Join with movie_genres if genre filter is applied
            if (genreIds != null && !genreIds.isEmpty()) {
                sqlBuilder.append(" JOIN movie_genres mg ON m.movie_id = mg.movie_id");
            }

            sqlBuilder.append(" WHERE 1=1");

            // Add search condition
            if (searchTerm != null && !searchTerm.isEmpty()) {
                sqlBuilder.append(" AND m.title LIKE ?");
            }

            // Add genre condition
            if (genreIds != null && !genreIds.isEmpty()) {
                sqlBuilder.append(" AND mg.genre_id IN (");
                for (int i = 0; i < genreIds.size(); i++) {
                    if (i > 0) {
                        sqlBuilder.append(",");
                    }
                    sqlBuilder.append("?");
                }
                sqlBuilder.append(")");
            }

            // Add year range conditions
            if (yearFrom != null) {
                sqlBuilder.append(" AND EXTRACT(YEAR FROM m.release_date) >= ?");
            }

            if (yearTo != null) {
                sqlBuilder.append(" AND EXTRACT(YEAR FROM m.release_date) <= ?");
            }

            stmt = conn.prepareStatement(sqlBuilder.toString());

            int paramIndex = 1;

            // Set search parameter
            if (searchTerm != null && !searchTerm.isEmpty()) {
                stmt.setString(paramIndex++, "%" + searchTerm + "%");
            }

            // Set genre parameters
            if (genreIds != null && !genreIds.isEmpty()) {
                for (Integer genreId : genreIds) {
                    stmt.setInt(paramIndex++, genreId);
                }
            }

            // Set year range parameters
            if (yearFrom != null) {
                stmt.setInt(paramIndex++, yearFrom);
            }

            if (yearTo != null) {
                stmt.setInt(paramIndex, yearTo);
            }

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
    public boolean addMovie(Movie movie) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO movies (title, release_date, minutes, image) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, movie.getTitle());
            stmt.setDate(2, new java.sql.Date(movie.getReleaseDate().getTime()));
            stmt.setInt(3, movie.getMinutes());
            stmt.setBytes(4, movie.getImage());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    movie.setMovieId(rs.getInt(1));
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding movie: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return success;
    }

    @Override
    public boolean updateMovie(Movie movie) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql;

            if (movie.getImage() != null) {
                sql = "UPDATE movies SET title = ?, release_date = ?, minutes = ?, image = ? WHERE movie_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, movie.getTitle());
                stmt.setDate(2, new java.sql.Date(movie.getReleaseDate().getTime()));
                stmt.setInt(3, movie.getMinutes());
                stmt.setBytes(4, movie.getImage());
                stmt.setInt(5, movie.getMovieId());
            } else {
                sql = "UPDATE movies SET title = ?, release_date = ?, minutes = ? WHERE movie_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, movie.getTitle());
                stmt.setDate(2, new java.sql.Date(movie.getReleaseDate().getTime()));
                stmt.setInt(3, movie.getMinutes());
                stmt.setInt(4, movie.getMovieId());
            }

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating movie: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    @Override
    public boolean deleteMovie(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            // First delete related records in movie_genres
            String sql = "DELETE FROM movie_genres WHERE movie_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            DBUtil.closePreparedStatement(stmt);

            // Then delete the movie
            sql = "DELETE FROM movies WHERE movie_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting movie: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    @Override
    public boolean addMovieGenre(int movieId, int genreId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO movie_genres (movie_id, genre_id) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movieId);
            stmt.setInt(2, genreId);

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding movie genre: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    @Override
    public boolean removeAllMovieGenres(int movieId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM movie_genres WHERE movie_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movieId);

            stmt.executeUpdate();
            success = true;
        } catch (SQLException e) {
            System.err.println("Error removing movie genres: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    private Movie mapMovie(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setMovieId(rs.getInt("movie_id"));
        movie.setTitle(rs.getString("title"));
        movie.setReleaseDate(rs.getDate("release_date"));
        movie.setMinutes(rs.getInt("minutes"));
        movie.setImage(rs.getBytes("image"));
        return movie;
    }
}