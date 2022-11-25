package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Passenger;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByEmail(String email);


    @Query("select p , count (t) as co from Passenger as p " +
            "JOIN Ticket as t on p.id=t.passenger.id group by t.passenger.id order by co desc, p.email")
    List<Passenger> getPassengers();

}
