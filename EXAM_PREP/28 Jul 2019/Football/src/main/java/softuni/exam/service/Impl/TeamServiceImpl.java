package softuni.exam.service.Impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.entities.dto.ImportTeamDto;
import softuni.exam.domain.entities.dto.ImportTeamRootDto;
import softuni.exam.domain.entities.entity.Picture;
import softuni.exam.domain.entities.entity.Team;
import softuni.exam.repository.TeamRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.TeamService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {
    private final static String TEAMS_FILE_PATH = "src/main/resources/files/xml/teams.xml";
    private final TeamRepository teamRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final PictureService pictureService;

    public TeamServiceImpl(TeamRepository teamRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil, PictureService pictureService) {
        this.teamRepository = teamRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.pictureService = pictureService;
    }

    @Override
    public String importTeams() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        if (teamRepository.count() == 0) {
            ImportTeamRootDto importTeamRootDto = xmlParser
                    .fromFile(TEAMS_FILE_PATH, ImportTeamRootDto.class);
            List<ImportTeamDto> importTeamDtos = importTeamRootDto.getImportTeamDto();
            for (ImportTeamDto teamDto : importTeamDtos) {

                boolean isValid = validationUtil.isValid(teamDto);

                Optional<Picture> picture = pictureService.findByUrl(teamDto.getPicture().getUrl());

                if (!isValid || picture.isEmpty()) {
                    sb.append("Invalid team\n");
                } else {
                    Team team = modelMapper.map(teamDto, Team.class);

                    team.setPicture(picture.get());
                    teamRepository.save(team);

                    sb.append(String.format("Successfully imported - %s\n", team.getName()));
                }
            }
        }
        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return Files
                .readString(Path.of(TEAMS_FILE_PATH));
    }

    @Override
    public Optional<Team> findByName(String name) {
        return teamRepository.findByName(name);
    }
}
