package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;

import java.util.List;


@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

   @Query("select o from Offer as o where o.apartment.apartmentType = :type order by o.apartment.area desc ,o.price")

    List<Offer> export(@Param("type") ApartmentType type);

}
