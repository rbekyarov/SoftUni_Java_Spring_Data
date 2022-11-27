package hiberspring.repository;


import hiberspring.domain.entities.EmployeeCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeCardRepository extends JpaRepository<EmployeeCard,Long> {
    Optional<EmployeeCard> findByNumber(String number);
}
