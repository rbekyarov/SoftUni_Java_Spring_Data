package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Offer;

import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Long> {
    @Query("select o from Offer as o where concat(o.description ,o.addedOn) = :parameters")
    Optional<Offer> getUnique(@Param("parameters") String parameters);
}
