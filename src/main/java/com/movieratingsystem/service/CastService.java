package com.movieratingsystem.service;

import com.movieratingsystem.dao.CastDAO;
import com.movieratingsystem.dao.impl.CastDAOImpl;
import com.movieratingsystem.models.Cast;
import java.util.List;

public class CastService {

    private CastDAO castDAO;

    public CastService() {
        this.castDAO = new CastDAOImpl();
    }

    public Cast getCastById(int id) {
        return castDAO.getCastById(id);
    }

    public List<Cast> getAllCast() {
        return castDAO.getAllCast();
    }

    public List<Cast> getCastByMovieId(int movieId) {
        return castDAO.getCastByMovieId(movieId);
    }

    public List<Cast> searchCast(String searchTerm, int page, int pageSize) {
        return castDAO.searchCast(searchTerm, page, pageSize);
    }

    public int getTotalCast() {
        return castDAO.getTotalCast();
    }

    public int getTotalSearchResults(String searchTerm) {
        return castDAO.getTotalSearchResults(searchTerm);
    }

    public boolean addCast(Cast cast) {
        // Validate cast data
        if (cast.getCastName() == null || cast.getCastName().trim().isEmpty()) {
            return false;
        }

        if (cast.getMovie() == null || cast.getMovie().getMovieId() <= 0) {
            return false;
        }

        if (cast.getCharName() == null || cast.getCharName().trim().isEmpty()) {
            return false;
        }

        return castDAO.addCast(cast);
    }

    public boolean updateCast(Cast cast) {
        // Validate cast data
        if (cast.getCastName() == null || cast.getCastName().trim().isEmpty()) {
            return false;
        }

        if (cast.getMovie() == null || cast.getMovie().getMovieId() <= 0) {
            return false;
        }

        if (cast.getCharName() == null || cast.getCharName().trim().isEmpty()) {
            return false;
        }

        return castDAO.updateCast(cast);
    }

    public boolean deleteCast(int id) {
        return castDAO.deleteCast(id);
    }
}