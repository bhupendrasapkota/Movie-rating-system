package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.Cast;
import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.CastService;
import com.movieratingsystem.service.MovieService;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet(name = "AdminAddCastController", urlPatterns = {"/admin/cast/add"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 5,   // 5 MB
    maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class AdminAddCastController extends HttpServlet {

    private CastService castService;
    private MovieService movieService;

    @Override
    public void init() throws ServletException {
        super.init();
        castService = new CastService();
        movieService = new MovieService();
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

        // Get all movies for the form
        List<Movie> movies = movieService.getAllMovies();
        request.setAttribute("movies", movies);

        // Forward to add cast page
        request.getRequestDispatcher("/WEB-INF/views/admin/add-cast.jsp").forward(request, response);
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

        // Get form data
        String castName = request.getParameter("castName");
        String birthDateStr = request.getParameter("birthDate");
        String gender = request.getParameter("gender");
        String biography = request.getParameter("biography");
        String movieIdStr = request.getParameter("movieId");
        String charName = request.getParameter("charName");

        // Validate input
        if (castName == null || castName.trim().isEmpty() ||
            movieIdStr == null || movieIdStr.trim().isEmpty() ||
            charName == null || charName.trim().isEmpty()) {
            
            request.setAttribute("error", "Cast name, movie, and character name are required");
            List<Movie> movies = movieService.getAllMovies();
            request.setAttribute("movies", movies);
            request.getRequestDispatcher("/WEB-INF/views/admin/add-cast.jsp").forward(request, response);
            return;
        }

        try {
            // Create cast object
            Cast cast = new Cast();
            cast.setCastName(castName.trim());
            
            if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                cast.setBirthDate(dateFormat.parse(birthDateStr));
            }
            
            cast.setGender(gender);
            cast.setBiography(biography != null ? biography.trim() : null);
            cast.setCharName(charName.trim());

            // Set movie
            int movieId = Integer.parseInt(movieIdStr);
            Movie movie = movieService.getMovieById(movieId);
            if (movie == null) {
                throw new ServletException("Invalid movie ID");
            }
            cast.setMovie(movie);

            // Handle photo upload
            Part filePart = request.getPart("photo");
            if (filePart != null && filePart.getSize() > 0) {
                InputStream fileContent = filePart.getInputStream();
                byte[] photoBytes = new byte[(int) filePart.getSize()];
                fileContent.read(photoBytes);
                cast.setPhoto(photoBytes);
            }

            // Add cast
            boolean success = castService.addCast(cast);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/cast?success=Cast member added successfully");
            } else {
                request.setAttribute("error", "Failed to add cast member");
                List<Movie> movies = movieService.getAllMovies();
                request.setAttribute("movies", movies);
                request.getRequestDispatcher("/WEB-INF/views/admin/add-cast.jsp").forward(request, response);
            }

        } catch (ParseException | NumberFormatException e) {
            request.setAttribute("error", "Invalid input format");
            List<Movie> movies = movieService.getAllMovies();
            request.setAttribute("movies", movies);
            request.getRequestDispatcher("/WEB-INF/views/admin/add-cast.jsp").forward(request, response);
        }
    }
} 