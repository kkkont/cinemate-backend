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
        return movieRepository.findByGenreNamesContainsAndAgeRestriction(genre, age);
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
     * Creates Movie, Genre, Schedule, Seat entities for the movies
     * Hardcoded. Not the best practice, but for the simplicity I kept it that way.
     */
    public void createMovies() {
        String[] titles = {"One Life", "Dune: Part Two", "Oppenheimer", "Anyone But You"};
        String[] genres = {"Drama, Biography", "Science Fiction, Adventure", "Biography, Historical Drama", "Comedy"};
        String[] descriptions = {"""
ONE LIFE tells the true story of Sir Nicholas ‘Nicky’ Winton, a young London broker who, in the months leading up to World War II, rescued 669 children from the Nazis. Nicky visited Prague in December 1938 and found families who had fled the rise of the Nazis in Germany and Austria, living in desperate conditions with little or no shelter and food, and under threat of Nazi invasion. He immediately realised it was a race against time. How many children could he and the team rescue before the borders closed?<br><br>

Fifty years later, it’s 1988 and Nicky lives haunted by the fate of the children he wasn’t able to bring to safety in England; always blaming himself for not doing more. It’s not until a live BBC television show, ‘That’s Life’, surprises him by introducing him to some surviving children – now adults – that he finally begins to come to terms with the guilt and grief he had carried for five decades.""",
                """
The saga continues as award-winning filmmaker Denis Villeneuve embarks on “Dune: Part Two,” the next chapter of Frank Herbert’s celebrated novel Dune, with an expanded all-star international ensemble cast. The film, from Warner Bros. Pictures and Legendary Pictures, is the highly anticipated follow-up to 2021’s six-time Academy Award-winning “Dune.<br><br>

 The big-screen epic continues the adaptation of Frank Herbert’s acclaimed bestseller Dune with returning and new stars, including Oscar nominee Timothée Chalamet (“Wonka,” “Call Me by Your Name”), Zendaya (“Spider-Man: No Way Home,” “Malcolm & Marie,” “Euphoria”), Rebecca Ferguson (“Mission: Impossible – Dead Reckoning”), Oscar nominee Josh Brolin (“Avengers: End Game,” “Milk”), Oscar nominee Austin Butler (“Elvis,” “Once Upon A Time…In Hollywood”), Oscar nominee Florence Pugh (“Black Widow,” “Little Women”), Dave Bautista (the “Guardians of the Galaxy” films, “Thor: Love and Thunder”), Oscar winner Christopher Walken (“The Deer Hunter,” “Hairspray”), Stephen McKinley Henderson (“Fences,” “Lady Bird”), Léa Seydoux (the “James Bond” franchise and “Crimes of the Future”), with Stellan Skarsgård (the “Mamma Mia!” films, “Avengers: Age of Ultron”), with Oscar nominee Charlotte Rampling (“45 Years,” “Assassin’s Creed”), and Oscar winner Javier Bardem (“No Country for Old Men,” “Being the Ricardos”).<br><br>

“Dune: Part Two” will explore the mythic journey of Paul Atreides as he unites with Chani and the Fremen while on a warpath of revenge against the conspirators who destroyed his family. Facing a choice between the love of his life and the fate of the known universe, he endeavors to prevent a terrible future only he can foresee.<br><br>

Villeneuve directed from a screenplay he co-wrote with Jon Spaihts based on Herbert’s novel. The film is produced by Mary Parent, Cale Boyter, Villeneuve, Tanya Lapointe and Patrick McCormick. The executive producers are Josh Grode, Herbert W. Gains, Jon Spaihts, Thomas Tull, Brian Herbert, Byron Merritt, Kim Herbert, with Kevin J. Anderson serving as creative consultant.<br><br>

Villeneuve is again collaborating with his “Dune” creatives: Oscar-winning director of photography Greig Fraser; Oscar-winning production designer Patrice Vermette; Oscar-winning editor Joe Walker; Oscar-winning visual effects supervisor Paul Lambert; Oscar-nominated costume designer Jacqueline West. Oscar-winning composer Hans Zimmer is again on hand to create the score.<br><br>

“Dune: Part Two” was filmed on location in Budapest, Abu Dhabi, Jordan and Italy.""",
                "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb.",
                "In the edgy comedy Anyone But You, Bea (Sydney Sweeney) and Ben (Glen Powell) look like the perfect couple, but after an amazing first date something happens that turns their fiery hot attraction ice cold — until they find themselves unexpectedly thrust together at a destination wedding in Australia. So they do what any two mature adults would do: pretend to be a couple.",
        };
        String[] photos = {"https://images.markus.live/mcswebsites.blob.core.windows.net/1013/Event_8841/poster_xxlarge/OneLife_Apollo_EPstr_2592x3840_EE.jpg?width=675&height=1000&format=jpg&quality=90",
                "https://images.markus.live/mcswebsites.blob.core.windows.net/1013/Event_8157/poster_xxlarge/Dune2_Apollo_EPstr_2592x3840.jpg?width=675&height=1000&format=jpg&quality=90",
                "https://images.markus.live/mcswebsites.blob.core.windows.net/1013/Event_7940/poster_xxlarge/Oppenheimer_2592x3840.jpg?width=675&height=1000&format=jpg&quality=90",
                "https://images.markus.live/mcswebsites.blob.core.windows.net/1013/Event_8824/poster_xxlarge/AnyoneButYou_B1Buoy_EE_Preview.jpg?width=675&height=1000&format=jpg&quality=90",

        };
        int[] runningTime = {110, 166, 180, 104};
        int[] releaseYear = {2023, 2023, 2023, 2023};
        String[] ageRestriction = {"PG13", "PG", "PG13", "PG13"};

        Set<Genre> genreSet = createGenres(genres);
        for (int i = 0; i < titles.length; i++) {
            Movie movie = new Movie();
            movie.setMovieName(titles[i]);
            movie.setRunningTimeInMinutes(runningTime[i]);
            movie.setDescription(descriptions[i]);
            movie.setAgeRestriction(ageRestriction[i]);
            movie.setReleaseYear(releaseYear[i]);
            movie.setPhoto(photos[i]);
            movie.setGenreNames(genres[i]);

            Set<Genre> movieGenres = new HashSet<>();
            String[] genreNames = genres[i].split(", ");
            for (String genreName : genreNames) {
                for (Genre genre : genreSet) {
                    if (genre.getName().equals(genreName)) {
                        movieGenres.add(genre);
                        //genre.getMovies().add(movie);
                        break;
                    }
                }
            }
            movie.setGenres(movieGenres);
            movieRepository.save(movie);
            createSchedule(movie, LocalDateTime.of(2024, 4, 26, 11, 0), "English", "Estonian");
            createSchedule(movie, LocalDateTime.of(2024, 4, 26, 13, 0), "English", "Estonian");
            createSchedule(movie, LocalDateTime.of(2024, 4, 26, 16, 0), "English", "Estonian");
            createSchedule(movie, LocalDateTime.of(2024, 4, 26, 19, 0), "English", "Estonian");

        }
    }

    /**
     * Creates Genre entities
     *
     * @param genres - all the genres
     * @return - a set of Genre entities
     */
    private Set<Genre> createGenres(String[] genres) {
        Set<Genre> genreSet = new HashSet<>();
        for (String genreString : genres) {
            String[] genreNames = genreString.split(", ");
            for (String genreName : genreNames) {
                // Check if the genre already exists
                if (!genreRepository.existsByName(genreName)) {
                    Genre genre = new Genre();
                    genre.setName(genreName);
                    genreSet.add(genre);
                    genreRepository.save(genre);
                }
            }
        }
        return genreSet;
    }

    /**
     * Creates a Schedule entity and saves it through repository
     *
     * @param movie     - Movie entity
     * @param dateTime  - the date and time of the seance
     * @param language  - the language of the seance
     * @param subtitles - the subtitles language of the seance
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
     * Creates Seat entities for the seance
     *
     * @param schedule - Schedule entity (seance)
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
