package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
    @Query("select u ,count(p) as c, p.caption,p.picture.size from User as u join Post as p on u.profilePicture.id= p.user.id order by p.user.profilePicture.size asc ")
    List<User> export();
}
