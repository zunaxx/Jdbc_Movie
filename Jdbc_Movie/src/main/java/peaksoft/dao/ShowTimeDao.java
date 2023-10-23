package peaksoft.dao;

import peaksoft.models.Movie;
import peaksoft.models.ShowTime;
import peaksoft.models.Theater;

import java.util.List;
import java.util.Map;

public interface ShowTimeDao {
    ShowTime saveShow(ShowTime showTime);

    ShowTime find(Long id);

    void assign(ShowTime showTime);
    List<Map<Theater, List<Movie>>> getMoviesGroupByTheatre();
}

