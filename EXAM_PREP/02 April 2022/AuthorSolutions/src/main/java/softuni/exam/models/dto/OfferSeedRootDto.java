package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedRootDto {

    @XmlElement(name = "offer")
    private List<OfferSeedDto> offerRootDtoList;

    public List<OfferSeedDto> getOfferRootDtoList() {
        return offerRootDtoList;
    }

    public void setOfferRootDtoList(List<OfferSeedDto> offerRootDtoList) {
        this.offerRootDtoList = offerRootDtoList;
    }
}
