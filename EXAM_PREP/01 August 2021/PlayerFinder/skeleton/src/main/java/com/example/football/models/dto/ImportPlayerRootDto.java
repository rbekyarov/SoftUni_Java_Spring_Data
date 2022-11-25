package com.example.football.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "players")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPlayerRootDto {
    @XmlElement(name = "player")
    private List<ImportPlayerDto> importPlayerDtos;

    public List<ImportPlayerDto> getImportPlayerDtos() {
        return importPlayerDtos;
    }

    public void setImportPlayerDtos(List<ImportPlayerDto> importPlayerDtos) {
        this.importPlayerDtos = importPlayerDtos;
    }
}
