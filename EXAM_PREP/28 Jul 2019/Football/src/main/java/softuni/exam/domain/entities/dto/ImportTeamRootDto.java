package softuni.exam.domain.entities.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "teams")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTeamRootDto {
    @XmlElement(name = "team")
    private List<ImportTeamDto> importTeamDto;

    public List<ImportTeamDto> getImportTeamDto() {
        return importTeamDto;
    }

    public void setImportTeamDto(List<ImportTeamDto> importTeamDto) {
        this.importTeamDto = importTeamDto;
    }
}
