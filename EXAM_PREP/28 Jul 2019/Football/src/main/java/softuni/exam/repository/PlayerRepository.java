package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.entity.Player;
import softuni.exam.domain.entities.entity.Team;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByLastName(String name);

    List<Player> findAllByTeamIs(Optional<Team> teamName);

    @Query("select p from Player as p where p.salary>:salary order by p.salary desc")
    List<Player> findAllBySalary(@Param("salary") BigDecimal salary);
}
