package com.example.football.service.impl;

import com.example.football.models.dto.ImportPlayerDto;
import com.example.football.models.dto.ImportPlayerRootDto;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final static String PLAYERS_FILE_PATH = "src/main/resources/files/xml/players.xml";
    private final StatRepository statRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final TownRepository townRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public PlayerServiceImpl(StatRepository statRepository, PlayerRepository playerRepository, TeamRepository teamRepository, TownRepository townRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.statRepository = statRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return playerRepository.count()>0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        if (playerRepository.count() == 0) {
            ImportPlayerRootDto importPlayerRootDto = xmlParser
                    .fromFile(PLAYERS_FILE_PATH, ImportPlayerRootDto.class);
            List<ImportPlayerDto> importPlayerDtos = importPlayerRootDto.getImportPlayerDtos();
            for (ImportPlayerDto playerDto : importPlayerDtos) {

                boolean isValid = validationUtil.isValid(playerDto);

                if (townRepository.findTownByName(playerDto.getTown().getName()).isEmpty()) {
                    isValid = false;
                }
                if (teamRepository.findTeamByName(playerDto.getTeam().getName()).isEmpty()) {
                    isValid = false;
                }
                if (statRepository.findById(playerDto.getStatId().getId()).isEmpty()) {
                    isValid = false;
                }
                if (!isValid) {
                    sb.append("Invalid Player\n");
                } else {
                    Player player = modelMapper.map(playerDto, Player.class);
                    player.setFirstName(player.getFirstName());
                    player.setLastName(player.getLastName());

                    player.setEmail(player.getEmail());
                    player.setPosition(player.getPosition());

                    Optional<Stat> startById = statRepository.findById(playerDto.getStatId().getId());
                    player.setStat(startById.get());

                    Optional<Town> townByName = townRepository.findTownByName(playerDto.getTown().getName());
                    player.setTown(townByName.get());

                    Optional<Team> teamByName = teamRepository.findTeamByName(playerDto.getTeam().getName());
                    player.setTeam(teamByName.get());


                    String birthDate = playerDto.getBirthDate();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate localDate = LocalDate.parse(birthDate, formatter);
                    player.setBirthDate(localDate);

                    playerRepository.save(player);
                    sb.append(String.format("Successfully imported Player %s %s - %s\n", player.getFirstName(),player.getLastName(), player.getPosition()));
                }
            }
        }

        return sb.toString();
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder sb = new StringBuilder();

        String start = "01/01/1995";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDateStart = LocalDate.parse(start, formatter);

        String stop = "01/01/2003";
        LocalDate localDateStop = LocalDate.parse(stop, formatter);


        List<Player> players = playerRepository.export(localDateStart, localDateStop );
        for (Player player : players) {
            sb.append(String.format("Player - %s %s%n" +
                                    "    Position - %s%n" +
                                    "    Team - %s%n" +
                                    "    Stadium - %s",
                            player.getFirstName(),
                            player.getLastName(),
                            player.getPosition(),
                            player.getTeam().getName(),
                            player.getTeam().getStadiumName()))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }
}
