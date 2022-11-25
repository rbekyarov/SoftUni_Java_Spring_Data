package softuni.exam.models.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.time.LocalDate;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedDto {

    @XmlElement(name = "price")
    private BigDecimal price;
    @XmlElement(name = "agent")
    private AgentName name;
    @XmlElement(name = "apartment")
    private ApartmentIdDto apartment;
    @XmlElement(name = "publishedOn")
    private String publishedOn;




    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public AgentName getName() {
        return name;
    }

    public OfferSeedDto setName(AgentName name) {
        this.name = name;
        return this;
    }


    public ApartmentIdDto getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentIdDto apartment) {
        this.apartment = apartment;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public OfferSeedDto setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
        return this;
    }
}
