package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferSeedRootDto;
import softuni.exam.models.entity.Offer;
import softuni.exam.models.entity.enums.ApartmentType;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.TownService;
import softuni.exam.service.OfferService;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private static final String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final ApartmentService apartmentService;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final TownService townService;
    private final AgentService agentService;

    public OfferServiceImpl(OfferRepository offerRepository,
                            ModelMapper modelMapper,
                            ApartmentService apartmentService,
                            ValidationUtil validationUtil,
                            XmlParser xmlParser,
                            TownService townService,
                            AgentService agentService) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.apartmentService = apartmentService;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.townService = townService;

        this.agentService = agentService;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files
                .readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        OfferSeedRootDto offerSeedRootDto = xmlParser
                .fromFile(OFFERS_FILE_PATH, OfferSeedRootDto.class);

        offerSeedRootDto.getOfferRootDtoList()
                .stream()
                .filter(offerSeedDto -> {
                    boolean isValid = validationUtil.isValid(offerSeedDto);

                    if (agentService.getAgentByName(offerSeedDto.getName().getName()) == null) {
                        isValid = false;
                    }

                    sb.append(isValid
                            ? String.format("Successfully import offer %.2f", offerSeedDto.getPrice())
                            : "Invalid offer")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(offerSeedDto -> {
                    Offer offer = modelMapper.map(offerSeedDto, Offer.class);
                    offer.setAgent(agentService.getAgentByName(offerSeedDto.getName().getName()));
                    offer.setApartment(apartmentService.findById(offerSeedDto.getApartment().getId()));

                    return offer;
                })
                .forEach(offerRepository::save);

        return sb.toString();
    }

    @Override
    public String getOffersOrderByAreaThenPrice() {
        StringBuilder sb = new StringBuilder();

        List<Offer> offerListThreeRooms = offerRepository.findAllByApartment_ApartmentTypeOrderByApartment_AreaDescPriceAsc(ApartmentType.three_rooms);

        offerListThreeRooms
                .forEach(offer -> {
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
                });

        return sb.toString();
    }
}
