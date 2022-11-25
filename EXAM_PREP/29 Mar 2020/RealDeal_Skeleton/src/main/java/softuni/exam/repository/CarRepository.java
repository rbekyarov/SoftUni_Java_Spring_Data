package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Car;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    @Query("select c from Car as c where concat(c.make ,c.model,c.kilometers) = :parameters")
    Optional<Car>getUnique(@Param("parameters") String parameters);

    Optional<Car> findById(Long id);

    @Query("SELECT c FROM Car AS c ORDER BY size(c.pictures) DESC, c.make")
    List<Car> findAllCars();


}
