package com.proovitoo.cinemate.service;

import com.proovitoo.cinemate.entity.*;
import com.proovitoo.cinemate.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CinemaService {
    private final MovieRepository movieRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final SeatRepository seatRepository;
    private final GenreRepository genreRepository;

    public CinemaService(MovieRepository movieRepository, ScheduleRepository scheduleRepository, UserHistoryRepository userHistoryRepository, SeatRepository seatRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.scheduleRepository = scheduleRepository;
        this.userHistoryRepository = userHistoryRepository;
        this.seatRepository = seatRepository;
        this.genreRepository = genreRepository;
    }

    /**
     * To find movie with id.
     * @param id - id that is provided
     * @return - movie
     */
    public Movie getMovieById(Long id){
        return movieRepository.findMovieById(id);
    }
    /**
     * Find seats by schedule id and the number of tickets.
     * Use recommendSeats method to mark seats as recommended
     *
     * @param scheduleId      - id of the seance
     * @param numberOfTickets - number of the tickets being bought
     * @return - a list of seats for the seance
     */
    public List<Seat> getSeatsByScheduleId(Long scheduleId, int numberOfTickets) {
        recommendSeats(scheduleId, numberOfTickets);
        return seatRepository.findByScheduleId(scheduleId);
    }

    /**
     * Algorithm that recommends seats to the middle of the hall using Euclidean distance
     *
     * @param scheduleId      - id of the seance
     * @param numberOfTickets - number of the tickets being bought
     */
    public void recommendSeats(Long scheduleId, int numberOfTickets) {
        setAllRecommendedToFalse(scheduleId);
        int centerRow = 5;
        int centerSeat = 5;
        double minDistance = Double.MAX_VALUE;
        int minRowDifference = Integer.MAX_VALUE; // For secondary criterion when the distances are the same

        List<Seat> allSeats = seatRepository.findByScheduleId(scheduleId);
        List<Seat> recommendedSeats = new ArrayList<>();

        // Convert list of seats to a map for faster access
        Map<Integer, Map<Integer, Seat>> seatMap = new HashMap<>();
        for (Seat seat : allSeats) {
            seatMap.computeIfAbsent(seat.getSeatRow(), k -> new HashMap<>())
                    .put(seat.getSeatNumber(), seat);
        }

        for (Seat startingSeat : allSeats) {
            int row = startingSeat.getSeatRow();
            int startingNumber = startingSeat.getSeatNumber();

            // Check if a contiguous block of seats fits within the row
            if (startingNumber + numberOfTickets - 1 > 9) {
                continue;
            }

            boolean oneIsOccupied = false;
            List<Seat> tempSeats = new ArrayList<>();

            for (int i = 0; i < numberOfTickets; i++) {
                Seat seat = seatMap.get(row).get(startingNumber + i);
                if (seat == null || seat.isOccupied()) {
                    oneIsOccupied = true;
                    break;
                }
                tempSeats.add(seat);
            }

            if (!oneIsOccupied) {
                // Calculate the middle seat's position
                int seatsFromStartingSeat = (numberOfTickets - 1) / 2;
                Seat centeredSeat = tempSeats.get(seatsFromStartingSeat);

                // Calculate the distance from the center
                double newDistance = calculateDistance(centeredSeat, centerRow, centerSeat);
                int rowDifference = Math.abs(row - centerRow);

                // Update recommended seats based on primary and secondary criteria
                if (newDistance < minDistance || (newDistance == minDistance && rowDifference < minRowDifference)) {
                    minDistance = newDistance;
                    minRowDifference = rowDifference;
                    recommendedSeats = new ArrayList<>(tempSeats);
                }
            }
        }

        for (Seat seat : recommendedSeats) {
            seat.setRecommended(true);
        }

        seatRepository.saveAll(recommendedSeats);
    }

    /**
     * Calculates distance from the center by using Euclidean distance
     *
     * @param seat       - the seat that we are calculating distance for
     * @param centerRow  - number of the row of centered seat
     * @param centerSeat - number of the centered seat
     * @return - the distance between two
     */
    private double calculateDistance(Seat seat, int centerRow, int centerSeat) {
        int rowDifference = seat.getSeatRow() - centerRow;
        int seatDifference = seat.getSeatNumber() - centerSeat;
        return Math.sqrt(rowDifference * rowDifference + seatDifference * seatDifference); // Euclidean distance
    }


    /**
     * Sets all the recommended seats back to false.
     * Used every time before new seat selection process
     *
     * @param scheduleId - id of the seance
     */
    private void setAllRecommendedToFalse(Long scheduleId) {
        List<Seat> seats = seatRepository.findByScheduleId(scheduleId);
        for (Seat seat : seats) {
            seat.setRecommended(false);
        }
    }

    /**
     * @return - all movies from the repository
     */
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    /**
     * Filtering movies
     *
     * @param genre - genre which is selected
     * @param age   - age restriction which is selected
     * @return - list of filtered moviesˇ
     */

    public List<Movie> getFilteredMovies(String genre, String age) {
        return movieRepository.findMoviesByFilters(genre, age);
    }

    /**
     * Saves user history
     *
     * @param userHistory - the collected data which is being saved
     * @return - using repository we save the history
     */
    public UserHistory save(UserHistory userHistory) {
        return userHistoryRepository.save(userHistory);
    }

    /**
     * We set the seat occupation to true through seat repository
     *
     * @param seatIds - a list of seats that are being updated
     */
    public void updateSeatOccupancy(List<Long> seatIds) {
        seatRepository.findAllById(seatIds).forEach(seat -> {
            seat.setOccupied(true);
            seatRepository.save(seat);
        });
    }

    /**
     * Finds a schedule for a movie
     *
     * @param movieId - id of a movie for which we are finding a schedule for
     * @return - a list of Schedule entities for the movie
     */
    public List<Schedule> getScheduleByMovieId(Long movieId) {
        return scheduleRepository.findByMovieId(movieId);
    }

}
