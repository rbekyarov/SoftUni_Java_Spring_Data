package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.ImportLaptopDto;
import exam.model.entity.Laptop;
import exam.model.entity.Shop;
import exam.repository.LaptopRepository;
import exam.repository.ShopRepository;

import exam.service.LaptopService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class LaptopServiceImpl implements LaptopService {
    private static final String LAPTOPS_FILE_PATH = "src/main/resources/files/json/laptops.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private ShopRepository shopRepository;
    private LaptopRepository laptopRepository;

    public LaptopServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, ShopRepository shopRepository, LaptopRepository laptopRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.shopRepository = shopRepository;
        this.laptopRepository = laptopRepository;
    }

    @Override
    public boolean areImported() {
        return laptopRepository.count()>0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(Path.of(LAPTOPS_FILE_PATH));
    }

    @Override
    public String importLaptops() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (laptopRepository.count() == 0) {

            ImportLaptopDto[] importLaptopDtos = gson.fromJson(readLaptopsFileContent(), ImportLaptopDto[].class);
            for (ImportLaptopDto laptopDto : importLaptopDtos) {
                boolean isValid = validationUtil.isValid(laptopDto);

                Laptop laptop = modelMapper.map(laptopDto, Laptop.class);

                Optional<Laptop> laptopByMacAddress = laptopRepository.findByMacAddress(laptop.getMacAddress());

                if (laptopByMacAddress.isPresent() || !isValid) {
                    sb.append("Invalid Laptop\n");
                } else {

                    Optional<Shop> shopByName= shopRepository.findByName(laptopDto.getShop().getName());
                    if (shopByName.isEmpty()) {
                        sb.append("Invalid Laptop\n");

                    }else {
                        laptop.setCpuSpeed(laptopDto.getCpuSpeed());
                        laptop.setDescription(laptopDto.getDescription());
                        laptop.setMacAddress(laptopDto.getMacAddress());
                        laptop.setPrice(laptopDto.getPrice());
                        laptop.setRam(laptopDto.getRam());
                        laptop.setStorage(laptopDto.getStorage());


                        laptop.setShop(shopByName.get());
                        laptopRepository.save(laptop);
                        sb.append(String.format("Successfully imported Laptop %s - %.2f - %d - %d\n", laptop.getMacAddress(),laptop.getCpuSpeed(), laptop.getRam(),laptop.getStorage()));
                    }

                }
            }
        }

        return sb.toString();
    }

    @Override
    public String exportBestLaptops() {
        StringBuilder sb = new StringBuilder();

        List<Laptop> laptops = laptopRepository.export();
        for (Laptop laptop : laptops) {
            sb.append(String.format("Laptop - %s%n" +
                                    "*Cpu speed - %.2f%n" +
                                    "**Ram - %d%n" +
                                    "***Storage - %d%n" +
                                    "****Price - %.2f%n" +
                                    "#Shop name - %s%n" +
                                    "##Town - %s%n",
                            laptop.getMacAddress(),
                            laptop.getCpuSpeed(),
                            laptop.getRam(),
                            laptop.getStorage(),
                            laptop.getPrice(),
                            laptop.getShop().getName(),
                            laptop.getShop().getTown().getName()))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }
}
