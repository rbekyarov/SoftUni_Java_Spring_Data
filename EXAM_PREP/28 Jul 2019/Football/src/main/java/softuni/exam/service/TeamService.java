package softuni.exam.service;

import softuni.exam.domain.entities.entity.Team;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Optional;

public interface TeamService {

    String importTeams() throws JAXBException;

    boolean areImported();

    String readTeamsXmlFile() throws IOException;

    Optional<Team> findByName(String name);
}
