package softuni.exam.instagraphlite.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "posts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPostRootDto {
    @XmlElement(name = "post")
    private List<ImportPostDto> importPostDtos;

    public List<ImportPostDto> getImportPostDtos() {
        return importPostDtos;
    }

    public void setImportPostDtos(List<ImportPostDto> importPostDtos) {
        this.importPostDtos = importPostDtos;
    }
}
