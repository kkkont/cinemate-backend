package com.proovitoo.cinemate.controller;

import com.proovitoo.cinemate.entity.*;
import com.proovitoo.cinemate.repository.SeatRepository;
import com.proovitoo.cinemate.service.CinemaService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/cinemate")
public class CinemaController {
    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    /**
     * Endpoint for movies
     *
     * @return - all the movies in the database
     */
    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        return cinemaService.getAllMovies();
    }

    /**
     * Endpoint for filtered movies
     *
     * @param genre - genre which is selected
     * @param age   - age restriction which is selected
     * @return - filtered movies
     */
    @GetMapping("/filter")
    public List<Movie> getFilteredMovies(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String age
    ) {
        return cinemaService.getFilteredMovies(genre, age);
    }

    /**
     * Endpoint for finding movie by id
     *
     * @param id - provided id
     * @return - movie
     */
    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = cinemaService.getMovieById(id);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint for finding all genres
     * @return - all genres
     */
    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return cinemaService.findAllGenres();
    }

    /**
     * Endpoint for finding all age restrictions
     * @return - all age restrictions
     */
    @GetMapping("/rated")
    public List<String> getAllAgeRestrictions(){
        return cinemaService.findAllAgeRestriction();
    }

    /**
     * Endpoint for a movie schedule
     *
     * @param movieId - id of the movie we would like a schedule for
     * @return - a schedule for the provided movie (id)
     */
    @GetMapping("/schedule_movie")
    public List<Schedule> getScheduleByMovieID(@RequestParam Long movieId) {
        return cinemaService.getScheduleByMovieId(movieId);
    }

    /**
     * Endpoint for the recommended seats which are in one seance
     *
     * @param scheduleID      - id of a specific seance
     * @param numberOfTickets - how many tickets are being bought
     * @return - recommended seats for exactly the number of tickets and for the seance with schedule id
     */

    @GetMapping("/seats")
    public List<Seat> getSeanceSeats(@RequestParam Long scheduleID, @RequestParam int numberOfTickets) {
        return cinemaService.getSeatsByScheduleId(scheduleID, numberOfTickets);
    }

    /**
     * Endpoint for creating user history
     *
     * @param userHistory - the history which we would like to save
     * @return - response entity CREATED
     */
    @PostMapping("/userHistory")
    public ResponseEntity<UserHistory> createUserHistory(@RequestBody UserHistory userHistory) {
        UserHistory savedUserHistory = cinemaService.save(userHistory);
        return new ResponseEntity<>(savedUserHistory, HttpStatus.CREATED);
    }

    /**
     * Endpoint for updating seat occupancy after the tickets are bought
     *
     * @param seatIds - the seats that are selected by the user
     * @return - response entity ACCEPTED
     */
    @PostMapping("/update-occupancy")
    public ResponseEntity<HttpStatus> updateSeatOccupancy(@RequestBody List<Long> seatIds) {
        cinemaService.updateSeatOccupancy(seatIds);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }


}
