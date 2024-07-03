package com.proovitoo.cinemate.repository;

import com.proovitoo.cinemate.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByGenreNamesContainsAndAgeRestriction(String genre, String age);
}
