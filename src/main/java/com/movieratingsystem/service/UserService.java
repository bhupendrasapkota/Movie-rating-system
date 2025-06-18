package com.movieratingsystem.service;

import com.movieratingsystem.dao.UserDAO;
import com.movieratingsystem.dao.impl.UserDAOImpl;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.utils.PasswordUtil;
import com.movieratingsystem.utils.ValidationUtil;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public UserModel getUserById(int id) {
        return userDAO.getUserById(id);
    }

    public UserModel getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public List<UserModel> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public List<UserModel> searchUsers(String searchTerm, int page, int pageSize) {
        return userDAO.searchUsers(searchTerm, page, pageSize);
    }

    public int getTotalUsers() {
        return userDAO.getTotalUsers();
    }

    public int getTotalSearchResults(String searchTerm) {
        return userDAO.getTotalSearchResults(searchTerm);
    }

    public boolean registerUser(UserModel user) {
        // Validate user data
        if (!ValidationUtil.isValidName(user.getName())) {
            return false;
        }

        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            return false;
        }

        if (!ValidationUtil.isValidPassword(user.getPassword())) {
            return false;
        }

        // Check if email already exists
        if (userDAO.getUserByEmail(user.getEmail()) != null) {
            return false;
        }

        // Hash the password
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(user.getPassword(), salt);
        user.setPassword(salt + ":" + hashedPassword);

        // Check if this is the first user
        int totalUsers = userDAO.getTotalUsers();
        if (totalUsers == 0) {
            // First user becomes admin
            user.setRole(UserModel.Role.admin);
        } else {
            // All other users are regular users
            user.setRole(UserModel.Role.user);
        }

        return userDAO.addUser(user);
    }

    public UserModel authenticateUser(String email, String password) {
        UserModel user = userDAO.getUserByEmail(email);

        if (user == null) {
            return null;
        }

        // Split stored password into salt and hash
        String[] parts = user.getPassword().split(":");
        if (parts.length != 2) {
            return null;
        }

        String salt = parts[0];
        String storedHash = parts[1];

        // Verify password
        if (PasswordUtil.verifyPassword(password, salt, storedHash)) {
            return user;
        }

        return null;
    }

    public boolean updateUser(UserModel user) {
        // Validate user data
        if (!ValidationUtil.isValidName(user.getName())) {
            return false;
        }

        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            return false;
        }

        // Check if email already exists for another user
        UserModel existingUser = userDAO.getUserByEmail(user.getEmail());
        if (existingUser != null && existingUser.getId() != user.getId()) {
            return false;
        }

        // Get the current user data to check if we need to update the password
        UserModel currentUser = userDAO.getUserById(user.getId());
        if (currentUser == null) {
            return false;
        }

        // If a new password is provided, hash it
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword(), salt);
            user.setPassword(salt + ":" + hashedPassword);
            return userDAO.updateUser(user); // Use full update including password
        } else {
            // If no new password, preserve the old password
            user.setPassword(currentUser.getPassword());
            return userDAO.updateUser(user); // Use full update but with old password
        }
    }

    public boolean updatePassword(int userId, String currentPassword, String newPassword) {
        UserModel user = userDAO.getUserById(userId);

        if (user == null) {
            return false;
        }

        // Validate new password
        if (!ValidationUtil.isValidPassword(newPassword)) {
            return false;
        }

        // Split stored password into salt and hash
        String[] parts = user.getPassword().split(":");
        if (parts.length != 2) {
            return false;
        }

        String salt = parts[0];
        String storedHash = parts[1];

        // Verify current password
        if (!PasswordUtil.verifyPassword(currentPassword, salt, storedHash)) {
            return false;
        }

        // Hash the new password
        String newSalt = PasswordUtil.generateSalt();
        String newHashedPassword = PasswordUtil.hashPassword(newPassword, newSalt);

        return userDAO.updatePassword(userId, newSalt + ":" + newHashedPassword);
    }

    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }

    public byte[] getUserImage(int userId) {
        return userDAO.getUserImage(userId);
    }
}