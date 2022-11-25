package exam.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "shops")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportShopRootDto {
    @XmlElement(name = "shop")
    private List<ImportShopDto> importShopDtos;

    public List<ImportShopDto> getImportShopDtos() {
        return importShopDtos;
    }

    public void setImportShopDtos(List<ImportShopDto> importShopDtos) {
        this.importShopDtos = importShopDtos;
    }
}
