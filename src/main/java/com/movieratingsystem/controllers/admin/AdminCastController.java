package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.Cast;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.CastService;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminCastController", urlPatterns = {"/admin/cast"})
public class AdminCastController extends HttpServlet {

    private CastService castService;

    @Override
    public void init() throws ServletException {
        super.init();
        castService = new CastService();
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

        // Get cast members with pagination
        List<Cast> castList;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            castList = castService.searchCast(searchTerm, page, pageSize);
            request.setAttribute("totalPages", 
                (int) Math.ceil((double) castService.getTotalSearchResults(searchTerm) / pageSize));
        } else {
            castList = castService.getAllCast();
            request.setAttribute("totalPages", 
                (int) Math.ceil((double) castService.getTotalCast() / pageSize));
        }

        request.setAttribute("castList", castList);
        request.setAttribute("currentPage", page);

        // Forward to cast list page
        request.getRequestDispatcher("/WEB-INF/views/admin/cast.jsp").forward(request, response);
    }
} 