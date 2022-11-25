package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportTownDto;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class TownServiceImpl implements TownService {
    private static final String TOWNS_FILE_PATH = "src/main/resources/files/json/towns.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private TownRepository townRepository;

    public TownServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, TownRepository townRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return townRepository.count()>0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (townRepository.count() == 0) {

            ImportTownDto[] importTownDtos = this.gson.fromJson(readTownsFileContent(), ImportTownDto[].class);
            for (ImportTownDto townDto : importTownDtos) {
                boolean isValid = validationUtil.isValid(townDto);

                Town town = modelMapper.map(townDto, Town.class);
                Optional<Town> findByName = townRepository.findByName(town.getName());
                if (findByName.isPresent() || !isValid) {
                    sb.append("Invalid Town\n");
                } else {
                    town.setName(townDto.getName());
                    town.setPopulation(townDto.getPopulation());
                    town.setGuide(townDto.getGuide());
                    townRepository.save(town);

                    sb.append(String.format("Successfully imported Town %s - %d\n", town.getName(),town.getPopulation() ));
                }
            }
        }
        return sb.toString();
    }
}
