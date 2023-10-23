package peaksoft.dao;

import peaksoft.models.Movie;
import peaksoft.models.Theater;

import java.util.List;
import java.util.Map;

public interface TheatreDao {

   Theater saveTheater(Theater theatre);

    Theater updateTheater(Theater theatre);

    void deleteTheater(Long id);

    Theater findById(Long id);

    List<Theater> getAllTheaters();

    List<Map<Movie, List<Theater>>> getMoviesByDuration(int duration);

    Theater createTheater(Theater theater);
}
