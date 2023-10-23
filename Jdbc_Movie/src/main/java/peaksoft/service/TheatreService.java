package peaksoft.service;

import peaksoft.models.Movie;
import peaksoft.models.Theater;

import java.util.List;
import java.util.Map;

public interface TheatreService {

    Theater createTheater(Theater theater);

    Theater updateTheater(Long id, Theater updatedTheater);

    void deleteTheater(Long id);

    Theater findById(Long id);

    List<Theater> getAllTheaters();

    java.util.List<Map<Movie, java.util.List<Theater>>> getAllMoviesByTime(int hour);
}
