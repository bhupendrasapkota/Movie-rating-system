package com.movieratingsystem.dao;

import com.movieratingsystem.models.Movie;
import java.util.List;

public interface MovieDAO {
    Movie getMovieById(int id);
    List<Movie> getAllMovies();
    List<Movie> getFeaturedMovies(int limit);
    List<Movie> searchMovies(String searchTerm, List<Integer> genreIds, Integer yearFrom, Integer yearTo, int page, int pageSize);
    int getTotalMovies();
    int getTotalSearchResults(String searchTerm, List<Integer> genreIds, Integer yearFrom, Integer yearTo);
    boolean addMovie(Movie movie);
    boolean updateMovie(Movie movie);
    boolean deleteMovie(int id);
    boolean addMovieGenre(int movieId, int genreId);
    boolean removeAllMovieGenres(int movieId);
}