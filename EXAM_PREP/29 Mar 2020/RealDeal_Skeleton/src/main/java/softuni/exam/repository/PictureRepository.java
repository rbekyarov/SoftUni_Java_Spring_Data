package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Picture;

import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture,Long> {
    Optional<Picture> findAllByName(String name);
    @Query("select count(p.id) from Picture as p where p.car.id = :id")
    public int getCountPic(@Param("id") Long id);

}
