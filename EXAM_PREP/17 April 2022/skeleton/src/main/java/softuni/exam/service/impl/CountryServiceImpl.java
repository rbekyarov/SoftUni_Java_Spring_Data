package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import softuni.exam.models.dto.ImportCountriesDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    private static final String COUNTRIES_FILE_PATH = "src/main/resources/files/json/countries.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final CountryRepository countryRepository;

    public CountryServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, CountryRepository countryRepository) {
        this.modelMapper = modelMapper;

        this.gson = gson;
        this.validationUtil = validationUtil;
        this.countryRepository = countryRepository;
    }

    @Override
    public boolean areImported() {
        return countryRepository.count() >0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files
                .readString(Path.of(COUNTRIES_FILE_PATH));
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (countryRepository.count() == 0) {

            ImportCountriesDto[] importCountriesDto = gson.fromJson(readCountriesFromFile(), ImportCountriesDto[].class);
            for (ImportCountriesDto countriesDto : importCountriesDto) {
                boolean isValid = validationUtil.isValid(countriesDto);

                Country country = modelMapper.map(countriesDto, Country.class);
                Optional<Country> countryByCountryName = countryRepository.findCountryByCountryName(country.getCountryName());
                if (countryByCountryName.isPresent() || !isValid) {
                    sb.append("Invalid country\n");
                } else {
                    country.setCountryName(countriesDto.getCountryName());
                    country.setCurrency(countriesDto.getCurrency());

                    countryRepository.save(country);
                    sb.append(String.format("Successfully imported country %s - %s\n", country.getCountryName(), country.getCurrency()));
                }
            }
        }
       String d = sb.toString();
        return sb.toString();
    }

    @Override
    public Optional<Country> getCountryById(Long countryId) {
        return countryRepository.findById(countryId);
    }

}

