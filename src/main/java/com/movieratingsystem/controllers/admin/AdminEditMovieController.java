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

@WebServlet(name = "AdminEditMovieController", urlPatterns = {"/admin/movies/edit"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 5,   // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class AdminEditMovieController extends HttpServlet {

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

        // Get movie ID
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/movies");
            return;
        }

        try {
            int movieId = Integer.parseInt(idParam);

            // Get movie
            Movie movie = movieService.getMovieById(movieId);

            if (movie == null) {
                response.sendRedirect(request.getContextPath() + "/admin/movies");
                return;
            }

            request.setAttribute("movie", movie);

            // Get all genres
            List<Genre> allGenres = genreService.getAllGenres();
            request.setAttribute("allGenres", allGenres);

            // Get movie's genre IDs
            List<Integer> movieGenreIds = movieService.getGenreIdsByMovieId(movieId);
            request.setAttribute("movieGenreIds", movieGenreIds);

            // Forward to edit movie page
            request.getRequestDispatcher("/WEB-INF/views/admin/edit-movie.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/movies");
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

        // Get form data
        String movieIdParam = request.getParameter("movieId");
        String title = request.getParameter("title");
        String releaseDateStr = request.getParameter("releaseDate");
        String minutesStr = request.getParameter("minutes");
        String[] genreIds = request.getParameterValues("genres");

        // Validate input
        if (movieIdParam == null || movieIdParam.trim().isEmpty() ||
                title == null || title.trim().isEmpty() ||
                releaseDateStr == null || releaseDateStr.trim().isEmpty() ||
                minutesStr == null || minutesStr.trim().isEmpty()) {

            response.sendRedirect(request.getContextPath() + "/admin/movies");
            return;
        }

        try {
            int movieId = Integer.parseInt(movieIdParam);

            // Get existing movie
            Movie movie = movieService.getMovieById(movieId);

            if (movie == null) {
                response.sendRedirect(request.getContextPath() + "/admin/movies");
                return;
            }

            // Parse release date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date releaseDate = dateFormat.parse(releaseDateStr);

            // Parse minutes
            int minutes = Integer.parseInt(minutesStr);

            // Update movie object
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

            // Update movie
            boolean success = movieService.updateMovie(movie, genreIdList);

            if (success) {
                // Redirect to admin movies page with success message
                response.sendRedirect(request.getContextPath() + "/admin/movies?success=Movie updated successfully");
            } else {
                request.setAttribute("error", "Failed to update movie");

                // Get all genres
                List<Genre> allGenres = genreService.getAllGenres();
                request.setAttribute("allGenres", allGenres);

                // Get movie's genre IDs
                List<Integer> movieGenreIds = movieService.getGenreIdsByMovieId(movieId);
                request.setAttribute("movieGenreIds", movieGenreIds);

                request.setAttribute("movie", movie);

                request.getRequestDispatcher("/WEB-INF/views/admin/edit-movie.jsp").forward(request, response);
            }

        } catch (ParseException | NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/movies");
        }
    }
}