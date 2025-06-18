package com.movieratingsystem.controllers;

import com.movieratingsystem.service.UserService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@WebServlet(name = "UserImageController", urlPatterns = {"/user/image"})
public class UserImageController extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int userId = Integer.parseInt(idParam);
            byte[] imageData = userService.getUserImage(userId);

            if (imageData == null || imageData.length == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Set content type and headers
            response.setContentType("image/jpeg");
            response.setContentLength(imageData.length);

            // Write image data to response
            try (OutputStream out = response.getOutputStream()) {
                out.write(imageData);
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}