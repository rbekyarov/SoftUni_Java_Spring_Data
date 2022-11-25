package softuni.exam.models.entity;

import softuni.exam.models.entity.enums.ApartmentType;

import javax.persistence.*;

@Entity
@Table(name = "apartments")
public class Apartment extends BaseEntity {

    private Double area;
    private ApartmentType apartmentType;
    private Town town;

    public Apartment() {
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }

    @Column(nullable = false)
    public Double getArea() {
        return area;
    }

    public Apartment setArea(Double area) {
        this.area = area;
        return this;
    }

    @ManyToOne
    public Town getTown() {
        return town;
    }

    public Apartment setTown(Town town) {
        this.town = town;
        return this;
    }
}
