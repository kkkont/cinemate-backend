package com.proovitoo.cinemate.repository;

import com.proovitoo.cinemate.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g " +
            "WHERE (:genre IS NULL OR g.name = :genre) " +
            "AND (:age IS NULL OR m.ageRestriction = :age)")
    List<Movie> findMoviesByFilters(@Param("genre") String genre,
                                    @Param("age") String age);

    Movie findMovieById(Long id);
}
