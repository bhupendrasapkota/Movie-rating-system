package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.UserService;
import com.movieratingsystem.utils.ValidationUtil;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.InputStream;

@WebServlet(name = "AdminEditUserController", urlPatterns = {"/admin/users/edit"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,  // 1 MB
    maxFileSize = 1024 * 1024 * 5,    // 5 MB
    maxRequestSize = 1024 * 1024 * 10  // 10 MB
)
public class AdminEditUserController extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserModel user = (UserModel) session.getAttribute("user");
        if (user.getRole() != UserModel.Role.admin) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Get user ID
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        try {
            int userId = Integer.parseInt(idParam);
            UserModel editUser = userService.getUserById(userId);

            if (editUser == null) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }

            request.setAttribute("editUser", editUser);
            request.getRequestDispatcher("/WEB-INF/views/admin/edit-user.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserModel user = (UserModel) session.getAttribute("user");
        if (user.getRole() != UserModel.Role.admin) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Get form parameters
        String idParam = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (idParam == null || name == null || email == null || role == null ||
            idParam.trim().isEmpty() || name.trim().isEmpty() || email.trim().isEmpty() || role.trim().isEmpty()) {
            request.setAttribute("error", "Name, email and role are required");
            doGet(request, response);
            return;
        }

        try {
            int userId = Integer.parseInt(idParam);
            UserModel editUser = userService.getUserById(userId);

            if (editUser == null) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }

            // Update user data
            editUser.setName(name.trim());
            editUser.setEmail(email.trim());
            editUser.setRole(UserModel.Role.valueOf(role.trim()));

            // Handle password update if provided
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                if (!newPassword.equals(confirmPassword)) {
                    request.setAttribute("error", "Passwords do not match");
                    request.setAttribute("editUser", editUser);
                    request.getRequestDispatcher("/WEB-INF/views/admin/edit-user.jsp").forward(request, response);
                    return;
                }
                if (!ValidationUtil.isValidPassword(newPassword)) {
                    request.setAttribute("error", "Password must be at least 8 characters long");
                    request.setAttribute("editUser", editUser);
                    request.getRequestDispatcher("/WEB-INF/views/admin/edit-user.jsp").forward(request, response);
                    return;
                }
                editUser.setPassword(newPassword);
            }

            // Handle profile image upload if provided
            Part filePart = request.getPart("profileImage");
            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream inputStream = filePart.getInputStream()) {
                    byte[] imageBytes = inputStream.readAllBytes();
                    editUser.setImage(imageBytes);
                }
            }

            if (userService.updateUser(editUser)) {
                response.sendRedirect(request.getContextPath() + "/admin/users?success=User updated successfully");
            } else {
                request.setAttribute("error", "Failed to update user");
                request.setAttribute("editUser", editUser);
                request.getRequestDispatcher("/WEB-INF/views/admin/edit-user.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid role value");
            doGet(request, response);
        }
    }
} 