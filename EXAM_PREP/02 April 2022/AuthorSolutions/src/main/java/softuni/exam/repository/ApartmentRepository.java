package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment,Long> {

    Optional<Apartment> findApartmentByAreaAndTown(Double area, Town town);
}
