package exam.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "towns")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTownRootDto {
    @XmlElement(name = "town")
    private List<ImportTownDto> importTownDtos;

    public List<ImportTownDto> getImportTownDtos() {
        return importTownDtos;
    }

    public void setImportTownDtos(List<ImportTownDto> importTownDtos) {
        this.importTownDtos = importTownDtos;
    }
}
