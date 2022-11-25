package softuni.exam.instagraphlite.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entity.Picture;

import java.util.List;
import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture,Long> {
    Optional<Picture> findByPath(String path);
    //Export Pictures with Size Bigger Than 30000

@Query("select p from Picture as p where p.size>:num order by p.size asc")
    List<Picture> findAllBySize(@Param("num") Float num);
}
