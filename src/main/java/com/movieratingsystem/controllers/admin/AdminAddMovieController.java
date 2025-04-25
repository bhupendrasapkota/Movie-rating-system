package com.movieratingsystem.controllers.admin;

import com.movieratingsystem.models.Genre;
import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.GenreService;
import com.movieratingsystem.service.MovieService;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet(name = "AdminAddMovieController", urlPatterns = {"/admin/movies/add"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 5,   // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class AdminAddMovieController extends HttpServlet {

    private MovieService movieService;
    private GenreService genreService;

    @Override
    public void init() throws ServletException {
        super.init();
        movieService = new MovieService();
        genreService = new GenreService();
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

        // Get all genres for the form
        List<Genre> genres = genreService.getAllGenres();
        request.setAttribute("genres", genres);

        // Forward to add movie page
        request.getRequestDispatcher("/WEB-INF/views/admin/add-movie.jsp").forward(request, response);
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
        String title = request.getParameter("title");
        String releaseDateStr = request.getParameter("releaseDate");
        String minutesStr = request.getParameter("minutes");
        String[] genreIds = request.getParameterValues("genres");

        // Validate input
        if (title == null || title.trim().isEmpty() ||
                releaseDateStr == null || releaseDateStr.trim().isEmpty() ||
                minutesStr == null || minutesStr.trim().isEmpty()) {

            request.setAttribute("error", "Title, release date, and duration are required");

            // Get all genres for the form
            List<Genre> genres = genreService.getAllGenres();
            request.setAttribute("genres", genres);

            request.getRequestDispatcher("/WEB-INF/views/admin/add-movie.jsp").forward(request, response);
            return;
        }

        try {
            // Parse release date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date releaseDate = dateFormat.parse(releaseDateStr);

            // Parse minutes
            int minutes = Integer.parseInt(minutesStr);

            // Create movie object
            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setReleaseDate(releaseDate);
            movie.setMinutes(minutes);

            // Handle poster upload
            Part filePart = request.getPart("poster");
            if (filePart != null && filePart.getSize() > 0) {
                InputStream fileContent = filePart.getInputStream();
                byte[] imageBytes = new byte[(int) filePart.getSize()];
                fileContent.read(imageBytes);
                movie.setImage(imageBytes);
            }

            // Parse genre IDs
            List<Integer> genreIdList = new ArrayList<>();
            if (genreIds != null) {
                for (String genreId : genreIds) {
                    try {
                        genreIdList.add(Integer.parseInt(genreId));
                    } catch (NumberFormatException e) {
                        // Ignore invalid genre IDs
                    }
                }
            }

            // Add movie
            boolean success = movieService.addMovie(movie, genreIdList);

            if (success) {
                // Redirect to admin movies page with success message
                response.sendRedirect(request.getContextPath() + "/admin/movies?success=Movie added successfully");
            } else {
                request.setAttribute("error", "Failed to add movie");

                // Get all genres for the form
                List<Genre> genres = genreService.getAllGenres();
                request.setAttribute("genres", genres);

                request.getRequestDispatcher("/WEB-INF/views/admin/add-movie.jsp").forward(request, response);
            }

        } catch (ParseException | NumberFormatException e) {
            request.setAttribute("error", "Invalid input format");

            // Get all genres for the form
            List<Genre> genres = genreService.getAllGenres();
            request.setAttribute("genres", genres);

            request.getRequestDispatcher("/WEB-INF/views/admin/add-movie.jsp").forward(request, response);
        }
    }
}