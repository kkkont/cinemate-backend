package com.proovitoo.cinemate.config;

import com.proovitoo.cinemate.entity.Genre;
import com.proovitoo.cinemate.entity.Movie;
import com.proovitoo.cinemate.entity.Schedule;
import com.proovitoo.cinemate.entity.Seat;
import com.proovitoo.cinemate.repository.GenreRepository;
import com.proovitoo.cinemate.repository.MovieRepository;
import com.proovitoo.cinemate.repository.ScheduleRepository;
import com.proovitoo.cinemate.repository.SeatRepository;
import com.proovitoo.cinemate.service.OmdbService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final OmdbService omdbService;


    public DataInitializer(MovieRepository movieRepository, GenreRepository genreRepository,
                           ScheduleRepository scheduleRepository, SeatRepository seatRepository,  OmdbService omdbService) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.scheduleRepository = scheduleRepository;
        this.seatRepository = seatRepository;
        this.omdbService = omdbService;
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> createMovies();
    }

    /** INSERT NEW MOVIES HERE **/
    String[] titles = {
            "One Life",
            "Dune: Part Two",
            "Oppenheimer",
            "Anyone But You",
            "Dune",
            "Inside out" ,
            "Apteeker Melchior",
            "Frankenstein"};

    /**
     * Creates movie entities using OMDb API service
     */
    public void createMovies() {
        LocalDate today = LocalDate.now();
        for (String title : titles) {
            Movie movie = omdbService.fetchMovieData(title);
            if (movie != null) {
                Set<Genre> movieGenres = createGenres(movie.getGenreNames().split(", "));
                movie.setGenres(movieGenres);
                movieRepository.save(movie);
                createSchedule(movie, today.atTime(11, 0), "English", "Estonian");
                createSchedule(movie, today.atTime(13, 0), "English", "Estonian");
                createSchedule(movie, today.atTime(16, 0), "English", "Estonian");
                createSchedule(movie, today.atTime(19, 0), "English", "Estonian");
            }
        }
    }

    /**
     * Creates genres based on the movies
     * @param genres - genres string
     * @return - Genre entities list
     */
    private Set<Genre> createGenres(String[] genres) {
        Set<Genre> genreSet = new HashSet<>();
        for (String genreName : genres) {
            Genre genre = genreRepository.findGenreByName(genreName);
            if (genre == null) {
                genre = new Genre();
                genre.setName(genreName);
                genreRepository.save(genre);
            }
            genreSet.add(genre);
        }
        return genreSet;
    }

    /**
     * Creates schedule for the movie
     * @param movie - movie
     * @param dateTime - date and time
     * @param language - language
     * @param subtitles - subtitles
     */
    public void createSchedule(Movie movie, LocalDateTime dateTime, String language, String subtitles) {
        Schedule schedule = new Schedule();
        schedule.setMovie(movie);
        schedule.setDateTime(dateTime);
        schedule.setLanguage(language);
        schedule.setSubtitles(subtitles);

        scheduleRepository.save(schedule);
        createSeatsForSchedule(schedule);
    }

    /**
     * Creates seat entities for the schedule
     * @param schedule - schedule we are creating seats for
     */
    private void createSeatsForSchedule(Schedule schedule) {
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                Seat seat = new Seat();
                seat.setSchedule(schedule);
                seat.setSeatRow(i);
                seat.setSeatNumber(j);
                seat.setOccupied(false);
                seat.setRecommended(false);
                seatRepository.save(seat);
            }
        }
    }
}