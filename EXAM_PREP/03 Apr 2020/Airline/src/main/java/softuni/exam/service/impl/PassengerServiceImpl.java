package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportPassengerDto;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Ticket;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class PassengerServiceImpl implements PassengerService {
    private static final String PASSENGERS_FILE_PATH = "src/main/resources/files/json/passengers.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private TownRepository townRepository;
    private PassengerRepository passengerRepository;
    private TicketRepository ticketRepository;

    public PassengerServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, TownRepository townRepository, PassengerRepository passengerRepository, TicketRepository ticketRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
        this.passengerRepository = passengerRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public boolean areImported() {
        return passengerRepository.count()>0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGERS_FILE_PATH));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (passengerRepository.count() == 0) {

            ImportPassengerDto[] importUserDtos = gson.fromJson(readPassengersFileContent(), ImportPassengerDto[].class);
            for (ImportPassengerDto passengerDto : importUserDtos) {
                boolean isValid = validationUtil.isValid(passengerDto);
                // проверка дали вече съществува
                Passenger passenger = modelMapper.map(passengerDto, Passenger.class);

                Optional<Passenger> passengerOptional = passengerRepository.findByEmail(passenger.getEmail());

                if (passengerOptional.isPresent() || !isValid) {
                    sb.append("Invalid Passenger\n");
                } else {
                    //проверка по town name
                    Optional<Town> town = townRepository.findByName(passengerDto.getTown());
                    if (town.isEmpty()) {
                        sb.append("Invalid Passenger\n");

                    } else {
                        passenger.setAge(passengerDto.getAge());
                        passenger.setEmail(passengerDto.getEmail());
                        passenger.setFirstName(passengerDto.getFirstName());
                        passenger.setLastName(passengerDto.getLastName());
                        passenger.setPhoneNumber(passengerDto.getPhoneNumber());

                        passenger.setTown(town.get());

                        passengerRepository.save(passenger);
                        sb.append(String.format("Successfully imported Passenger %s - %s\n", passenger.getLastName(),passenger.getEmail()));
                    }

                }
            }
        }

        return sb.toString();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();
        List<Passenger> passengers = passengerRepository.getPassengers();
        for (Passenger passenger : passengers) {
            Long count = ticketRepository.find(passenger.getId());

            sb.append(String.format("Passenger %s  %s\n" +
                                    "   Email - %s\n" +
                                    "   Phone - %s\n" +
                                    "   Number of tickets - %d\n",
                                        passenger.getFirstName(),
                                        passenger.getLastName(),
                                        passenger.getEmail(),
                                        passenger.getPhoneNumber(),
                    count));

        }
        return sb.toString();

    }
}
