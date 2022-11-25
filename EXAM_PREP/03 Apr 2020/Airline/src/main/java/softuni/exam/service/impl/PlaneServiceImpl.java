package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportPlaneDto;
import softuni.exam.models.dto.ImportPlaneRootDto;
import softuni.exam.models.entity.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class PlaneServiceImpl implements PlaneService {
    private final static String PLANES_FILE_PATH = "src/main/resources/files/xml/planes.xml";
    private final PlaneRepository planeRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public PlaneServiceImpl(PlaneRepository planeRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.planeRepository = planeRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return planeRepository.count()>0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(PLANES_FILE_PATH));
    }

    @Override
    public String importPlanes() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        if (planeRepository.count() == 0) {
            ImportPlaneRootDto importPlaneRootDto = xmlParser
                    .fromFile(PLANES_FILE_PATH, ImportPlaneRootDto.class);
            List<ImportPlaneDto> importPlaneDtos = importPlaneRootDto.getImportPlaneDtos();
            for (ImportPlaneDto planesDto : importPlaneDtos) {

                boolean isValid = validationUtil.isValid(planesDto);

                Optional<Plane> byCaption = planeRepository.findByRegisterNumber(planesDto.getRegisterNumber());
                if (byCaption.isPresent()) {
                    isValid = false;
                }

                if (!isValid) {
                    sb.append("Invalid Plane\n");
                } else {
                    Plane plane = modelMapper.map(planesDto, Plane.class);
                    plane.setAirline(planesDto.getAirline());
                    plane.setCapacity(planesDto.getCapacity());
                    plane.setRegisterNumber(planesDto.getRegisterNumber());

                    planeRepository.save(plane);
                    sb.append(String.format("Successfully imported Plane %s\n", plane.getRegisterNumber()));
                }
            }
        }

        return sb.toString();
    }
}
