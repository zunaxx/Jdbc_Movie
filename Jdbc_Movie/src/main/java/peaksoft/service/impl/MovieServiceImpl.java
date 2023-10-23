package peaksoft.service.impl;

import peaksoft.config.JdbcConfig;
import peaksoft.models.Genre;
import peaksoft.models.Movie;
import peaksoft.service.MovieService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieServiceImpl implements MovieService {

     Connection connection;

    public MovieServiceImpl() {
        connection = JdbcConfig.getConnection();
    }


    @Override
    public String createMovie(String tableName, List<String> columns) {
        try (Statement statement = connection.createStatement()) {
            StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS movies");
            createTableSQL.append(tableName).append(" (");
            for (int i = 0; i < columns.size(); i++) {
                createTableSQL.append(columns.get(i));
                if (i < columns.size() - 1) {
                    createTableSQL.append(", ");
                }
            }
            createTableSQL.append(")");
            statement.execute(createTableSQL.toString());
            return "Table created: " + tableName;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String saveMovie(Movie movie) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO movies (title, genre, duration) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getGenre());
            preparedStatement.setInt(3, movie.getDuration());
            preparedStatement.executeUpdate();
            return "Movie saved successfully";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Movie findById(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM movies WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getLong("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setGenre(String.valueOf(Genre.valueOf(resultSet.getString("genre"))));
                movie.setDuration(resultSet.getInt("duration"));
                return movie;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Movie> searchByName(String title) {
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM movies WHERE title = ?")) {
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getLong("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setGenre(String.valueOf(Genre.valueOf(resultSet.getString("genre"))));
                movie.setDuration(resultSet.getInt("duration"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }

    @Override
    public Map<Genre, List<Movie>> getMoviesByGenre(String genre) {
        Map<Genre, List<Movie>> genreMap = new HashMap<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM movies WHERE genre = ?")) {
            preparedStatement.setString(1, genre);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getLong("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setGenre(String.valueOf(Genre.valueOf(resultSet.getString("genre"))));
                movie.setDuration(resultSet.getInt("duration"));
                if (!genreMap.containsKey(Genre.valueOf(genre))) {
                    genreMap.put(Genre.valueOf(genre), new ArrayList<>());
                }
                genreMap.get(Genre.valueOf(genre)).add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return genreMap;
    }

    @Override
    public List<Movie> sortByDuration(String ascOrDesc) {
        List<Movie> movies = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String order = "ASC";
            if ("desc".equalsIgnoreCase(ascOrDesc)) {
                order = "DESC";
            }
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM movies ORDER BY duration " + order);
            while (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getLong("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setGenre(String.valueOf(Genre.valueOf(resultSet.getString("genre"))));
                movie.setDuration(resultSet.getInt("duration"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }

    @Override
    public List<Movie> getMoviesByTheaterIdAndStartTime(Long theaterId, LocalDate startTime) {
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT m.* FROM movies m " +
                        "INNER JOIN show_time st ON m.id = st.movie_id " +
                        "WHERE st.theatre_id = ? AND DATE(st.start_time) = ?")) {
            preparedStatement.setLong(1, theaterId);
            preparedStatement.setDate(2, Date.valueOf(startTime));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getLong("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setGenre(String.valueOf(Genre.valueOf(resultSet.getString("genre"))));
                movie.setDuration(resultSet.getInt("duration"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }

    @Override
    public String updateMovie(Long id, Movie updatedMovie) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE movies SET title = ?, genre = ?, duration = ? WHERE id = ?")) {
            preparedStatement.setString(1, updatedMovie.getTitle());
            preparedStatement.setString(2, updatedMovie.getGenre());
            preparedStatement.setInt(3, updatedMovie.getDuration());
            preparedStatement.setLong(4, id);
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                return "Movie updated successfully";
            } else {
                return "Movie not found with ID: " + id;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteMovie(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM movies WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows > 0) {
                return "Movie deleted successfully";
            } else {
                return "Movie not found with ID: " + id;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
