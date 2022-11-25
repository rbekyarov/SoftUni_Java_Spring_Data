package com.example.football.repository;

import com.example.football.models.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {
    //birth date after 01-01-1995 and before 01-01-2003



    @Query("select p from Player as p where p.birthDate BETWEEN :start and :end " +
            "order by p.stat.shooting desc ,p.stat.passing desc , p.stat.endurance desc, p.lastName")

    List<Player> export(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
