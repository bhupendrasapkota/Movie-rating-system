package com.movieratingsystem.controllers;

import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.UserService;
import java.io.IOException;
import java.io.InputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet(name = "EditProfileController", urlPatterns = {"/profile/edit"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 5,   // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class EditProfileController extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Forward to edit profile page
        request.getRequestDispatcher("/WEB-INF/views/edit-profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserModel user = (UserModel) session.getAttribute("user");

        // Get form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate input
        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Name and email are required");
            request.getRequestDispatcher("/WEB-INF/views/edit-profile.jsp").forward(request, response);
            return;
        }

        // Update user object
        UserModel updatedUser = new UserModel();
        updatedUser.setId(user.getId());
        updatedUser.setName(name);
        updatedUser.setEmail(email);
        updatedUser.setRole(user.getRole());

        // Handle profile image upload
        Part filePart = request.getPart("profileImage");
        if (filePart != null && filePart.getSize() > 0) {
            InputStream fileContent = filePart.getInputStream();
            byte[] imageBytes = new byte[(int) filePart.getSize()];
            fileContent.read(imageBytes);
            updatedUser.setImage(imageBytes);
        } else {
            updatedUser.setImage(user.getImage());
        }

        // Update user profile
        boolean success = userService.updateUser(updatedUser);

        if (!success) {
            request.setAttribute("error", "Failed to update profile. Email may already be in use.");
            request.getRequestDispatcher("/WEB-INF/views/edit-profile.jsp").forward(request, response);
            return;
        }

        // Update password if provided
        if (currentPassword != null && !currentPassword.trim().isEmpty() &&
                newPassword != null && !newPassword.trim().isEmpty() &&
                confirmPassword != null && !confirmPassword.trim().isEmpty()) {

            // Check if passwords match
            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("error", "New passwords do not match");
                request.getRequestDispatcher("/WEB-INF/views/edit-profile.jsp").forward(request, response);
                return;
            }

            // Update password
            boolean passwordSuccess = userService.updatePassword(user.getId(), currentPassword, newPassword);

            if (!passwordSuccess) {
                request.setAttribute("error", "Failed to update password. Current password may be incorrect.");
                request.getRequestDispatcher("/WEB-INF/views/edit-profile.jsp").forward(request, response);
                return;
            }
        }

        // Update session with new user data
        UserModel refreshedUser = userService.getUserById(user.getId());
        session.setAttribute("user", refreshedUser);

        // Set success message and redirect to profile page
        request.setAttribute("success", "Profile updated successfully");
        request.getRequestDispatcher("/WEB-INF/views/edit-profile.jsp").forward(request, response);
    }
}