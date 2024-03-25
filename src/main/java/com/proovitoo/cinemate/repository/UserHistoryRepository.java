package com.proovitoo.cinemate.repository;

import com.proovitoo.cinemate.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long>{

}

