package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportOfferDto;
import softuni.exam.models.dto.ImportOfferRootDto;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Offer;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {
    private final static String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";
    private final CarRepository carRepository;
    private final SellerRepository sellerRepository;
    private final OfferRepository offerRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public OfferServiceImpl(CarRepository carRepository, SellerRepository sellerRepository, OfferRepository offerRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.carRepository = carRepository;
        this.sellerRepository = sellerRepository;
        this.offerRepository = offerRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
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

                if (carRepository.findById(offerDto.getCar().getId()).isEmpty()) {
                    isValid = false;
                }
                if (sellerRepository.findById(offerDto.getSeller().getId()).isEmpty()) {
                    isValid = false;
                }
                Optional<Offer> offerOptional = offerRepository.getUnique(offerDto.getDescription()+offerDto.getAddedOn());
                if (offerOptional.isPresent()) {
                    isValid = false;
                }

                if (!isValid) {
                    sb.append("Invalid offer\n");
                } else {
                    Offer offer = modelMapper.map(offerDto, Offer.class);
                    offer.setPrice(offerDto.getPrice());
                    offer.setDescription(offerDto.getDescription());
                    offer.setHasGoldStatus(offerDto.getHasGoldStatus());

                    String registeredOn = offerDto.getAddedOn();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime localDate = LocalDateTime.parse(registeredOn, formatter);
                    offer.setAddedOn(localDate);


                    Optional<Car> car = carRepository.findById(offerDto.getCar().getId());
                    offer.setCar(car.get());

                    Optional<Seller> seller = sellerRepository.findById(offerDto.getSeller().getId());
                    offer.setSeller(seller.get());

                    offerRepository.save(offer);
                    sb.append(String.format("Successfully import offer %s - %s\n", offer.getAddedOn(),offer.getHasGoldStatus()));
                }
            }
        }

        return sb.toString();
    }
}
