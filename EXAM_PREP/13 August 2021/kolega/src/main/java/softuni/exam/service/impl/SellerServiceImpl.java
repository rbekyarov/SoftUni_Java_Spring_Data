package softuni.exam.service.impl;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.SellerSeedRootDtoXML;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SellerServiceImpl implements SellerService {
    private final static String SELLERS_FILE_PATH = "src/main/resources/files/xml/sellers.xml";
    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public SellerServiceImpl(SellerRepository sellerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(SELLERS_FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();
        xmlParser
                .fromFile(SELLERS_FILE_PATH, SellerSeedRootDtoXML.class)
                .getSellers()
                .stream()
                .filter(sellerSeedDtoXML -> {
                            boolean isValid = validationUtil.isValid(sellerSeedDtoXML);
                            if (isValid) {
                                builder
                                        .append(String.format("Successfully import seller %s - %s",
                                                sellerSeedDtoXML.getLastName(),
                                                sellerSeedDtoXML.getEmail()))
                                        .append(System.lineSeparator());
                            } else {
                                builder.append("Invalid seller").append(System.lineSeparator());
                            }
                            return isValid;
                        }
                )
                .map(sellerSeedDtoXML -> modelMapper.map(sellerSeedDtoXML, Seller.class))
                .forEach(sellerRepository::save);

        return builder.toString();
    }
}