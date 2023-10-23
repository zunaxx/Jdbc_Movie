package peaksoft.service.impl;

import peaksoft.dao.MovieDao;
import peaksoft.dao.ShowTimeDao;
import peaksoft.dao.TheatreDao;
import peaksoft.dao.impl.MovieDaoImpl;
import peaksoft.dao.impl.ShowTimeImpldao;
import peaksoft.dao.impl.TheatreDaoImpl;
import peaksoft.models.Movie;
import peaksoft.models.ShowTime;
import peaksoft.models.Theater;
import peaksoft.service.ShowTimeServices;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ShowTimeServiceImpl implements ShowTimeServices {
    ShowTimeDao showTimeDao = new ShowTimeImpldao();
    MovieDao movieDao = new MovieDaoImpl();
    TheatreDao theatreDao = new TheatreDaoImpl();

    Connection connection;

    public ShowTimeServiceImpl() {
        this.connection = connection;
    }

    @Override
    public ShowTime saveShow(ShowTime showTime) {
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO show_time (movie_id, theatre_id, start_time, end_time) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, showTime.getMovie_id());
            preparedStatement.setLong(2, showTime.getTheatre_id());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(showTime.getStart_time()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(showTime.getEnd_time()));
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                showTime.setId(generatedKeys.getLong(1));
            }
            return showTime;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ShowTime findById(Long id) {
        ShowTime showTime = showTimeDao.find(id);

        if (showTime == null) {
            throw new RuntimeException("ShowTime с ID " + id + " не найден");
        }

        return showTime;
    }

    @Override
    public String assign(Long showTime_id, Long movie_id) {
        ShowTime showTime = findById(showTime_id);
        Movie movie = movieDao.findById(movie_id);
        if (showTime != null && movie != null) {
            showTime.setMovie_id(movie.getId());
            showTimeDao.assign(showTime);
            return "Фильм успешно назначен для данного ShowTime";
        } else {
            return "Ошибка: ShowTime или фильм не найдены";
        }
    }

    @Override
    public String assign(Long showTimeId, Long movieId, Long theaterId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE show_time SET movie_id = ?, theatre_id = ? WHERE id = ?");
            preparedStatement.setLong(1, movieId);
            preparedStatement.setLong(2, theaterId);
            preparedStatement.setLong(3, showTimeId);
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                return "Assigned successfully";
            } else {
                return "ShowTime not found with ID: " + showTimeId;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ShowTime> getAllShowTimes() {
        List<ShowTime> showTimes = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM show_time");
            while (resultSet.next()) {
                ShowTime showTime = new ShowTime();
                showTime.setId(resultSet.getLong("id"));
                showTime.setMovie_id(resultSet.getLong("movie_id"));
                showTime.setTheatre_id(resultSet.getLong("theatre_id"));
                showTime.setStart_time(resultSet.getTimestamp("start_time").toLocalDateTime());
                showTime.setEnd_time(resultSet.getTimestamp("end_time").toLocalDateTime());
                showTimes.add(showTime);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return showTimes;
    }

    @Override
    public String deleteShowTimeByStartAndEndTime(LocalDate startTime, LocalDate endTime) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM show_time WHERE start_time >= ? AND start_time <= ? AND end_time >= ? AND end_time <= ?");
            preparedStatement.setTimestamp(1, Timestamp.valueOf(startTime.atStartOfDay()));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(endTime.plusDays(1).atStartOfDay()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(startTime.atStartOfDay()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(endTime.plusDays(1).atStartOfDay()));
            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows > 0) {
                return "ShowTimes deleted successfully";
            } else {
                return "No ShowTimes found within the specified time range";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<Theater, List<Movie>>> getMoviesGroupByTheater() {
        return showTimeDao.getMoviesGroupByTheatre();
    }
}
