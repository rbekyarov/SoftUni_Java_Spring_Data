package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastSeedRootDto;
import softuni.exam.models.dto.ImportForecastDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.models.entity.DayOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ForecastServiceImpl implements ForecastService {
    private final static String FORECASTS_FILE_PATH = "src/main/resources/files/xml/forecasts.xml";
    private final ForecastRepository forecastRepository;
    private final CityRepository cityRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CityService cityService;

    public ForecastServiceImpl(ForecastRepository forecastRepository, CityRepository cityRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil, CityService cityService) {
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.cityService = cityService;
    }

    @Override
    public boolean areImported() {
        return forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files
                .readString(Path.of(FORECASTS_FILE_PATH));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        if (forecastRepository.count() == 0) {
            List<ImportForecastDto> importForecastDto =
                    xmlParser.fromFile(FORECASTS_FILE_PATH, ForecastSeedRootDto.class)
                            .getForecastSeedDtos();

            for (ImportForecastDto forecastDto : importForecastDto) {
                boolean isValid = validationUtil.isValid(forecastDto);
                Forecast forecast = modelMapper.map(forecastDto, Forecast.class);
                Optional<City> city = cityRepository.findCityById(forecastDto.getCity());

                if (!city.isPresent() || !isValid) {
                    sb.append("Invalid country\n");
                } else {
                    forecast.setDayOfWeek(forecastDto.getDayOfWeek());
                    forecast.setMaxTemperature(forecastDto.getMaxTemperature());
                    forecast.setMinTemperature(forecastDto.getMinTemperature());

                    forecast.setSunrise(convertToLocalTime(forecastDto.getSunrise()));
                    forecast.setSunset(convertToLocalTime(forecastDto.getSunset()));
                    forecast.setCity(city.get());

                    forecastRepository.save(forecast);
                    sb.append(String.format("Successfully imported forecast %s - %f\n", forecast.getDayOfWeek(), forecast.getMaxTemperature()));
                }
            }
        }

        return sb.toString();
    }

    @Override
    public String exportForecasts() {
        StringBuilder sb = new StringBuilder();

        List<Forecast> resultForecast = forecastRepository.export(DayOfWeek.SUNDAY, 150000);

        resultForecast
                .forEach(f -> {
                    sb.append(String.format("City: %s\n" +
                                            "-min temperature: %.2f\n" +
                                            "--max temperature: %.2f\n" +
                                            "---sunrise: %s\n" +
                                            "-----sunset: %s",
                                    f.getCity().getCityName(),
                                    f.getMinTemperature(),
                                    f.getMaxTemperature(),
                                    f.getSunrise(),
                                    f.getSunset()))
                            .append(System.lineSeparator());
                });

        return sb.toString().trim();
    }

    private LocalTime convertToLocalTime(String string) {
        return LocalTime.parse(string);
    }
}