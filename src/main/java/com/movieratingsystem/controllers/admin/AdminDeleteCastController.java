package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.CastService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminDeleteCastController", urlPatterns = {"/admin/cast/delete"})
public class AdminDeleteCastController extends HttpServlet {

    private CastService castService;

    @Override
    public void init() throws ServletException {
        super.init();
        castService = new CastService();
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

        // Get cast ID
        String castIdParam = request.getParameter("castId");
        if (castIdParam == null || castIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/cast");
            return;
        }

        try {
            int castId = Integer.parseInt(castIdParam);

            // Delete cast
            boolean success = castService.deleteCast(castId);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/cast?success=Cast member deleted successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/cast?error=Failed to delete cast member");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/cast");
        }
    }
} 