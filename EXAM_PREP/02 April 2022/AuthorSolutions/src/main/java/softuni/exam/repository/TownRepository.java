package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Town;

import java.util.List;
import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<Town,Long> {

    @Query("SELECT c from Town c " +
            "ORDER BY c.population desc, c.townName")
    List<Town> findCarsOrderByPicturesCountThenByMake();

    Optional<Town> getTownByTownName(String townName);


}
