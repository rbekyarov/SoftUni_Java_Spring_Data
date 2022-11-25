package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportSellerRootDto {
    @XmlElement(name = "seller")
    private List<ImportSellerDto> importSellerDtos;

    public List<ImportSellerDto> getImportSellerDtos() {
        return importSellerDtos;
    }

    public void setImportSellerDtos(List<ImportSellerDto> importSellerDtos) {
        this.importSellerDtos = importSellerDtos;
    }
}
