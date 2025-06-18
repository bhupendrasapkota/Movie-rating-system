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

        // Validate input
        if (castName == null || castName.trim().isEmpty()) {
            request.setAttribute("error", "Cast name is required");
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

            // Handle photo upload
            Part filePart = request.getPart("photo");
            if (filePart != null && filePart.getSize() > 0) {
                InputStream fileContent = filePart.getInputStream();
                byte[] photoBytes = new byte[(int) filePart.getSize()];
                fileContent.read(photoBytes);
                cast.setPhoto(photoBytes);
            }

            // Get all movie roles
            boolean hasValidRole = false;
            String[] movieIds = request.getParameterValues("movieId[]");
            String[] charNames = request.getParameterValues("charName[]");
            
            if (movieIds != null && charNames != null && movieIds.length == charNames.length) {
                for (int i = 0; i < movieIds.length; i++) {
                    String movieIdStr = movieIds[i];
                    String charName = charNames[i];
                    
                    if (!movieIdStr.trim().isEmpty() && !charName.trim().isEmpty()) {
                        hasValidRole = true;
                        int movieId = Integer.parseInt(movieIdStr);
                        Movie movie = movieService.getMovieById(movieId);
                        if (movie == null) {
                            throw new ServletException("Invalid movie ID");
                        }
                        
                        // Create a new cast object for each role
                        Cast castRole = new Cast();
                        castRole.setCastName(cast.getCastName());
                        castRole.setBirthDate(cast.getBirthDate());
                        castRole.setGender(cast.getGender());
                        castRole.setBiography(cast.getBiography());
                        castRole.setPhoto(cast.getPhoto());
                        castRole.setMovie(movie);
                        castRole.setCharName(charName.trim());
                        
                        if (!castService.addCast(castRole)) {
                            throw new ServletException("Failed to add cast member for movie: " + movie.getTitle());
                        }
                    }
                }
            }

            if (!hasValidRole) {
                request.setAttribute("error", "At least one movie role is required");
                List<Movie> movies = movieService.getAllMovies();
                request.setAttribute("movies", movies);
                request.getRequestDispatcher("/WEB-INF/views/admin/add-cast.jsp").forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/admin/cast?success=Cast member added successfully");

        } catch (ParseException | NumberFormatException e) {
            request.setAttribute("error", "Invalid input format");
            List<Movie> movies = movieService.getAllMovies();
            request.setAttribute("movies", movies);
            request.getRequestDispatcher("/WEB-INF/views/admin/add-cast.jsp").forward(request, response);
        } catch (ServletException e) {
            request.setAttribute("error", e.getMessage());
            List<Movie> movies = movieService.getAllMovies();
            request.setAttribute("movies", movies);
            request.getRequestDispatcher("/WEB-INF/views/admin/add-cast.jsp").forward(request, response);
        }
    }
} 