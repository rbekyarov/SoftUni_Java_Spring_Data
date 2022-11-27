package hiberspring.domain.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportEmployeeRootDto {
    @XmlElement(name = "employee")
    private List<ImportEmployeeDto> importEmployeeDtos;

    public List<ImportEmployeeDto> getImportEmployeeDtos() {
        return importEmployeeDtos;
    }

    public void setImportEmployeeDtos(List<ImportEmployeeDto> importEmployeeDtos) {
        this.importEmployeeDtos = importEmployeeDtos;
    }
}
