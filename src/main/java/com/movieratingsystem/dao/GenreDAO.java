package com.movieratingsystem.dao;

import com.movieratingsystem.models.Genre;
import java.util.List;

public interface GenreDAO {
    Genre getGenreById(int id);
    List<Genre> getAllGenres();
    List<Genre> getGenresByMovieId(int movieId);
    List<Genre> searchGenres(String searchTerm, int page, int pageSize);
    int getTotalGenres();
    int getTotalSearchResults(String searchTerm);
    boolean addGenre(Genre genre);
    boolean updateGenre(Genre genre);
    boolean deleteGenre(int id);
    List<Integer> getGenreIdsByMovieId(int movieId);
}