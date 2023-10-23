package peaksoft.dao;

import peaksoft.models.Movie;

import java.util.List;

public interface MovieDao {
    void createTable(String table, List<String> strings);

    void saveMovies(Movie movie);

    Movie findById(Long id);
}
