package com.movieratingsystem.dao;

import com.movieratingsystem.models.UserModel;
import java.util.List;

public interface UserDAO {

    UserModel getUserById(int id);
    UserModel getUserByEmail(String email);
    List<UserModel> getAllUsers();
    List<UserModel> searchUsers(String searchTerm, int page, int pageSize);
    int getTotalUsers();
    int getTotalSearchResults(String searchTerm);
    boolean addUser(UserModel user);
    boolean updateUser(UserModel user);
    boolean updateUserWithoutPassword(UserModel user);
    boolean updatePassword(int userId, String newPassword);
    boolean deleteUser(int id);
    byte[] getUserImage(int userId);

}