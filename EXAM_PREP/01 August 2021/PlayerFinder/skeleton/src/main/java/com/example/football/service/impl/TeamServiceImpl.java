package com.example.football.service.impl;

import com.example.football.models.dto.ImportTeamDto;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {
    private static final String TEAMS_FILE_PATH = "src/main/resources/files/json/teams.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private TeamRepository teamRepository;
    private TownRepository townRepository;

    public TeamServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, TeamRepository teamRepository, TownRepository townRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return teamRepository.count()>0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(Path.of(TEAMS_FILE_PATH));
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (teamRepository.count() == 0) {
            String s = readTeamsFileContent();
            ImportTeamDto[] importTeamDtos = gson.fromJson(readTeamsFileContent(), ImportTeamDto[].class);
            for (ImportTeamDto importTeamDto : importTeamDtos) {
                boolean isValid = validationUtil.isValid(importTeamDto);
                // проверка дали вече съществува
                Team team = modelMapper.map(importTeamDto, Team.class);
                String name = team.getName();
                Optional<Team> teamByName = teamRepository.findTeamByName(team.getName());
                
                if (teamByName.isPresent() || !isValid) {
                    sb.append("Invalid teamn");
                } else {
                    //проверка по ид
                    Optional<Town> townByName= townRepository.findTownByName(team.getTown().getName());
                    if (townByName.isEmpty()) {
                        sb.append("Invalid team\n");

                    }else {
                        team.setFanBase(importTeamDto.getFanBase());
                        team.setHistory(importTeamDto.getHistory());
                        team.setName(importTeamDto.getName());
                        team.setStadiumName(importTeamDto.getStadiumName());
                        team.setTown(townByName.get());


                        teamRepository.save(team);
                        sb.append(String.format("Successfully imported Team  - %s %d\n", team.getName(), team.getFanBase()));
                    }

                }
            }
        }

        return sb.toString();
    }
}
