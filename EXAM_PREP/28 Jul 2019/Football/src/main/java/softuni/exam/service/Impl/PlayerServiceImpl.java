package softuni.exam.service.Impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.entities.dto.ImportPlayerDto;
import softuni.exam.domain.entities.entity.Picture;
import softuni.exam.domain.entities.entity.Player;
import softuni.exam.domain.entities.entity.Team;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.PlayerService;
import softuni.exam.service.TeamService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    private static final String PLAYERS_FILE_PATH = "src/main/resources/files/json/players.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private TeamService teamService;
    private PictureService pictureService;
    private PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, TeamService teamService, PictureService pictureService, PlayerRepository playerRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.teamService = teamService;
        this.pictureService = pictureService;
        this.playerRepository = playerRepository;
    }

    public PlayerServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public String importPlayers() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (playerRepository.count() == 0) {

            ImportPlayerDto[] importPlayerDtos = gson.fromJson(readPlayersJsonFile(), ImportPlayerDto[].class);
            for (ImportPlayerDto playerDto : importPlayerDtos) {
                boolean isValid = validationUtil.isValid(playerDto);

                Player player = modelMapper.map(playerDto, Player.class);

                Optional<Team> team = teamService.findByName(playerDto.getTeam().getName());
                Optional<Picture> picture = pictureService.findByUrl(playerDto.getPicture().getUrl());
                Optional<Player> player1 = playerRepository.findByLastName(playerDto.getLastName());

                if (player1.isPresent() || !isValid || team.isEmpty() || picture.isEmpty()) {
                    sb.append("Invalid player\n");
                } else {
                    player.setTeam(team.get());
                    player.setPicture(picture.get());

                    playerRepository.save(player);
                    sb.append(String.format("Successfully imported player: %s %s\n", player.getFirstName(), player.getLastName()));
                }

            }
        }


        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder sb = new StringBuilder();
        BigDecimal salary = new BigDecimal(100000);
        List<Player> players = playerRepository.findAllBySalary(salary);
        for (Player player : players) {
            sb.append(String.format("Player name: %s %s\n" +
                            "     Number: %d\n" +
                            "     Salary: %f\n" +
                            "     Team: %s\n",
                    player.getFirstName(), player.getLastName(),
                    player.getNumber(),
                    player.getSalary(),
                    player.getTeam().getName()
            ));
        }
        return sb.toString();
    }

    @Override
    public String exportPlayersInATeam() {
        String teamName = "North Hub";
        StringBuilder sb = new StringBuilder();
        List<Player> northHubPlayers = playerRepository.findAllByTeamIs(teamService.findByName(teamName));
        sb.append(String.format("Team: %s\n", teamName));
        for (Player player : northHubPlayers) {
            sb.append(String.format(
                    "     Player name: %s %s - %s\n" +
                            "     Number: %d\n",
                    player.getFirstName(),
                    player.getLastName(), player.getPosition(),
                    player.getNumber()
            ));
        }
        return sb.toString();
    }
}
