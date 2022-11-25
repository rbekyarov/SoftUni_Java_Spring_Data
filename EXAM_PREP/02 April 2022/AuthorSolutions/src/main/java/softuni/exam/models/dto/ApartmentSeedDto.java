package softuni.exam.models.dto;

import softuni.exam.models.entity.enums.ApartmentType;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApartmentSeedDto {

    @XmlElement(name = "apartmentType")
    private ApartmentType apartmentType;
    @XmlElement(name = "area")
    private Double area;
    @XmlElement(name = "town")
    private String town;

    @NotNull
    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public ApartmentSeedDto setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
        return this;
    }

    @DecimalMin(value = "40.00")
    public Double getArea() {
        return area;
    }

    public ApartmentSeedDto setArea(Double area) {
        this.area = area;
        return this;
    }

    public String getTown() {
        return town;
    }

    public ApartmentSeedDto setTown(String town) {
        this.town = town;
        return this;
    }
}
