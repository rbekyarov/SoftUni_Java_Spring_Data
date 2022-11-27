package hiberspring.service.Impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.ImportTownDto;
import hiberspring.domain.entities.Town;
import hiberspring.repository.TownRepository;
import hiberspring.service.TownService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class TownServiceImpl implements TownService {
    private static final String TOWNS_FILE_PATH = "src/main/resources/files/towns.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final TownRepository townRepository;

    public TownServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, TownRepository townRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
    }

    @Override
    public Boolean townsAreImported() {
        return townRepository.count()>0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return Files
                .readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns(String townsFileContent) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (townRepository.count() == 0) {

            ImportTownDto[] importTownDtos = this.gson.fromJson(readTownsJsonFile(), ImportTownDto[].class);
            for (ImportTownDto townDto : importTownDtos) {
                boolean isValid = validationUtil.isValid(townDto);

                Town town = modelMapper.map(townDto, Town.class);
                Optional<Town> townByName = townRepository.findByName(townDto.getName());
                if (townByName.isPresent() || !isValid) {
                    sb.append("Error: Invalid data.\n");

                    }else {
                    townRepository.save(town);
                        sb.append(String.format("Successfully imported Town %s.\n", town.getName()));
                    }
                }
            }
        return sb.toString();
    }

    @Override
    public Optional<Town> findByName(String town) {
        return townRepository.findByName(town);
    }
}
