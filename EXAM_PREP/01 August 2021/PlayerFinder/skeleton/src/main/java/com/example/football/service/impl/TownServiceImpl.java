package com.example.football.service.impl;

import com.example.football.models.dto.ImportTownDto;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    public String readTownsFileContent()throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (townRepository.count() == 0) {

            ImportTownDto[] importTownDtos = this.gson.fromJson(readTownsFileContent(), ImportTownDto[].class);
            for (ImportTownDto importTownDto : importTownDtos) {
                boolean isValid = validationUtil.isValid(importTownDto);

                Town town = modelMapper.map(importTownDto, Town.class);
                Optional<Town> countryByCountryName = townRepository.findTownByName(town.getName());
                if (countryByCountryName.isPresent() || !isValid) {
                    sb.append("Invalid town\n");
                } else {
                    town.setName(importTownDto.getName());
                    town.setPopulation(importTownDto.getPopulation());
                    town.setTravelGuide(importTownDto.getTravelGuide());

                    townRepository.save(town);
                    sb.append(String.format("Successfully imported town %s - %s\n", town.getName(), town.getPopulation()));
                }
            }
        }
        return sb.toString();
    }
}
