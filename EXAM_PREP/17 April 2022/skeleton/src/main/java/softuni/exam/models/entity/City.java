package softuni.exam.models.entity;



import javax.persistence.*;


@Entity
@Table(name = "cities")
public class City extends BaseEntity{

    private String cityName;

    private String	description;

    private Integer population;

    private Country country;

    public City() {
    }

    public City(String cityName, String description, Integer population, Country country) {
        this.cityName = cityName;
        this.description = description;
        this.population = population;
        this.country = country;
    }
    @Column(nullable = false, unique = true, name = "city_name")
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }



    @Column(columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Column(nullable = false)
    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }
    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;

    }
}
