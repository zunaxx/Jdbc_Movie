package peaksoft.models;

import java.time.LocalDateTime;

public class ShowTime {

    private Long id;
    private Long movie_id;
    private Long theatre_id;
    private LocalDateTime start_time;
    private LocalDateTime end_time;

    public ShowTime() {
    }

    public ShowTime(Long movie_id, Long theatre_id, LocalDateTime start_time, LocalDateTime end_time) {
        this.movie_id = movie_id;
        this.theatre_id = theatre_id;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Long movie_id) {
        this.movie_id = movie_id;
    }

    public Long getTheatre_id() {
        return theatre_id;
    }

    public void setTheatre_id(Long theatre_id) {
        this.theatre_id = theatre_id;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "ShowTime{" +
                "id=" + id +
                ", movie_id=" + movie_id +
                ", theatre_id=" + theatre_id +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                '}';
    }
}
