package com.proovitoo.cinemate.repository;

import com.proovitoo.cinemate.entity.Genre;
import com.proovitoo.cinemate.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findGenreByName(String name);
    List<Genre> findAll();
}
