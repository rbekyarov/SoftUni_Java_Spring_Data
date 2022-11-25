package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.ImportPictureDto;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class PictureServiceImpl implements PictureService {
    private static final String PICTURES_FILE_PATH = "src/main/resources/files/pictures.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private PictureRepository pictureRepository;

    public PictureServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, PictureRepository pictureRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public boolean areImported() {
        return pictureRepository.count()>0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(PICTURES_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (pictureRepository.count() == 0) {

            ImportPictureDto[] importPictureDtos = this.gson.fromJson(readFromFileContent(), ImportPictureDto[].class);
            for (ImportPictureDto pictureDto : importPictureDtos) {
                boolean isValid = validationUtil.isValid(pictureDto);

                Picture picture = modelMapper.map(pictureDto, Picture.class);
                Optional<Picture> findByPath = pictureRepository.findByPath(picture.getPath());
                if (findByPath.isPresent() || !isValid) {
                    sb.append("Invalid Picture\n");
                } else {
                    picture.setPath(pictureDto.getPath());
                    picture.setSize(pictureDto.getSize());
                    pictureRepository.save(picture);
                    sb.append(String.format("Successfully imported Picture, with size %.2f\n", picture.getSize() ));
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String exportPictures() {
        StringBuilder sb = new StringBuilder();
        List<Picture> allBySize = pictureRepository.findAllBySize(30000f);
        for (Picture picture : allBySize) {
            sb.append(String.format("%.2f – %s\n",picture.getSize(),picture.getPath() ));
        }

        return sb.toString();
    }
}
