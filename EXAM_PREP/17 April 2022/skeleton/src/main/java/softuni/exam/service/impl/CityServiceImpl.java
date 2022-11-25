package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCityDto;

import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {
    private static final String CITIES_FILE_PATH = "src/main/resources/files/json/cities.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private CityRepository cityRepository;
    private CountryRepository countryRepository;
    private CountryService countryService;

    public CityServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, CityRepository cityRepository, CountryRepository countryRepository, CountryService countryService) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.countryService = countryService;
    }

    @Override
    public boolean areImported() {
        return cityRepository.count() >0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(Path.of(CITIES_FILE_PATH));
    }

    @Override
    public String importCities() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (cityRepository.count() == 0) {

            ImportCityDto[] importCityDto = this.gson.fromJson(readCitiesFileContent(), ImportCityDto[].class);
            for (ImportCityDto citiesDto : importCityDto) {
                boolean isValid = validationUtil.isValid(citiesDto);

                City city = modelMapper.map(citiesDto, City.class);
                Optional<City> cityByCountryName = cityRepository.findCitiesByCityName(city.getCityName());
                if (cityByCountryName.isPresent() || !isValid) {
                    sb.append("Invalid country\n");
                } else {
                    Optional<Country> countryById= countryRepository.findCountryByIdIs(citiesDto.getCountry());
                    if (countryById.isEmpty()) {
                        sb.append("ERROR: %s \n").append(citiesDto.getCityName());

                    }else {
                        city.setCityName(citiesDto.getCityName());
                        city.setDescription(citiesDto.getDescription());
                        city.setPopulation(citiesDto.getPopulation());
                        city.setCountry(countryById.get());

                        cityRepository.save(city);
                        sb.append(String.format("Successfully imported country %s - %s\n", city.getCityName(), city.getPopulation()));
                    }

                }
            }
        }

        return sb.toString();
    }
}
