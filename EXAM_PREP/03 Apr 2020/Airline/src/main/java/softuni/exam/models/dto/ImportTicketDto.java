package softuni.exam.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTicketDto {
    @XmlElement(name = "serial-number")
    private String serialNumber;
    @XmlElement(name = "price")
    private BigDecimal price;
    @XmlElement(name = "take-off")
    private String takeoff;
    @XmlElement(name = "from-town")
    private TownName fromTown;
    @XmlElement(name = "to-town")
    private TownName toTown;
    @XmlElement(name = "passenger")
    private PassengerEmail passenger;
    @XmlElement(name = "plane")
    private PlaneRegisterNumber plane;

    public ImportTicketDto() {
    }

    @Size(min = 2)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @NotNull
    public String getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(String takeoff) {
        this.takeoff = takeoff;
    }

    @NotNull
    public TownName getFromTown() {
        return fromTown;
    }

    public void setFromTown(TownName fromTown) {
        this.fromTown = fromTown;
    }

    @NotNull
    public TownName getToTown() {
        return toTown;
    }

    public void setToTown(TownName toTown) {
        this.toTown = toTown;
    }

    @NotNull
    public PassengerEmail getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEmail passenger) {
        this.passenger = passenger;
    }

    @NotNull
    public PlaneRegisterNumber getPlane() {
        return plane;
    }

    public void setPlane(PlaneRegisterNumber plane) {
        this.plane = plane;
    }
}
