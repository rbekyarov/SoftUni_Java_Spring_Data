package softuni.exam;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import softuni.exam.service.CityService;
import softuni.exam.service.CountryService;

import java.io.IOException;

@Component
public class Runner implements CommandLineRunner {
    private CountryService countryService;
    private CityService cityService;

    public Runner(CountryService countryService, CityService cityService) {
        this.countryService = countryService;
        this.cityService = cityService;
    }

    @Override
    public void run(String... args) throws Exception {
       // impCountries();
       // System.out.println("hy");
      // impCities();
    }
    private void impCountries() throws IOException {
        countryService.importCountries();
    }
    private void impCities() throws IOException {
        cityService.importCities();
    }
}
