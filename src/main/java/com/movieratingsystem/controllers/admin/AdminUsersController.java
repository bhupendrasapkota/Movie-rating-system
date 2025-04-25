package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminUsersController", urlPatterns = {"/admin/users"})
public class AdminUsersController extends HttpServlet {

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

        // Get search parameters
        String searchTerm = request.getParameter("search");
        int page = 1;
        int pageSize = 10;

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException e) {
            // Use default page value
        }

        // Get users with pagination
        List<UserModel> users;
        int totalUsers;
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            users = userService.searchUsers(searchTerm, page, pageSize);
            totalUsers = userService.getTotalSearchResults(searchTerm);
        } else {
            List<UserModel> allUsers = userService.getAllUsers();
            totalUsers = allUsers.size();
            
            // Apply pagination manually
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, allUsers.size());
            
            if (fromIndex < allUsers.size()) {
                users = allUsers.subList(fromIndex, toIndex);
            } else {
                users = List.of(); // Empty list if page is out of bounds
            }
        }

        request.setAttribute("users", users);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", (int) Math.ceil((double) totalUsers / pageSize));

        // Forward to users page
        request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
    }
} 