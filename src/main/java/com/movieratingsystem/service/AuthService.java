package com.movieratingsystem.service;

import com.movieratingsystem.dao.UserDAO;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.dao.impl.UserDAOImpl;

public class AuthService {
    private static final UserDAO userDAO = new UserDAOImpl();

    public static int register(String name, String email, String password, String role, byte[] image){
        // Create a new UserModel object
        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);// FIXED: Password now set
        user.setRole(UserModel.Role.valueOf(role));
        user.setImage(image);

        // Register the user and return the generated ID
        return userDAO.addUser(user) ? user.getId() : -1;
    }

    public static UserModel login(String email, String password){
        // Create a UserModel with the provided credentials
        UserModel user = new UserModel();
        user.setEmail(email);
        user.setPassword(password);

        // Authenticate the user and return the result
        return userDAO.getUserByEmail(email); // No session created here
    }

    public static UserModel getUserById(int id) {
        return userDAO.getUserById(id);
    }
}


