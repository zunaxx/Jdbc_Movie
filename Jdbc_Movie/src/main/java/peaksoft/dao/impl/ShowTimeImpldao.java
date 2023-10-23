package peaksoft.dao.impl;

import peaksoft.config.JdbcConfig;
import peaksoft.dao.ShowTimeDao;
import peaksoft.models.Movie;
import peaksoft.models.ShowTime;
import peaksoft.models.Theater;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowTimeImpldao implements ShowTimeDao {
    private  final Connection connection = JdbcConfig.getConnection();
    @Override
    public ShowTime saveShow(ShowTime showTime) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("""
    insert into show_time(movie_id,theatre_id,start_time,end_time)
    values (?,?,?,?)
    """);
            preparedStatement.setLong(1,showTime.getMovie_id());
            preparedStatement.setLong(2,showTime.getTheatre_id());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(showTime.getStart_time()));
            preparedStatement.setTimestamp(4,Timestamp.valueOf(showTime.getStart_time()));
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return getShowTimeFindStartAndEnd(showTime.getStart_time(),showTime.getEnd_time());
    }

    @Override
    public ShowTime find(Long id) {
        ShowTime showTime = new ShowTime();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("""
    select * from show_time where  id = ?
    """);
            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                throw  new RuntimeException("not found");
            }else {
                showTime.setId(resultSet.getLong("id"));
                showTime.setMovie_id(resultSet.getLong("movie_id"));
                showTime.setTheatre_id(resultSet.getLong("theatre_id"));
                showTime.setStart_time(resultSet.getTimestamp("start_time").toLocalDateTime());
                showTime.setEnd_time(resultSet.getTimestamp("end_time").toLocalDateTime());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return showTime;
    }

    @Override
    public void assign(ShowTime showTime) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("""
update show_time set
movie_id = ?,
theatre_id = ?
where id = ?
""");
            preparedStatement.setLong(1,showTime.getMovie_id());
            preparedStatement.setLong(2,showTime.getTheatre_id());
            preparedStatement.setLong(3,showTime.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Map<Theater, List<Movie>>> getMoviesGroupByTheatre() {
        List<Map<Theater, List<Movie>>> result = new ArrayList<>();

        try {
            String sql = """
            SELECT t.id AS theater_id, t.name AS theater_name, t.location AS theater_location,
                   m.id AS movie_id, m.title AS movie_title, m.genre AS movie_genre, m.duration AS movie_duration
            FROM show_time st
            JOIN theater t ON st.theatre_id = t.id
            JOIN movie m ON st.movie_id = m.id
            """;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            Map<Theater, List<Movie>> theaterMovieMap = new HashMap<>();

            while (resultSet.next()) {
                Long theaterId = resultSet.getLong("theater_id");
                String theaterName = resultSet.getString("theater_name");
                String theaterLocation = resultSet.getString("theater_location");
                Long movieId = resultSet.getLong("movie_id");
                String movieTitle = resultSet.getString("movie_title");
                String movieGenre = resultSet.getString("movie_genre");
                int movieDuration = resultSet.getInt("movie_duration");

                Theater theater = new Theater(theaterId, theaterName, theaterLocation);
                Movie movie = new Movie();

                theaterMovieMap.computeIfAbsent(theater, k -> new ArrayList<>()).add(movie);
            }


            for (Theater theater : theaterMovieMap.keySet()) {
                Map<Theater, List<Movie>> entry = new HashMap<>();
                entry.put(theater, theaterMovieMap.get(theater));
                result.add(entry);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении SQL-запроса", e);
        }

        return result;
    }

    public ShowTime getShowTimeFindStartAndEnd(LocalDateTime start,LocalDateTime end){
        ShowTime showTime = new ShowTime();
        PreparedStatement preparedStatement = null;
        try {
           preparedStatement = connection.prepareStatement("""
    select * from show_time where start_time =? and end_time = ?
    
    """);
            preparedStatement.setTimestamp(1,Timestamp.valueOf(start));
            preparedStatement.setTimestamp(2,Timestamp.valueOf(end));
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                showTime.setId(resultSet.getLong("id"));
            }else{
                throw  new RuntimeException("not found showTime");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
return showTime;
    }
}
