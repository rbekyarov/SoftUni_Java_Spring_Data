package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.DayOfWeek;
import softuni.exam.models.entity.Forecast;
import java.util.List;



@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    @Query("select f from Forecast as f join City as c on c.id= f.city.id where " +
            "f.dayOfWeek = :day and c.population < :population " +
            "order by  f.maxTemperature desc, f.id")
    List<Forecast> export(@Param("day") DayOfWeek day,@Param("population") Integer population);
}
