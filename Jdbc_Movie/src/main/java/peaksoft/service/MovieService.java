package peaksoft.service;

import peaksoft.models.Movie;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import peaksoft.models.Genre;




public interface MovieService {
    String createMovie(String tableName, List<String> columns);

    String saveMovie(Movie movie);

    Movie findById(Long id);

    List<Movie> searchByName(String title);

    Map<Genre, List<Movie>> getMoviesByGenre(String genre);

    List<Movie> sortByDuration(String ascOrDesc);

    List<Movie> getMoviesByTheaterIdAndStartTime(Long theaterId, LocalDate startTime);

    String updateMovie(Long id, Movie updatedMovie);

    String deleteMovie(Long id);
}
