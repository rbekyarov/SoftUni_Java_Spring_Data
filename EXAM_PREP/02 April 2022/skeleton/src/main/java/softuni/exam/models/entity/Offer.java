package softuni.exam.models.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {
    private BigDecimal price;
    private LocalDate publishedOn;
    private Apartment apartment;
    private Agent agent;

    public Offer() {
    }

    public Offer(BigDecimal price, LocalDate publishedOn, Apartment apartment, Agent agent) {
        this.price = price;
        this.publishedOn = publishedOn;
        this.apartment = apartment;
        this.agent = agent;
    }
    @Column
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
     @Column
    public LocalDate getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(LocalDate publishedOn) {
        this.publishedOn = publishedOn;
    }
    @ManyToOne
    @JoinColumn(name = "apartment_id", referencedColumnName = "id")
    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }
    @ManyToOne
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
