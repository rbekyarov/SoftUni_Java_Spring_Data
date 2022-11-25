package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedRootDtoXML {

    @XmlElement(name = "offer")
    private List<OfferSeedDtoXML> offers;

    public OfferSeedRootDtoXML() {
    }

    public List<OfferSeedDtoXML> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferSeedDtoXML> offers) {
        this.offers = offers;
    }
}
