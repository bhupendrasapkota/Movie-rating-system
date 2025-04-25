package com.movieratingsystem.dao.impl;

import com.movieratingsystem.dao.UserDAO;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public UserModel getUserById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UserModel user = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM users WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = mapUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return user;
    }

    @Override
    public UserModel getUserByEmail(String email) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UserModel user = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM users WHERE email = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = mapUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return user;
    }

    @Override
    public List<UserModel> getAllUsers() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<UserModel> users = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM users ORDER BY name";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return users;
    }

    @Override
    public List<UserModel> searchUsers(String searchTerm, int page, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<UserModel> users = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM users WHERE name LIKE ? OR email LIKE ? ORDER BY name LIMIT ? OFFSET ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            stmt.setInt(3, pageSize);
            stmt.setInt(4, (page - 1) * pageSize);
            rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching users: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return users;
    }

    @Override
    public int getTotalUsers() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM users";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total users: " + e.getMessage());
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
            String sql = "SELECT COUNT(*) FROM users WHERE name LIKE ? OR email LIKE ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
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
    public boolean addUser(UserModel user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO users (name, email, password, image, role) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setBytes(4, user.getImage());
            stmt.setString(5, user.getRole().toString());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return success;
    }

    @Override
    public boolean updateUser(UserModel user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET name = ?, email = ?, password = ?, image = ?, role = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setBytes(4, user.getImage());
            stmt.setString(5, user.getRole().toString());
            stmt.setInt(6, user.getId());

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    @Override
    public boolean updateUserWithoutPassword(UserModel user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET name = ?, email = ?, role = ? WHERE id = ?";

            if (user.getImage() != null) {
                sql = "UPDATE users SET name = ?, email = ?, image = ?, role = ? WHERE id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getEmail());
                stmt.setBytes(3, user.getImage());
                stmt.setString(4, user.getRole().toString());
                stmt.setInt(5, user.getId());
            } else {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getRole().toString());
                stmt.setInt(4, user.getId());
            }

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user without password: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET password = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    @Override
    public boolean deleteUser(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM users WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }

        return success;
    }

    @Override
    public byte[] getUserImage(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        byte[] image = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT image FROM users WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                image = rs.getBytes("image");
            }
        } catch (SQLException e) {
            System.err.println("Error getting user image: " + e.getMessage());
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }

        return image;
    }

    private UserModel mapUser(ResultSet rs) throws SQLException {
        UserModel user = new UserModel();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setImage(rs.getBytes("image"));
        user.setRole(UserModel.Role.valueOf(rs.getString("role")));
        return user;
    }
}