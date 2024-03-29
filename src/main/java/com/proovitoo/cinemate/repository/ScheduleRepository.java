package com.proovitoo.cinemate.repository;

import com.proovitoo.cinemate.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findByMovieGenresName(String genreName);
    List<Schedule> findByMovieId(Long movie_id);
    List<Schedule> findByMovieMovieName(String movieName);
}
