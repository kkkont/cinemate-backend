package com.proovitoo.cinemate.controller;

import com.proovitoo.cinemate.entity.Movie;
import com.proovitoo.cinemate.entity.Schedule;
import com.proovitoo.cinemate.entity.Seat;
import com.proovitoo.cinemate.service.CinemaService;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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
    @GetMapping("/moviesbygenre")
    public List<Movie> getMovieByGenre(@RequestParam String genre){
        return cinemaService.getMoviesByGenre(genre);
    }

    @GetMapping("/seats")
    public List<Seat> getSeanceSeats(@RequestParam Long scheduleID){
        return cinemaService.getSeatsByScheduleId(scheduleID);
    }

}
