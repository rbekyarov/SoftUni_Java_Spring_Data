package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CarsSeedDtoJSON;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    private static final String CARS_FILE_PATH = "src/main/resources/files/json/cars.json";
    private final CarRepository carRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CarServiceImpl(CarRepository carRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.carRepository = carRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;

    }

    @Override
    public boolean areImported() {
        return carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder builder = new StringBuilder();
        CarsSeedDtoJSON[] carsSeedDtoJSONS = gson.fromJson(readCarsFileContent(), CarsSeedDtoJSON[].class);
        Arrays.stream(carsSeedDtoJSONS)
                .filter(carsSeedDtoJSON -> {
                            boolean isValid = validationUtil.isValid(carsSeedDtoJSON);

                            builder.append(isValid ? String.format("Successfully imported car - %s - %s",
                                    carsSeedDtoJSON.getMake(),
                                    carsSeedDtoJSON.getModel())
                                    : "Invalid car").append(System.lineSeparator());

                            return isValid;
                        }
                ).map(carsSeedDtoJSON ->
                        modelMapper.map(carsSeedDtoJSON, Car.class)).forEach(carRepository::save);
        return builder.toString();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder builder = new StringBuilder();
        List<Car> cars = carRepository.findAllCars();
        for (Car car : cars) {
            builder.append(String.format("Car make - %s, model - %s\n" +
                                    "\tKilometers - %d\n" +
                                    "\tRegistered on - %s\n" +
                                    "\tNumber of pictures - %d",
                            car.getMake(),
                            car.getModel(),
                            car.getKilometers(),
                            car.getRegisteredOn(),
                            car.getPictures().size()))
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }

    @Override
    public Car findCarById(Long car) {
        return carRepository.findById(car).orElse(null);
    }


}
