package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import softuni.exam.models.dto.ImportOfferDto;
import softuni.exam.models.dto.ImportOfferRootDto;
import softuni.exam.models.entity.*;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {
    private static final String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";
    private final OfferRepository offerRepository;
    private final TownRepository townRepository;
    private final ApartmentRepository apartmentRepository;
    private final AgentRepository agentRepository;
    private final ModelMapper modelMapper;
    private final ApartmentService apartmentService;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public OfferServiceImpl(OfferRepository offerRepository, TownRepository townRepository, ApartmentRepository apartmentRepository, AgentRepository agentRepository, ModelMapper modelMapper, ApartmentService apartmentService, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.offerRepository = offerRepository;
        this.townRepository = townRepository;
        this.apartmentRepository = apartmentRepository;
        this.agentRepository = agentRepository;
        this.modelMapper = modelMapper;
        this.apartmentService = apartmentService;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count()>0;
    }


    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        if (offerRepository.count() == 0) {
            ImportOfferRootDto importOfferRootDto = xmlParser
                    .fromFile(OFFERS_FILE_PATH, ImportOfferRootDto.class);
            List<ImportOfferDto> importOfferDto = importOfferRootDto.getImportOfferDto();
            for (ImportOfferDto offerDto : importOfferDto) {

                boolean isValid = validationUtil.isValid(offerDto);

                if (agentRepository.findAgentByFirstName(offerDto.getName().getName()).isEmpty()) {
                    isValid = false;
                }
                if (apartmentRepository.findById(offerDto.getApartment().getId()).isEmpty()) {
                    isValid = false;
                }
                if (!isValid) {
                    sb.append("Invalid apartment\n");
                } else {
                    Offer offer = modelMapper.map(offerDto, Offer.class);

                    offer.setPrice(offerDto.getPrice());

                    String publishedOn = offerDto.getPublishedOn();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate localDate = LocalDate.parse(publishedOn, formatter);
                    offer.setPublishedOn(localDate);

                    Optional<Agent> agentByFirstName = agentRepository.findAgentByFirstName(offerDto.getName().getName());
                    offer.setAgent(agentByFirstName.get());

                    Optional<Apartment> apartamentById = apartmentRepository.findById(offerDto.getApartment().getId());
                    offer.setApartment(apartamentById.get());

                    offerRepository.save(offer);
                    sb.append(String.format("Successfully imported offer %.2f\n", offer.getPrice()));
                }
            }
        }

        return sb.toString();
    }

    @Override
    public String exportOffers() {
        StringBuilder sb = new StringBuilder();
        List<Offer>offers = offerRepository.export(ApartmentType.three_rooms);
        for (Offer offer : offers) {
            sb.append(String.format("Agent %s %s with offer №%d:%n" +
                                    "   -Apartment area: %.2f%n" +
                                    "   --Town: %s%n" +
                                    "   ---Price: %.2f$",
                            offer.getAgent().getFirstName(),
                            offer.getAgent().getLastName(),
                            offer.getId(),
                            offer.getApartment().getArea(),
                            offer.getApartment().getTown().getTownName(),
                            offer.getPrice()))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }
}
