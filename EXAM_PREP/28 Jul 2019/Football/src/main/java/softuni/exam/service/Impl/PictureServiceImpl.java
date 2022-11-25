package softuni.exam.service.Impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.entities.dto.ImportPictureDto;
import softuni.exam.domain.entities.dto.ImportPictureRootDto;
import softuni.exam.domain.entities.entity.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class PictureServiceImpl implements PictureService {
    private final static String PICTURES_FILE_PATH = "src/main/resources/files/xml/pictures.xml";
    private final PictureRepository pictureRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public PictureServiceImpl(PictureRepository pictureRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.pictureRepository = pictureRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public String importPictures() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        if (pictureRepository.count() == 0) {
            ImportPictureRootDto importPictureRootDto = xmlParser
                    .fromFile(PICTURES_FILE_PATH, ImportPictureRootDto.class);
            List<ImportPictureDto> pictureDto = importPictureRootDto.getImportPictureDtos();
            for (ImportPictureDto importPictureDto : pictureDto) {
                boolean isValid = validationUtil.isValid(importPictureDto);
                if (!isValid) {
                    sb.append("Invalid picture\n");
                } else {
                    Picture picture = modelMapper.map(importPictureDto, Picture.class);

                    pictureRepository.save(picture);
                    sb.append(String.format("Successfully imported picture - %s\n", picture.getUrl()));
                }
            }
        }
        return sb.toString();

    }

    @Override
    public boolean areImported() {
        //TODO Implement me
        return pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        //TODO Implement me
        return Files
                .readString(Path.of(PICTURES_FILE_PATH));
    }

    @Override
    public Optional<Picture> findByUrl(String url) {
        return pictureRepository.findByUrl(url);
    }

}
