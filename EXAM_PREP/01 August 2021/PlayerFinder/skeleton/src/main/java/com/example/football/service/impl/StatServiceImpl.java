package com.example.football.service.impl;

import com.example.football.models.dto.ImportStatDto;
import com.example.football.models.dto.ImportStatRootDto;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class StatServiceImpl implements StatService {
    private final static String STATS_FILE_PATH = "src/main/resources/files/xml/stats.xml";
    private final StatRepository statRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public StatServiceImpl(StatRepository statRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.statRepository = statRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files
                .readString(Path.of(STATS_FILE_PATH));
    }

    @Override
    public String importStats() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        if (statRepository.count() == 0) {
            ImportStatRootDto importStatRootDto = xmlParser
                    .fromFile(STATS_FILE_PATH, ImportStatRootDto.class);
            List<ImportStatDto> statDto = importStatRootDto.getImportStatDtos();
            for (ImportStatDto importStatDto : statDto) {
                boolean isValid = validationUtil.isValid(importStatDto);
                if (!isValid) {
                    sb.append("Invalid apartment\n");
                } else {
                    Stat stat = modelMapper.map(statDto, Stat.class);

                    stat.setEndurance(importStatDto.getEndurance());
                    stat.setPassing(importStatDto.getPassing());
                    stat.setShooting(importStatDto.getShooting());
                    statRepository.save(stat);
                    sb.append(String.format("Successfully imported Stat %.2f - %.2f - %.2f\n", stat.getEndurance(), stat.getPassing(), stat.getShooting()));
                }
            }
        }
        return sb.toString();
    }
}
