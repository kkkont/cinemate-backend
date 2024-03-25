package com.proovitoo.cinemate.service;

import com.proovitoo.cinemate.entity.*;
import com.proovitoo.cinemate.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void addToUserHistory(Schedule schedule) {
        UserHistory userHistory = new UserHistory();
        userHistory.setSchedule(schedule);
        userHistoryRepository.save(userHistory);
    }

    public List<Seat> getSeatsByScheduleId(Long scheduleId) {
        return seatRepository.findByScheduleId(scheduleId);
    }

    public List<Seat> getAvailableSeatsByScheduleId(Long scheduleId) {
        return seatRepository.findByScheduleIdAndOccupied(scheduleId, false);
    }

    public void markSeatAsRecommended(Long seatId) {
        Seat seat = seatRepository.findById(seatId).orElse(null);
        if (seat != null) {
            seat.setRecommended(true);
            seatRepository.save(seat);
        }
    }

    public void updateSeatOccupancy(Long seatId, boolean isOccupied) {
        Seat seat = seatRepository.findById(seatId).orElse(null);
        if (seat != null) {
            seat.setOccupied(isOccupied);
            seatRepository.save(seat);
        }
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenresName(genre);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesByGenre(String genre) {
        return scheduleRepository.findByMovieGenresName(genre);
    }

    public List<Schedule> getSchedulesByMovieTitle(String movieTitle) {
        return scheduleRepository.findByMovieGenresName(movieTitle);
    }

    public void createMovies() {
        String[] titles = {"One Life", "Dune: Part Two", "Oppenheimer", "Anyone But You"};
        String[] genres = {"Drama, Biography", "Science Fiction, Adventure", "Biography, Historical Drama", "Comedy"};
        String[] descriptions = {"ONE LIFE tells the true story of Sir Nicholas ‘Nicky’ Winton, a young London broker who, in the months leading up to World War II, rescued 669 children from the Nazis. Nicky visited Prague in December 1938 and found families who had fled the rise of the Nazis in Germany and Austria, living in desperate conditions with little or no shelter and food, and under threat of Nazi invasion. He immediately realised it was a race against time. How many children could he and the team rescue before the borders closed?\n" + "\n" + "Fifty years later, it’s 1988 and Nicky lives haunted by the fate of the children he wasn’t able to bring to safety in England; always blaming himself for not doing more. It’s not until a live BBC television show, ‘That’s Life’, surprises him by introducing him to some surviving children – now adults – that he finally begins to come to terms with the guilt and grief he had carried for five decades.",
                "The saga continues as award-winning filmmaker Denis Villeneuve embarks on “Dune: Part Two,” the next chapter of Frank Herbert’s celebrated novel Dune, with an expanded all-star international ensemble cast. The film, from Warner Bros. Pictures and Legendary Pictures, is the highly anticipated follow-up to 2021’s six-time Academy Award-winning “Dune.”\n" + "\n" + "The big-screen epic continues the adaptation of Frank Herbert’s acclaimed bestseller Dune with returning and new stars, including Oscar nominee Timothée Chalamet (“Wonka,” “Call Me by Your Name”), Zendaya (“Spider-Man: No Way Home,” “Malcolm & Marie,” “Euphoria”), Rebecca Ferguson (“Mission: Impossible – Dead Reckoning”), Oscar nominee Josh Brolin (“Avengers: End Game,” “Milk”), Oscar nominee Austin Butler (“Elvis,” “Once Upon A Time…In Hollywood”), Oscar nominee Florence Pugh (“Black Widow,” “Little Women”), Dave Bautista (the “Guardians of the Galaxy” films, “Thor: Love and Thunder”), Oscar winner Christopher Walken (“The Deer Hunter,” “Hairspray”), Stephen McKinley Henderson (“Fences,” “Lady Bird”), Léa Seydoux (the “James Bond” franchise and “Crimes of the Future”), with Stellan Skarsgård (the “Mamma Mia!” films, “Avengers: Age of Ultron”), with Oscar nominee Charlotte Rampling (“45 Years,” “Assassin’s Creed”), and Oscar winner Javier Bardem (“No Country for Old Men,” “Being the Ricardos”).\n" + "\n" + "“Dune: Part Two” will explore the mythic journey of Paul Atreides as he unites with Chani and the Fremen while on a warpath of revenge against the conspirators who destroyed his family. Facing a choice between the love of his life and the fate of the known universe, he endeavors to prevent a terrible future only he can foresee.\n" + "\n" + "Villeneuve directed from a screenplay he co-wrote with Jon Spaihts based on Herbert’s novel. The film is produced by Mary Parent, Cale Boyter, Villeneuve, Tanya Lapointe and Patrick McCormick. The executive producers are Josh Grode, Herbert W. Gains, Jon Spaihts, Thomas Tull, Brian Herbert, Byron Merritt, Kim Herbert, with Kevin J. Anderson serving as creative consultant.\n" + "\n" + "Villeneuve is again collaborating with his “Dune” creatives: Oscar-winning director of photography Greig Fraser; Oscar-winning production designer Patrice Vermette; Oscar-winning editor Joe Walker; Oscar-winning visual effects supervisor Paul Lambert; Oscar-nominated costume designer Jacqueline West. Oscar-winning composer Hans Zimmer is again on hand to create the score.\n" + "\n" + "“Dune: Part Two” was filmed on location in Budapest, Abu Dhabi, Jordan and Italy.",
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
            createSchedule(movie, LocalDateTime.of(2024, 4, 26, 11, 0),"English","Estonian");
            createSchedule(movie, LocalDateTime.of(2024, 4, 26, 13, 0),"English","Estonian");
            createSchedule(movie, LocalDateTime.of(2024, 4, 26, 16, 0),"English","Estonian");
            createSchedule(movie, LocalDateTime.of(2024, 4, 26, 19, 0),"English","Estonian");

        }
    }

    private Set<Genre> createGenres(String[] genres) {
        Set<Genre> genreSet = new HashSet<>();
        for (String genreString : genres) {
            String[] genreNames = genreString.split(", ");
            for (String genreName : genreNames) {
                Genre genre = new Genre();
                genre.setName(genreName);
                genreSet.add(genre);
                genreRepository.save(genre);
            }
        }
        return genreSet;
    }

    public void createSchedule(Movie movie, LocalDateTime dateTime, String language, String subtitles) {
        Schedule schedule = new Schedule();
        schedule.setMovie(movie);
        schedule.setDateTime(dateTime);
        schedule.setLanguage(language);
        schedule.setSubtitles(subtitles);

        scheduleRepository.save(schedule);
        createSeatsForSchedule(schedule);
    }

    private void createSeatsForSchedule(Schedule schedule) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
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
