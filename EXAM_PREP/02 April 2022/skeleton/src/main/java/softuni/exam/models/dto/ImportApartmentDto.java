package softuni.exam.models.dto;

import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Town;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportApartmentDto {
  @XmlElement(name = "apartmentType")
  private ApartmentType apartmentType;
  @XmlElement(name = "area")
  private Double area;
  @XmlElement(name = "town")
  private String town;

  public ImportApartmentDto() {
  }

  public ApartmentType getApartmentType() {
    return apartmentType;
  }

  public void setApartmentType(ApartmentType apartmentType) {
    this.apartmentType = apartmentType;
  }



  @DecimalMin(value = "40")
  @NotNull
  public Double getArea() {
    return area;
  }

  public void setArea(Double area) {
    this.area = area;
  }
  @NotNull
  public String getTown() {
    return town;
  }

  public void setTown(String town) {
    this.town = town;
  }
}
