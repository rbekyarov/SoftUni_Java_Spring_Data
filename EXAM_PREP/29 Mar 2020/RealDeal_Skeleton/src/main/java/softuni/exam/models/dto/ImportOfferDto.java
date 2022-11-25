package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportOfferDto {

    @XmlElement(name = "price")
    private BigDecimal price;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "has-gold-status")
    private Boolean hasGoldStatus;

    @XmlElement(name = "added-on")
    private String addedOn;

    @XmlElement(name = "car")
    private CarId car;

    @XmlElement(name = "seller")
    private SellerId seller;

    public ImportOfferDto() {
    }

    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Size(min = 5)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHasGoldStatus() {
        return hasGoldStatus;
    }

    public void setHasGoldStatus(Boolean hasGoldStatus) {
        this.hasGoldStatus = hasGoldStatus;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public CarId getCar() {
        return car;
    }

    public void setCar(CarId car) {
        this.car = car;
    }

    public SellerId getSeller() {
        return seller;
    }

    public void setSeller(SellerId seller) {
        this.seller = seller;
    }
}
