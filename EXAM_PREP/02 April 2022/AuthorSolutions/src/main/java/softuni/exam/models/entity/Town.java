package softuni.exam.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity {
    private String townName;
    private Integer population;

    public Town() {
    }

    @Column(unique = true, nullable = false)
    public String getTownName() {
        return townName;
    }

    public void setTownName(String make) {
        this.townName = make;
    }

    @Column(nullable = false)
    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer kilometers) {
        this.population = kilometers;
    }

}
