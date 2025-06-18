package com.movieratingsystem.dao;

import com.movieratingsystem.models.Cast;
import java.util.List;

public interface CastDAO {
    Cast getCastById(int id);
    List<Cast> getAllCast();
    List<Cast> getCastByMovieId(int movieId);
    List<Cast> searchCast(String searchTerm, int page, int pageSize);
    int getTotalCast();
    int getTotalSearchResults(String searchTerm);
    boolean addCast(Cast cast);
    boolean updateCast(Cast cast);
    boolean deleteCast(int id);
}