package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportTicketDto;
import softuni.exam.models.dto.ImportTicketRootDto;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Plane;
import softuni.exam.models.entity.Ticket;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {
    private final static String TICKETS_FILE_PATH = "src/main/resources/files/xml/tickets.xml";
    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final PlaneRepository planeRepository;
    private final TownRepository townRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public TicketServiceImpl(TicketRepository ticketRepository, PassengerRepository passengerRepository, PlaneRepository planeRepository, TownRepository townRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.ticketRepository = ticketRepository;
        this.passengerRepository = passengerRepository;
        this.planeRepository = planeRepository;
        this.townRepository = townRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return ticketRepository.count()>0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKETS_FILE_PATH));
    }

    @Override
    public String importTickets() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        if (ticketRepository.count() == 0) {
            ImportTicketRootDto importTicketRootDto = xmlParser
                    .fromFile(TICKETS_FILE_PATH, ImportTicketRootDto.class);
            List<ImportTicketDto> importTicketDtos = importTicketRootDto.getImportTicketDtos();
            for (ImportTicketDto ticketDto : importTicketDtos) {

                boolean isValid = validationUtil.isValid(ticketDto);

                if (passengerRepository.findByEmail(ticketDto.getPassenger().getEmail()).isEmpty()) {
                    isValid = false;
                }
                if (planeRepository.findByRegisterNumber(ticketDto.getPlane().getRegisterNumber()).isEmpty()) {
                    isValid = false;
                }
                if (townRepository.findByName(ticketDto.getFromTown().getName()).isEmpty()) {
                    isValid = false;
                }
                if (townRepository.findByName(ticketDto.getToTown().getName()).isEmpty()) {
                    isValid = false;
                }
                Optional<Ticket> byCaption = ticketRepository.findBySerialNumber(ticketDto.getSerialNumber());
                if (byCaption.isPresent()) {
                    isValid = false;
                }

                if (!isValid) {
                    sb.append("Invalid Ticket\n");
                } else {
                    Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
                    ticket.setSerialNumber(ticketDto.getSerialNumber());
                    ticket.setPrice(ticketDto.getPrice());

                    String takeoff = ticketDto.getTakeoff();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(takeoff, formatter);
                    ticket.setTakeoff(dateTime);

                    Optional<Passenger> passenger = passengerRepository.findByEmail(ticketDto.getPassenger().getEmail());
                    ticket.setPassenger(passenger.get());

                    Optional<Plane> plane = planeRepository.findByRegisterNumber(ticketDto.getPlane().getRegisterNumber());
                    ticket.setPlane(plane.get());

                    Optional<Town> getFromTown = townRepository.findByName(ticketDto.getFromTown().getName());
                    ticket.setFromTown(getFromTown.get());

                    Optional<Town> getToTown = townRepository.findByName(ticketDto.getToTown().getName());
                    ticket.setToTown(getToTown.get());

                    ticketRepository.save(ticket);
                    sb.append(String.format("Successfully imported Ticket %s - %s\n", ticket.getFromTown().getName(),ticket.getToTown().getName()));
                }
            }
        }

        return sb.toString();
    }
}
