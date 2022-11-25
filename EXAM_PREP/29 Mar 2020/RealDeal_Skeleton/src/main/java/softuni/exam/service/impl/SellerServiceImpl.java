package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportSellerDto;
import softuni.exam.models.dto.ImportSellerRootDto;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {
    private final static String SELLERS_FILE_PATH = "src/main/resources/files/xml/sellers.xml";
    private final SellerRepository sellerRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public SellerServiceImpl(SellerRepository sellerRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.sellerRepository = sellerRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return sellerRepository.count()>0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files
                .readString(Path.of(SELLERS_FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        if (sellerRepository.count() ==0) {
            List<ImportSellerDto> importSellerDtos =
                    xmlParser.fromFile(SELLERS_FILE_PATH, ImportSellerRootDto.class)
                            .getImportSellerDtos();

            for (ImportSellerDto sellerDto : importSellerDtos) {
                boolean isValid = validationUtil.isValid(sellerDto);
                Seller seller = modelMapper.map(sellerDto, Seller.class);
                Optional<Seller> sellerUnique = sellerRepository.findByEmail(sellerDto.getEmail());


                String rating = sellerDto.getRating();
                if(rating==null){
                    isValid = false;
                }
                assert rating != null;
                if(!rating.equals("GOOD")&&!rating.equals("BAD")&&!rating.equals("UNKNOWN")){
                    isValid = false;
                }

                if (sellerUnique.isPresent() || !isValid) {
                    sb.append("Invalid seller\n");
                } else {
                    seller.setEmail(sellerDto.getEmail());
                    seller.setFirstName(sellerDto.getFirstName());
                    seller.setLastName(sellerDto.getLastName());

                    seller.setTown(sellerDto.getTown());

                    sellerRepository.save(seller);
                    sb.append(String.format("Successfully import seller %s - %s\n", seller.getLastName(), seller.getEmail()));
                }
            }
        }

        return sb.toString();
    }
}
