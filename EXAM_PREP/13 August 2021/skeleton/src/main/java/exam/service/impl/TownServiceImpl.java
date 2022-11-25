package exam.service.impl;



import exam.model.dto.ImportTownDto;
import exam.model.dto.ImportTownRootDto;
import exam.model.entity.Town;
import exam.repository.TownRepository;
import exam.service.TownService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;



@Service
public class TownServiceImpl implements TownService {
    private final static String TOWNS_FILE_PATH = "src/main/resources/files/xml/towns.xml";
    private final TownRepository townRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public TownServiceImpl(TownRepository townRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return townRepository.count()>0;
    }

    @Override
    public String readTownsFileContent()throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException, JAXBException {

        StringBuilder sb = new StringBuilder();
        if (townRepository.count() == 0) {
            ImportTownRootDto importTownRootDto = xmlParser
                    .fromFile(TOWNS_FILE_PATH, ImportTownRootDto.class);
            List<ImportTownDto> townDtoList = importTownRootDto.getImportTownDtos();
            for (ImportTownDto importTownDto : townDtoList) {
                boolean isValid = validationUtil.isValid(importTownDto);
                if (!isValid) {
                    sb.append("Invalid Town\n");
                } else {
                    Town town = modelMapper.map(importTownDto, Town.class);

                    town.setName(importTownDto.getName());
                    town.setPopulation(importTownDto.getPopulation());
                    town.setTravelGuide(importTownDto.getTravelGuide());
                    townRepository.save(town);
                    sb.append(String.format("Successfully imported Town %s\n", town.getName()));
                }
            }
        }
        return sb.toString();
    }
}
