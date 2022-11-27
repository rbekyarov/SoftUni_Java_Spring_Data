package hiberspring.repository;

import hiberspring.domain.entities.Employee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    @Query("select e from Employee as e  JOIN Branch as b on e.branch.id = b.id " +
            "right outer join Product as p on b.id=p.branch.id order by concat(e.firstName,e.lastName)")
    List<Employee> export();
}
