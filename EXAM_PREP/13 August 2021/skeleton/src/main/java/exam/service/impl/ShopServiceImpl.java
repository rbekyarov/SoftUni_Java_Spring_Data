package exam.service.impl;

import exam.model.dto.ImportShopDto;
import exam.model.dto.ImportShopRootDto;
import exam.model.entity.Shop;
import exam.model.entity.Town;
import exam.repository.ShopRepository;
import exam.repository.TownRepository;
import exam.service.ShopService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService {
    private final static String SHOPS_FILE_PATH = "src/main/resources/files/xml/shops.xml";
    private final TownRepository townRepository;
    private final ShopRepository shopRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public ShopServiceImpl(TownRepository townRepository, ShopRepository shopRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.shopRepository = shopRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(Path.of(SHOPS_FILE_PATH));
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        if (shopRepository.count() == 0) {
            ImportShopRootDto importShopRootDto = xmlParser
                    .fromFile(SHOPS_FILE_PATH, ImportShopRootDto.class);
            List<ImportShopDto> importShopDtos = importShopRootDto.getImportShopDtos();
            for (ImportShopDto shopDto : importShopDtos) {

                boolean isValid = validationUtil.isValid(shopDto);

                if (townRepository.findTownByName(shopDto.getTown().getName()).isEmpty()) {
                    isValid = false;
                }
                Optional<Shop> byName = shopRepository.findByName(shopDto.getName());
                if (byName.isPresent()) {
                    isValid = false;
                }

                if (!isValid) {
                    sb.append("Invalid shop\n");
                } else {
                    Shop shop = modelMapper.map(shopDto, Shop.class);
                    shop.setAddress(shopDto.getAddress());
                    shop.setShopArea(shopDto.getShopArea());
                    shop.setName(shopDto.getName());
                    shop.setIncome(shopDto.getIncome());
                    shop.setEmployeeCount(shopDto.getEmployeeCount());

                    Optional<Town> townByName = townRepository.findTownByName(shopDto.getTown().getName());
                    shop.setTown(townByName.get());

                    shopRepository.save(shop);
                    sb.append(String.format("Successfully imported Shop %s - %d \n", shop.getName(),shop.getIncome()));
                }
            }
        }

        return sb.toString();
    }
}
