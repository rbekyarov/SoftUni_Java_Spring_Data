package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportApartmentDto;
import softuni.exam.models.dto.ImportApartmentRootDto;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private final static String APARTMENTS_FILE_PATH = "src/main/resources/files/xml/apartments.xml";
    private final ApartmentRepository apartmentRepository;
    private final TownRepository townRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, TownRepository townRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.apartmentRepository = apartmentRepository;
        this.townRepository = townRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return apartmentRepository.count()>0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files
                .readString(Path.of(APARTMENTS_FILE_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        if (apartmentRepository.count() == 0) {
            List<ImportApartmentDto> apartmentDto =
                    xmlParser.fromFile(APARTMENTS_FILE_PATH, ImportApartmentRootDto.class)
                            .getImportApartmentDtoList();

            for (ImportApartmentDto apartmentsDto : apartmentDto) {
                boolean isValid = validationUtil.isValid(apartmentsDto);
                Apartment apartment = modelMapper.map(apartmentsDto, Apartment.class);
                Optional<Town> town = townRepository.findTownByTownName(apartmentsDto.getTown());

                if (town.isEmpty() || !isValid) {
                    sb.append("Invalid apartment\n");
                } else {
                    apartment.setApartmentType(apartmentsDto.getApartmentType());
                    apartment.setArea(apartmentsDto.getArea());

                    apartment.setTown(town.get());

                    apartmentRepository.save(apartment);
                    sb.append(String.format("Successfully imported apartment %s - %.2f\n", apartment.getApartmentType(), apartment.getArea()));
                }
            }
        }

        return sb.toString();
    }
}
