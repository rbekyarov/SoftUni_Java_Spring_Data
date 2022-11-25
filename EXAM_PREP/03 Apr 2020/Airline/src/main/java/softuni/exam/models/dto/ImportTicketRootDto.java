package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tickets")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTicketRootDto {
    @XmlElement(name = "ticket")
    private List<ImportTicketDto> importTicketDtos;

    public List<ImportTicketDto> getImportTicketDtos() {
        return importTicketDtos;
    }

    public void setImportTicketDtos(List<ImportTicketDto> importTicketDtos) {
        this.importTicketDtos = importTicketDtos;
    }
}
