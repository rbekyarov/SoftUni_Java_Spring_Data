package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name = "apartments")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportApartmentRootDto {
    @XmlElement(name = "apartment")
    private List<ImportApartmentDto> importApartmentDtoList;

    public List<ImportApartmentDto> getImportApartmentDtoList() {
        return importApartmentDtoList;
    }

    public void setImportApartmentDtoList(List<ImportApartmentDto> importApartmentDtoList) {
        this.importApartmentDtoList = importApartmentDtoList;
    }
}
