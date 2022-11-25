package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCarDto;
import softuni.exam.models.dto.ImportPictureDto;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Picture;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PictureServiceImpl implements PictureService {
    private static final String PICTURES_FILE_PATH = "src/main/resources/files/json/pictures.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private PictureRepository pictureRepository;
    private CarRepository carRepository;

    public PictureServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, PictureRepository pictureRepository, CarRepository carRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.pictureRepository = pictureRepository;
        this.carRepository = carRepository;
    }

    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return Files.readString(Path.of(PICTURES_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (pictureRepository.count() == 0) {

            ImportPictureDto[] importPictureDtos = this.gson.fromJson(readPicturesFromFile(), ImportPictureDto[].class);
            for (ImportPictureDto pictureDto : importPictureDtos) {
                boolean isValid = validationUtil.isValid(pictureDto);

                Picture picture = modelMapper.map(pictureDto, Picture.class);

                Optional<Picture> picUnique = pictureRepository.findAllByName(pictureDto.getName());
                Optional<Car> getCar = carRepository.findById(Long.parseLong(pictureDto.getCarId()));
                if (picUnique.isPresent() || !isValid || getCar.isEmpty()) {
                    sb.append("Invalid Picture\n");
                } else {
                    picture.setName(pictureDto.getName());

                    String registeredOn = pictureDto.getDateAndTime();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime localDate = LocalDateTime.parse(registeredOn, formatter);
                    picture.setDateAndTime(localDate);
                    picture.setCar(getCar.get());
                    pictureRepository.save(picture);
                    sb.append(String.format("Successfully import picture - %s\n", picture.getName()));
                }
            }
        }
        return sb.toString();
    }
}
