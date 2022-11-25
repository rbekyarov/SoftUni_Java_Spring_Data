package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentsSeedDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.repository.AgentRepository;
import softuni.exam.service.TownService;
import softuni.exam.service.AgentService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class AgentServiceImpl implements AgentService {

    private static final String AGENTS_FILE_PATH = "src/main/resources/files/json/agents.json";

    private final AgentRepository agentRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public AgentServiceImpl(AgentRepository agentRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.agentRepository = agentRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files
                .readString(Path.of(AGENTS_FILE_PATH));

    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson
                .fromJson(readAgentsFromFile(), AgentsSeedDto[].class))
                .filter(agentsSeedDto -> {
                    boolean isValid = validationUtil.isValid(agentsSeedDto);

                    boolean doesntExist = agentRepository.findAgentByFirstName(agentsSeedDto.getFirstName()).isEmpty();
                    if (!doesntExist){
                        isValid = false;
                    }

                    sb
                            .append(isValid
                                    ? String.format("Successfully import agent - %s %s",
                                    agentsSeedDto.getFirstName(), agentsSeedDto.getLastName())
                                    : "Invalid agent")
                            .append(System.lineSeparator());


                    return isValid;
                })
                .map(agentsSeedDto -> {
                    Agent agent = modelMapper.map(agentsSeedDto, Agent.class);
                    agent.setTown(townService.findTownByName(agentsSeedDto.getTown()));
                    return agent;
                })
                .forEach(agentRepository::save);

        return sb.toString();
    }

    @Override
    public Agent getAgentByName(String name) {
        return agentRepository.findAgentByFirstName(name).orElse(null);
    }
}
