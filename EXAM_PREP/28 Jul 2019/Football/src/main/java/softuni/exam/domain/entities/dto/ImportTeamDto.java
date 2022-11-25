package softuni.exam.domain.entities.dto;

import softuni.exam.domain.entities.entity.Picture;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTeamDto {
   @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "picture")
    private PictureURL picture;

    public ImportTeamDto() {
    }
@Size(min = 3, max = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PictureURL getPicture() {
        return picture;
    }

    public void setPicture(PictureURL picture) {
        this.picture = picture;
    }
}
