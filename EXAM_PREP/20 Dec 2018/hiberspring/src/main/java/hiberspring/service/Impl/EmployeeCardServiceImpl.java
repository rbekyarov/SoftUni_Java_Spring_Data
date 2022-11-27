package hiberspring.service.Impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.ImpordEmployeeCardDto;
import hiberspring.domain.dtos.ImportTownDto;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.domain.entities.Town;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {
    private static final String EC_FILE_PATH = "src/main/resources/files/employee-cards.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
   private final EmployeeCardRepository employeeCardRepository;



    public EmployeeCardServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, EmployeeCardRepository employeeCardRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.employeeCardRepository = employeeCardRepository;
    }

    @Override
    public Boolean employeeCardsAreImported() {
        return employeeCardRepository.count()>0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return Files
                .readString(Path.of(EC_FILE_PATH));
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (employeeCardRepository.count() == 0) {

            ImpordEmployeeCardDto[] employeeCardDtos = this.gson.fromJson(readEmployeeCardsJsonFile(), ImpordEmployeeCardDto[].class);
            for (ImpordEmployeeCardDto employeeCardDto : employeeCardDtos) {
                boolean isValid = validationUtil.isValid(employeeCardDto);

                EmployeeCard employeeCard = modelMapper.map(employeeCardDto, EmployeeCard.class);
                Optional<EmployeeCard> optionalEmployeeCard = employeeCardRepository.findByNumber(employeeCardDto.getNumber());
                if (optionalEmployeeCard.isPresent() || !isValid) {
                    sb.append("Error: Invalid data.\n");

                }else {
                    employeeCardRepository.save(employeeCard);
                    sb.append(String.format("Successfully imported Employee Card %s\n", employeeCard.getNumber()));
                }
            }
        }
        return sb.toString();
    }
}
