package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softuni.exam.models.entity.Seller;

import java.util.Optional;


public interface SellerRepository extends JpaRepository<Seller,Long> {
    Optional<Seller> findByEmail(String email);


}
