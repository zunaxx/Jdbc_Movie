package peaksoft.dao.impl;

import peaksoft.config.JdbcConfig;
import peaksoft.dao.TheatreDao;
import peaksoft.models.Movie;
import peaksoft.models.Theater;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TheatreDaoImpl implements TheatreDao {
    private final Connection connection = JdbcConfig.getConnection();

    @Override
    public Theater saveTheater(Theater theatre) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into theatre (name, location) values (?, ?) RETURNING id")) {
            preparedStatement.setString(1, theatre.getName());
            preparedStatement.setString(2, theatre.getLocation());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                theatre.setId(id);
                return theatre;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Theater updateTheater(Theater theatre) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update theatre set name = ?, location = ? where id = ?")) {
            preparedStatement.setString(1, theatre.getName());
            preparedStatement.setString(2, theatre.getLocation());
            preparedStatement.setLong(3, theatre.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                return theatre;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteTheater(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from theatre where id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Theater findById(Long theatreId) {
        Theater theatre = new Theater();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("""
    select * from theatre where id =?
    """);
            preparedStatement.setLong(1,theatreId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                throw new RuntimeException("not fpund");
            }
            else{
                theatre.setId(resultSet.getLong("id"));
                theatre.setName(resultSet.getString("name"));
                theatre.setLocation(resultSet.getString("location"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return theatre;
    }

    @Override
    public List<Theater> getAllTheaters() {
        List<Theater> theaters = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from theatre")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Theater theatre = new Theater();
                theatre.setId(resultSet.getLong("id"));
                theatre.setName(resultSet.getString("name"));
                theatre.setLocation(resultSet.getString("location"));
                theaters.add(theatre);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return theaters;
    }

    @Override
    public List<Map<Movie, List<Theater>>> getMoviesByDuration(int duration) {
        List<Map<Movie, List<Theater>>> result = new ArrayList<>();
        String query = "SELECT m.*, t.* " +
                "FROM movies m " +
                "INNER JOIN showtimes s ON m.id = s.movie_id " +
                "INNER JOIN theaters t ON s.theatre_id = t.id " +
                "WHERE m.duration < ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, duration);
            ResultSet resultSet = preparedStatement.executeQuery();

            Map<Movie, List<Theater>> movieTheaterMap = new HashMap<>();
            while (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getLong("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setGenre(resultSet.getString("genre"));
                movie.setDuration(resultSet.getInt("duration"));

                Theater theater = new Theater();
                theater.setId(resultSet.getLong("theater_id"));
                theater.setName(resultSet.getString("theater_name"));
                theater.setLocation(resultSet.getString("location"));

                if (movieTheaterMap.containsKey(movie)) {
                    movieTheaterMap.get(movie).add(theater);
                } else {
                    List<Theater> theaters = new ArrayList<>();
                    theaters.add(theater);
                    movieTheaterMap.put(movie, theaters);
                }
            }
            result.add(movieTheaterMap);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;

    }

    @Override
    public Theater createTheater(Theater theater) {
        String query = "INSERT INTO theaters (name, location) VALUES (?, ?) RETURNING id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, theater.getName());
            preparedStatement.setString(2, theater.getLocation());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating theater failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    theater.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating theater failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return theater;
    }


}
