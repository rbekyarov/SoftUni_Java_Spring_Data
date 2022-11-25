package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerSeedRootDtoXML {

    @XmlElement(name = "seller")
    private List<SellerSeedDtoXML> sellers;

    public SellerSeedRootDtoXML() {
    }
    public List<SellerSeedDtoXML> getSellers() {
        return sellers;
    }

    public void setSellers(List<SellerSeedDtoXML> sellers) {
        this.sellers = sellers;
    }
}
