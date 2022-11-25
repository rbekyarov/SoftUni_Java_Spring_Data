package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "forecasts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ForecastSeedRootDto {

    @XmlElement(name = "forecast")
    private List<ImportForecastDto> forecastSeedDtos;

    public List<ImportForecastDto> getForecastSeedDtos() {
        return forecastSeedDtos;
    }

    public void setForecastSeedDtos(List<ImportForecastDto> forecastSeedDtos) {
        this.forecastSeedDtos = forecastSeedDtos;
    }
}
