package com.proovitoo.cinemate.repository;

import com.proovitoo.cinemate.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    /**
     * Would implement it after to apply some filters
     * @param genreName - the genre of movies we are looking for
     * @return - list of the movies in that genre
     */
    List<Movie> findByGenresName(String genreName);
}
