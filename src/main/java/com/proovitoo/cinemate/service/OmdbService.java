package com.proovitoo.cinemate.service;

import com.proovitoo.cinemate.entity.Movie;
import com.proovitoo.cinemate.entity.OmdbMovieResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OmdbService {
    private final RestTemplate restTemplate;
    private final String apiKey = "e93c727c";

    public OmdbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * Fetches movie data from OMDb API
     * @param title - title of the movie
     * @return - Movie entity
     */
    public Movie fetchMovieData(String title) {
        String url = String.format("http://www.omdbapi.com/?t=%s&apikey=%s", title, apiKey);
        OmdbMovieResponse response = restTemplate.getForObject(url, OmdbMovieResponse.class);
        return mapToMovie(response);
    }

    /**
     * Maps the response to Movie entity
     * @param response - response from the API
     * @return - Movie entity
     */
    private Movie mapToMovie(OmdbMovieResponse response) {
        Movie movie = new Movie();
        movie.setMovieName(response.getTitle());
        movie.setReleaseYear(Integer.parseInt(response.getYear()));
        movie.setGenreNames(response.getGenre());
        movie.setRunningTimeInMinutes(Integer.parseInt(response.getRuntime().split(" ")[0]));
        movie.setDescription(response.getPlot());
        movie.setPhoto(response.getPoster());
        movie.setAgeRestriction(response.getRated());
        movie.setDirector(response.getDirector());
        movie.setWriter(response.getWriter());
        movie.setActors(response.getActors());
        movie.setCountry(response.getCountry());
        for (OmdbMovieResponse.Rating rating : response.getRatings()) {
            if(rating.getSource().equals("Internet Movie Database")) movie.setImdbRating(rating.getValue());
            else if(rating.getSource().equals("Rotten Tomatoes")) movie.setRtRating(rating.getValue());
        }
        return movie;
    }
}
