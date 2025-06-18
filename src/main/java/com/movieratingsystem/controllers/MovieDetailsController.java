package com.movieratingsystem.controllers;

import com.movieratingsystem.models.Cast;
import com.movieratingsystem.models.Genre;
import com.movieratingsystem.models.Movie;
import com.movieratingsystem.models.Review;
import com.movieratingsystem.models.UserModel;
import com.movieratingsystem.service.CastService;
import com.movieratingsystem.service.GenreService;
import com.movieratingsystem.service.MovieService;
import com.movieratingsystem.service.RatingService;
import com.movieratingsystem.service.ReviewService;
import com.movieratingsystem.service.WatchlistService;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "MovieDetailsController", urlPatterns = {"/movie"})
public class MovieDetailsController extends HttpServlet {

    private MovieService movieService;
    private GenreService genreService;
    private CastService castService;
    private ReviewService reviewService;
    private RatingService ratingService;
    private WatchlistService watchlistService;

    @Override
    public void init() throws ServletException {
        super.init();
        movieService = new MovieService();
        genreService = new GenreService();
        castService = new CastService();
        reviewService = new ReviewService();
        ratingService = new RatingService();
        watchlistService = new WatchlistService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/movies");
            return;
        }

        try {
            int movieId = Integer.parseInt(idParam);

            // Get movie details
            Movie movie = movieService.getMovieById(movieId);

            if (movie == null) {
                response.sendRedirect(request.getContextPath() + "/movies");
                return;
            }

            request.setAttribute("movie", movie);

            // Get genres for this movie
            List<Genre> genres = genreService.getGenresByMovieId(movieId);
            request.setAttribute("genres", genres);

            // Get cast for this movie
            List<Cast> cast = castService.getCastByMovieId(movieId);
            request.setAttribute("cast", cast);

            // Get reviews for this movie
            List<Review> reviews = reviewService.getReviewsByMovieId(movieId);
            request.setAttribute("reviews", reviews);

            // Get average rating for this movie
            Double averageRating = ratingService.getAverageRatingForMovie(movieId);
            request.setAttribute("averageRating", averageRating);

            // Check if movie is in user's watchlist
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                UserModel user = (UserModel) session.getAttribute("user");
                boolean inWatchlist = watchlistService.isMovieInWatchlist(user.getId(), movieId);
                request.setAttribute("inWatchlist", inWatchlist);
            }

            // Forward to movie details page
            request.getRequestDispatcher("/WEB-INF/views/movie-details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/movies");
        }
    }
}