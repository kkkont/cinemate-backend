package com.proovitoo.cinemate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="MOVIES")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String movieName;
    private int runningTimeInMinutes;
    @Column(length = 10000)
    private String description;
    private String ageRestriction;
    private int releaseYear;
    private String photo;

    // Extra details
    private String director;
    private String writer;
    private String actors;
    private String country;
    private String imdbRating;
    private String rtRating;

    @ManyToMany
    @JoinTable(
            name = "MOVIE_GENRE",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonIgnore
    private Set<Genre> genres;
    private String genreNames;

}
