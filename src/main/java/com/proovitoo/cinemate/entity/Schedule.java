package com.proovitoo.cinemate.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;
    private LocalDateTime dateTime;
    private String language;
    private String subtitles;
    @OneToMany(mappedBy = "schedule")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Collection<Seat> seats;
}
