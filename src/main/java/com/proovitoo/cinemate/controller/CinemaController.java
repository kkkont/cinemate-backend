package com.proovitoo.cinemate.controller;

import com.proovitoo.cinemate.entity.Movie;
import com.proovitoo.cinemate.entity.Schedule;
import com.proovitoo.cinemate.entity.Seat;
import com.proovitoo.cinemate.entity.UserHistory;
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
    @PostConstruct
    public void init() {
        cinemaService.createMovies();
    }

    @GetMapping("/movies")
    public List<Movie> getAllMovies(){
        return cinemaService.getAllMovies();
    }

    @GetMapping("/schedule_movie")
    public List<Schedule> getScheduleByMovieID(@RequestParam Long movieId){return cinemaService.getScheduleByMovieId(movieId);}
    @GetMapping("/seats")
    public List<Seat> getSeanceSeats(@RequestParam Long scheduleID,@RequestParam int numberOfTickets){
        return cinemaService.getSeatsByScheduleId(scheduleID, numberOfTickets);
    }
    @PostMapping("/userHistory")
    public ResponseEntity<UserHistory> createUserHistory(@RequestBody UserHistory userHistory) {
        UserHistory savedUserHistory = cinemaService.save(userHistory);
        return new ResponseEntity<>(savedUserHistory, HttpStatus.CREATED);
    }

}
