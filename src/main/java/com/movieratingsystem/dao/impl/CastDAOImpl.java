package com.movieratingsystem.dao.impl;

import com.movieratingsystem.dao.CastDAO;
import com.movieratingsystem.dao.MovieDAO;
import com.movieratingsystem.models.Cast;
import com.movieratingsystem.models.Movie;
import com.movieratingsystem.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CastDAOImpl implements CastDAO {

    private final MovieDAO movieDAO;

    public CastDAOImpl() {
        this.movieDAO = new MovieDAOImpl();
    }

    @Override
    public Cast getCastById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cast cast = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM cast WHERE cast_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cast = mapCast(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting cast by ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return cast;
    }

    @Override
    public List<Cast> getAllCast() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Cast> castList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM cast ORDER BY cast_name";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                castList.add(mapCast(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all cast: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return castList;
    }

    @Override
    public List<Cast> getCastByMovieId(int movieId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Cast> castList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM cast WHERE movie_id = ? ORDER BY cast_name";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movieId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                castList.add(mapCast(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting cast by movie ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return castList;
    }

    @Override
    public List<Cast> searchCast(String searchTerm, int page, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Cast> castList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM cast WHERE cast_name LIKE ? ORDER BY cast_name LIMIT ? OFFSET ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setInt(2, pageSize);
            stmt.setInt(3, (page - 1) * pageSize);
            rs = stmt.executeQuery();

            while (rs.next()) {
                castList.add(mapCast(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching cast: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return castList;
    }

    @Override
    public int getTotalCast() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM cast";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total cast: " + e.getMessage());
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
            String sql = "SELECT COUNT(*) FROM cast WHERE cast_name LIKE ?";
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
    public boolean addCast(Cast cast) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO cast (cast_name, birth_date, gender, biography, photo, movie_id, char_name) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, cast.getCastName());

            if (cast.getBirthDate() != null) {
                stmt.setDate(2, new java.sql.Date(cast.getBirthDate().getTime()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }

            stmt.setString(3, cast.getGender());
            stmt.setString(4, cast.getBiography());
            stmt.setBytes(5, cast.getPhoto());
            stmt.setInt(6, cast.getMovie().getMovieId());
            stmt.setString(7, cast.getCharName());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    cast.setCastId(rs.getInt(1));
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding cast: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return success;
    }

    @Override
    public boolean updateCast(Cast cast) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql;

            if (cast.getPhoto() != null) {
                sql = "UPDATE cast SET cast_name = ?, birth_date = ?, gender = ?, biography = ?, photo = ?, movie_id = ?, char_name = ? WHERE cast_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, cast.getCastName());

                if (cast.getBirthDate() != null) {
                    stmt.setDate(2, new java.sql.Date(cast.getBirthDate().getTime()));
                } else {
                    stmt.setNull(2, java.sql.Types.DATE);
                }

                stmt.setString(3, cast.getGender());
                stmt.setString(4, cast.getBiography());
                stmt.setBytes(5, cast.getPhoto());
                stmt.setInt(6, cast.getMovie().getMovieId());
                stmt.setString(7, cast.getCharName());
                stmt.setInt(8, cast.getCastId());
            } else {
                sql = "UPDATE cast SET cast_name = ?, birth_date = ?, gender = ?, biography = ?, movie_id = ?, char_name = ? WHERE cast_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, cast.getCastName());

                if (cast.getBirthDate() != null) {
                    stmt.setDate(2, new java.sql.Date(cast.getBirthDate().getTime()));
                } else {
                    stmt.setNull(2, java.sql.Types.DATE);
                }

                stmt.setString(3, cast.getGender());
                stmt.setString(4, cast.getBiography());
                stmt.setInt(5, cast.getMovie().getMovieId());
                stmt.setString(6, cast.getCharName());
                stmt.setInt(7, cast.getCastId());
            }

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating cast: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    @Override
    public boolean deleteCast(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM cast WHERE cast_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting cast: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    private Cast mapCast(ResultSet rs) throws SQLException {
        Cast cast = new Cast();
        cast.setCastId(rs.getInt("cast_id"));
        cast.setCastName(rs.getString("cast_name"));
        cast.setBirthDate(rs.getDate("birth_date"));
        cast.setGender(rs.getString("gender"));
        cast.setBiography(rs.getString("biography"));
        cast.setPhoto(rs.getBytes("photo"));
        cast.setCharName(rs.getString("char_name"));

        // Get the movie
        int movieId = rs.getInt("movie_id");
        Movie movie = movieDAO.getMovieById(movieId);
        cast.setMovie(movie);

        return cast;
    }
}