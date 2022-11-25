package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name = "planes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPlaneRootDto {
    @XmlElement(name = "plane")
    private List<ImportPlaneDto> importPlaneDtos;

    public List<ImportPlaneDto> getImportPlaneDtos() {
        return importPlaneDtos;
    }

    public void setImportPlaneDtos(List<ImportPlaneDto> importPlaneDtos) {
        this.importPlaneDtos = importPlaneDtos;
    }
}
