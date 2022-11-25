package softuni.exam.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "apartments")
public class Apartment extends BaseEntity{
    private ApartmentType apartmentType;
    private Double area;
    private Town town;

    public Apartment() {
    }

    public Apartment(ApartmentType apartmentType, Double area, Town town) {
        this.apartmentType = apartmentType;
        this.area = area;
        this.town = town;
    }
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }
    @Column
    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }
    @ManyToOne
    @JoinColumn(name = "town_id", referencedColumnName = "id")
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }
}
