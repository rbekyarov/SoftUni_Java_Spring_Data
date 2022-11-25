package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferSeedRootDtoXML;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class OfferServiceImpl implements OfferService {

    private final static String OFFER_PATH_FILE = "src/main/resources/files/xml/offers.xml";
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFER_PATH_FILE));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();
        xmlParser
                .fromFile(OFFER_PATH_FILE, OfferSeedRootDtoXML.class)
                .getOffers()
                .stream()
                .filter(offerSeedDtoXML -> {
                            boolean isValid = validationUtil.isValid(offerSeedDtoXML);
                            if (isValid) {
                                builder
                                        .append(String.format("Successfully import offer %s - %s",
                                                offerSeedDtoXML.getAddedOn(),
                                                offerSeedDtoXML.isHasGoldStatus())).append(System.lineSeparator());
                            } else {
                                builder.append("Invalid offer").append(System.lineSeparator());
                            }
                            return isValid;
                        }
                )
                .map(offerSeedDtoXML -> modelMapper.map(offerSeedDtoXML, Offer.class))
                .forEach(offerRepository::save);
        System.out.println();
        return builder.toString();
    }
}
