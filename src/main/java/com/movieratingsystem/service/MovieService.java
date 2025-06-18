package com.movieratingsystem.service;

import com.movieratingsystem.dao.GenreDAO;
import com.movieratingsystem.dao.MovieDAO;
import com.movieratingsystem.dao.impl.GenreDAOImpl;
import com.movieratingsystem.dao.impl.MovieDAOImpl;
import com.movieratingsystem.models.Genre;
import com.movieratingsystem.models.Movie;
import com.movieratingsystem.utils.ValidationUtil;
import java.util.List;

public class MovieService {

    private final MovieDAO movieDAO;
    private final GenreDAO genreDAO;

    public MovieService() {
        this.movieDAO = new MovieDAOImpl();
        this.genreDAO = new GenreDAOImpl();
    }

    public Movie getMovieById(int id) {
        return movieDAO.getMovieById(id);
    }

    public List<Movie> getAllMovies() {
        return movieDAO.getAllMovies();
    }

    public List<Movie> getFeaturedMovies(int limit) {
        return movieDAO.getFeaturedMovies(limit);
    }

    public List<Movie> searchMovies(String searchTerm, List<Integer> genreIds, Integer yearFrom, Integer yearTo, int page, int pageSize) {
        return movieDAO.searchMovies(searchTerm, genreIds, yearFrom, yearTo, page, pageSize);
    }

    public int getTotalMovies() {
        return movieDAO.getTotalMovies();
    }

    public int getTotalSearchResults(String searchTerm, List<Integer> genreIds, Integer yearFrom, Integer yearTo) {
        return movieDAO.getTotalSearchResults(searchTerm, genreIds, yearFrom, yearTo);
    }

    public boolean addMovie(Movie movie, List<Integer> genreIds) {
        // Validate movie data
        if (!ValidationUtil.isValidMovieTitle(movie.getTitle())) {
            return false;
        }

        if (!ValidationUtil.isValidDuration(movie.getMinutes())) {
            return false;
        }

        boolean success = movieDAO.addMovie(movie);

        if (success && genreIds != null && !genreIds.isEmpty()) {
            for (Integer genreId : genreIds) {
                movieDAO.addMovieGenre(movie.getMovieId(), genreId);
            }
        }

        return success;
    }

    public boolean updateMovie(Movie movie, List<Integer> genreIds) {
        // Validate movie data
        if (!ValidationUtil.isValidMovieTitle(movie.getTitle())) {
            return false;
        }

        if (!ValidationUtil.isValidDuration(movie.getMinutes())) {
            return false;
        }

        boolean success = movieDAO.updateMovie(movie);

        if (success && genreIds != null) {
            // Remove all existing genres for this movie
            movieDAO.removeAllMovieGenres(movie.getMovieId());

            // Add new genres
            for (Integer genreId : genreIds) {
                movieDAO.addMovieGenre(movie.getMovieId(), genreId);
            }
        }

        return success;
    }

    public boolean deleteMovie(int id) {
        return movieDAO.deleteMovie(id);
    }

    public List<Genre> getGenresByMovieId(int movieId) {
        return genreDAO.getGenresByMovieId(movieId);
    }

    public List<Integer> getGenreIdsByMovieId(int movieId) {
        return genreDAO.getGenreIdsByMovieId(movieId);
    }
}