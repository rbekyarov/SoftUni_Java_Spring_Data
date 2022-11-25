package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ApartmentIdDto {

    @XmlElement(name = "id")
    private Long id;


    public Long getId() {
        return id;
    }

    public ApartmentIdDto setId(Long id) {
        this.id = id;
        return this;
    }
}
