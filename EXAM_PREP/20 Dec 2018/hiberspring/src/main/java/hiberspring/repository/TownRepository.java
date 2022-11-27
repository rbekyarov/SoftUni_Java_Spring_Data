package hiberspring.repository;


import hiberspring.domain.entities.Town;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TownRepository extends JpaRepository<Town,Long> {
    Optional<Town> findByName(String name);
}
