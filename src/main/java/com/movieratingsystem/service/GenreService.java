package com.movieratingsystem.service;

import com.movieratingsystem.dao.GenreDAO;
import com.movieratingsystem.dao.impl.GenreDAOImpl;
import com.movieratingsystem.models.Genre;
import com.movieratingsystem.utils.ValidationUtil;
import java.util.List;

public class GenreService {

    private final GenreDAO genreDAO;

    public GenreService() {
        this.genreDAO = new GenreDAOImpl();
    }

    public Genre getGenreById(int id) {
        return genreDAO.getGenreById(id);
    }

    public List<Genre> getAllGenres() {
        return genreDAO.getAllGenres();
    }

    public List<Genre> getGenresByMovieId(int movieId) {
        return genreDAO.getGenresByMovieId(movieId);
    }

    public List<Genre> searchGenres(String searchTerm, int page, int pageSize) {
        return genreDAO.searchGenres(searchTerm, page, pageSize);
    }

    public int getTotalGenres() {
        return genreDAO.getTotalGenres();
    }

    public int getTotalSearchResults(String searchTerm) {
        return genreDAO.getTotalSearchResults(searchTerm);
    }

    public boolean addGenre(Genre genre) {
        // Validate genre data
        if (!ValidationUtil.isValidGenreName(genre.getGenreName())) {
            return false;
        }

        return genreDAO.addGenre(genre);
    }

    public boolean updateGenre(Genre genre) {
        // Validate genre data
        if (!ValidationUtil.isValidGenreName(genre.getGenreName())) {
            return false;
        }

        return genreDAO.updateGenre(genre);
    }

    public boolean deleteGenre(int id) {
        return genreDAO.deleteGenre(id);
    }
}