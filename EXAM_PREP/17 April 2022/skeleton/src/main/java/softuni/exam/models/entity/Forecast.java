package softuni.exam.models.entity;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "forecasts")
public class Forecast extends BaseEntity {

    private DayOfWeek dayOfWeek;

    private Double minTemperature;

    private Double maxTemperature;

    private LocalTime sunrise;

    private LocalTime sunset;

    private City city;

    public Forecast() {
    }

    public Forecast(DayOfWeek dayOfWeek, Double minTemperature, Double maxTemperature, LocalTime sunrise, LocalTime sunset, City city) {
        this.dayOfWeek = dayOfWeek;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.city = city;
    }
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    @Column(name = "min_temperature", nullable = false)
    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }
    @Column(name = "max_temperature", nullable = false)
    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
    @Column(nullable = false)
    public LocalTime getSunrise() {
        return sunrise;
    }

    public void setSunrise(LocalTime sunrise) {
        this.sunrise = sunrise;
    }
    @Column(nullable = false)
    public LocalTime getSunset() {
        return sunset;
    }

    public void setSunset(LocalTime sunset) {
        this.sunset = sunset;
    }
}
