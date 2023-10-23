package peaksoft;

import peaksoft.config.JdbcConfig;
import peaksoft.models.Genre;
import peaksoft.models.Movie;
import peaksoft.models.ShowTime;
import peaksoft.models.Theater;
import peaksoft.service.MovieService;
import peaksoft.service.ShowTimeServices;
import peaksoft.service.TheatreService;
import peaksoft.service.impl.MovieServiceImpl;
import peaksoft.service.impl.ShowTimeServiceImpl;
import peaksoft.service.impl.TheatreServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Инициализация сервисов и подключение к базе данных
        MovieService movieService = new MovieServiceImpl();
        ShowTimeServices showTimeService = new ShowTimeServiceImpl();
        TheatreService theatreService = new TheatreServiceImpl();

        boolean exit = false;
        while (!exit) {
            System.out.println("Меню:");
            System.out.println("1. Создать фильм");
            System.out.println("2. Найти фильм по ID");
            System.out.println("3. Назначить фильм ShowTime");
            System.out.println("4. Создать театр");
            System.out.println("5. Найти театр по ID");
            System.out.println("6. Получить фильмы, сгруппированные по жанру");
            System.out.println("7. Удалить фильм по ID");
            System.out.println("8. Получить все сеансы в кинотеатрах");
            System.out.println("9. Удалить сеансы по диапазону времени");
            System.out.println("10. Получить все фильмы, сгруппированные по кинотеатрам");
            System.out.println("11. Выйти");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Чтобы считать символ новой строки

            switch (choice) {
                case 1:
                    // Создать фильм
                    System.out.print("Введите название фильма: ");
                    String title = scanner.nextLine();
                    System.out.print("Введите жанр: ");
                    String genre = scanner.nextLine();
                    System.out.print("Введите продолжительность: ");
                    int duration = scanner.nextInt();
                    Movie movie = new Movie();
                    String result = movieService.saveMovie(movie);
                    System.out.println(result);
                    break;
                case 2:
                    // Найти фильм по ID
                    System.out.print("Введите ID фильма: ");
                    long movieId = scanner.nextLong();
                    Movie foundMovie = movieService.findById(movieId);
                    if (foundMovie != null) {
                        System.out.println("Найден фильм: " + foundMovie);
                    } else {
                        System.out.println("Фильм не найден с ID: " + movieId);
                    }
                    break;
                case 3:
                    // Назначить фильм ShowTime
                    System.out.print("Введите ID ShowTime: ");
                    long showTimeId = scanner.nextLong();
                    System.out.print("Введите ID фильма: ");
                    long movieIdForShowTime = scanner.nextLong();
                    String assignResult = showTimeService.assign(showTimeId, movieIdForShowTime);
                    System.out.println(assignResult);
                    break;
                case 4:
                    // Создать театр
                    System.out.print("Введите название театра: ");
                    String theaterName = scanner.nextLine();
                    System.out.print("Введите местоположение: ");
                    String location = scanner.nextLine();
                    long theaterId = 0;
                    Theater theatre = new Theater(theaterId, theaterName, location);
                    Theater savedTheatre = ((TheatreServiceImpl) theatreService).saveTheater(theatre);
                    System.out.println("Театр создан: " + savedTheatre);
                    break;
                case 5:
                    // Найти театр по ID
                    System.out.print("Введите ID театра: ");
                    theaterId = scanner.nextLong();
                    Theater foundTheatre = theatreService.findById(theaterId);
                    if (foundTheatre != null) {
                        System.out.println("Найден театр: " + foundTheatre);
                    } else {
                        System.out.println("Театр не найден с ID: " + theaterId);
                    }
                    break;
                case 6:
                    // Получить фильмы, сгруппированные по жанру
                    System.out.print("Введите жанр: ");
                    String genreForGrouping = scanner.nextLine();
                    Map<Genre, List<Movie>> moviesByGenre = movieService.getMoviesByGenre(genreForGrouping);
                    if (moviesByGenre.isEmpty()) {
                        System.out.println("Фильмы данного жанра не найдены.");
                    } else {
                        for (Map.Entry<Genre, List<Movie>> entry : moviesByGenre.entrySet()) {
                            Genre key = entry.getKey();
                            List<Movie> movies = entry.getValue();
                            System.out.println("Жанр: " + key);
                            for (Movie m : movies) {
                                System.out.println("Фильм: " + m);
                            }
                        }
                    }
                    break;
                case 7:
                    // Удалить фильм по ID
                    System.out.print("Введите ID фильма для удаления: ");
                    long movieIdToDelete = scanner.nextLong();
                    String deleteResult = movieService.deleteMovie(movieIdToDelete);
                    System.out.println(deleteResult);
                    break;
                case 8:
                    // Получить все сеансы в кинотеатрах
                    List<ShowTime> allShowTimes = showTimeService.getAllShowTimes();
                    if (allShowTimes.isEmpty()) {
                        System.out.println("Сеансы не найдены.");
                    } else {
                        for (ShowTime showTime : allShowTimes) {
                            System.out.println("Сеанс: " + showTime);
                        }
                    }
                    break;
                case 9:
                    // Удалить сеансы по диапазону времени
                    System.out.print("Введите начальную дату и время (yyyy-MM-dd HH:mm): ");
                    String startTimeStr = scanner.nextLine();
                    System.out.print("Введите конечную дату и время (yyyy-MM-dd HH:mm): ");
                    String endTimeStr = scanner.nextLine();
                    LocalDate startTime = LocalDate.parse(startTimeStr);
                    LocalDate endTime = LocalDate.parse(endTimeStr);
                    String deleteShowTimesResult = showTimeService.deleteShowTimeByStartAndEndTime(startTime, endTime);
                    System.out.println(deleteShowTimesResult);
                    break;
                case 10:
                    // Получить все фильмы, сгруппированные по кинотеатрам
                    List<Map<Theater, List<Movie>>> moviesGroupedByTheater = showTimeService.getMoviesGroupByTheater();
                    if (moviesGroupedByTheater.isEmpty()) {
                        System.out.println("Фильмы в кинотеатрах не найдены.");
                    } else {
                        for (Map<Theater, List<Movie>> theaterMap : moviesGroupedByTheater) {
                            for (Map.Entry<Theater, List<Movie>> entry : theaterMap.entrySet()) {
                                Theater theater = entry.getKey();
                                List<Movie> movies = entry.getValue();
                                System.out.println("Кинотеатр: " + theater);
                                for (Movie m : movies) {
                                    System.out.println("Фильм: " + m);
                                }
                            }
                        }
                        break;

                    }
                case 11:
                    exit = true;
                    System.out.println("Выход из программы.");
                    break;
                default:
                    System.out.println("Неправильный выбор. Пожалуйста, выберите действие из меню.");
                    break;
            }
        }

        JdbcConfig.closeConnection();
    }
}

