package com.proovitoo.cinemate.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class OmdbMovieResponse {
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Year")
    private String year;
    @JsonProperty("Rated")
    private String rated;
    @JsonProperty("Runtime")
    private String runtime;
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("Plot")
    private String plot;
    @JsonProperty("Poster")
    private String poster;
    @JsonProperty("Director")
    private String director;
    @JsonProperty("Writer")
    private String writer ;
    @JsonProperty("Actors")
    private String actors;
    @JsonProperty("Country")
    private String country;

    @JsonProperty("Ratings")
    private List<Rating> ratings;

    @Getter
    public static class Rating {
        @JsonProperty("Source")
        private String source;

        @JsonProperty("Value")
        private String value;

    }
}
