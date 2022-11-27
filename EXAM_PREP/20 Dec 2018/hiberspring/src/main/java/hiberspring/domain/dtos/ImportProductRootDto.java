package hiberspring.domain.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportProductRootDto {
    @XmlElement(name = "product")
    private List<ImportProductDto> importProductDtos;

    public List<ImportProductDto> getImportProductDtos() {
        return importProductDtos;
    }

    public void setImportProductDtos(List<ImportProductDto> importProductDtos) {
        this.importProductDtos = importProductDtos;
    }
}
