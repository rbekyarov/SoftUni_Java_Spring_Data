package softuni.exam.domain.entities.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name = "pictures")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPictureRootDto {
    @XmlElement(name = "picture")
    private List<ImportPictureDto>importPictureDtos;

    public List<ImportPictureDto> getImportPictureDtos() {
        return importPictureDtos;
    }

    public void setImportPictureDtos(List<ImportPictureDto> importPictureDtos) {
        this.importPictureDtos = importPictureDtos;
    }
}
