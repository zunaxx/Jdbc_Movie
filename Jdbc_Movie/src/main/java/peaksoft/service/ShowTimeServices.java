package peaksoft.service;

import peaksoft.models.Movie;
import peaksoft.models.ShowTime;
import peaksoft.models.Theater;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ShowTimeServices {
    ShowTime saveShow(ShowTime showTime);
    ShowTime findById(Long id);

    String assign(Long showTime_id, Long movie_id);


    String assign(Long showTimeId, Long movieId, Long theaterId);

    List<ShowTime> getAllShowTimes();


    String deleteShowTimeByStartAndEndTime(LocalDate startTime, LocalDate endTime);

    List<Map<Theater, List<Movie>>> getMoviesGroupByTheater();
}
