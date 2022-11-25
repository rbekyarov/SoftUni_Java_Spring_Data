package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportAgentDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class AgentServiceImpl implements AgentService {
    private static final String AGENTS_FILE_PATH = "src/main/resources/files/json/agents.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final AgentRepository agentRepository;
    private final TownRepository townRepository;

    public AgentServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, AgentRepository agentRepository, TownRepository townRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.agentRepository = agentRepository;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return agentRepository.count()>0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files
                .readString(Path.of(AGENTS_FILE_PATH));
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (agentRepository.count() == 0) {

            ImportAgentDto[] importAgentDto = this.gson.fromJson(readAgentsFromFile(), ImportAgentDto[].class);
            for (ImportAgentDto agentDto : importAgentDto) {
                boolean isValid = validationUtil.isValid(agentDto);
                 // проверка дали вече съществува
                Agent agent = modelMapper.map(agentDto, Agent.class);
                Optional<Agent> cityByCountryName = agentRepository.findAgentByFirstName(agent.getFirstName());
                if (cityByCountryName.isPresent() || !isValid) {
                    sb.append("Invalid agent\n");
                } else {
                    //проверка по ид
                    Optional<Town> townByName= townRepository.findTownByTownName(agentDto.getTown());
                    if (townByName.isEmpty()) {
                        sb.append("Invalid agent\n");

                    }else {
                        agent.setEmail(agentDto.getEmail());
                        agent.setFirstName(agentDto.getFirstName());
                        agent.setLastName(agentDto.getLastName());
                        agent.setTown(townByName.get());

                        agentRepository.save(agent);
                        sb.append(String.format("Successfully imported agent - %s %s\n", agent.getFirstName(), agent.getLastName()));
                    }

                }
            }
        }

        return sb.toString();
    }
}
