package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCarDto;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {
    private static final String CARS_FILE_PATH = "src/main/resources/files/json/cars.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private CarRepository carRepository;
    private PictureRepository pictureRepository;

    public CarServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, CarRepository carRepository, PictureRepository pictureRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.carRepository = carRepository;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public boolean areImported() {
        return carRepository.count()>0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (carRepository.count() == 0) {

            ImportCarDto[] importCarDtos = this.gson.fromJson(readCarsFileContent(), ImportCarDto[].class);
            for (ImportCarDto pictureDto : importCarDtos) {
                boolean isValid = validationUtil.isValid(pictureDto);

                Car car = modelMapper.map(pictureDto, Car.class);

                Optional<Car> carUnique = carRepository.getUnique(car.getMake()+car.getModel()+car.getKilometers());
                if (carUnique.isPresent() || !isValid) {
                    sb.append("Invalid Picture\n");
                } else {
                    car.setKilometers(pictureDto.getKilometers());
                    car.setMake(pictureDto.getMake());
                    car.setModel(pictureDto.getModel());

                    String registeredOn = pictureDto.getRegisteredOn();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate localDate = LocalDate.parse(registeredOn, formatter);
                    car.setRegisteredOn(localDate);

                    carRepository.save(car);
                    sb.append(String.format("Successfully imported car - %s - %s\n", car.getMake(),car.getModel() ));
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder sb = new StringBuilder();

 List<Car> cars = carRepository.findAllCars();
      for (Car car : cars) {
            sb.append(String.format("Car make - %s, model - %s\n" +
                       "    Kilometers - %d\n" +
                        "    Registered on - %s\n" +
                         "    Number of pictures - %d\n",
                    car.getMake(),car.getModel(),
                    car.getKilometers(),car.getRegisteredOn(),
                    car.getPictures().size()));
        }

        return sb.toString();
    }
}
