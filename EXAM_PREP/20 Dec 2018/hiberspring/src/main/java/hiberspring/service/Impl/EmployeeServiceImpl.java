package hiberspring.service.Impl;

import hiberspring.domain.dtos.ImportEmployeeDto;
import hiberspring.domain.dtos.ImportEmployeeRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.EmployeeService;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final static String EMPLOYEES_FILE_PATH = "src/main/resources/files/employees.xml";
    private final EmployeeRepository employeeRepository;
    private final EmployeeCardRepository employeeCardRepository;
    private final BranchRepository branchRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeCardRepository employeeCardRepository, BranchRepository branchRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.employeeRepository = employeeRepository;
        this.employeeCardRepository = employeeCardRepository;
        this.branchRepository = branchRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean employeesAreImported() {
        return employeeRepository.count()>0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return Files
                .readString(Path.of(EMPLOYEES_FILE_PATH));
    }

    @Override
    public String importEmployees() throws JAXBException, IOException {
        StringBuilder sb = new StringBuilder();
        if (employeeRepository.count() == 0) {
            ImportEmployeeRootDto importEmployeeRootDto = xmlParser
                    .fromFile(EMPLOYEES_FILE_PATH, ImportEmployeeRootDto.class);
            List<ImportEmployeeDto> importEmployeeDtos = importEmployeeRootDto.getImportEmployeeDtos();
            for (ImportEmployeeDto employeeDto : importEmployeeDtos) {

                boolean isValid = validationUtil.isValid(employeeDto);

                if (branchRepository.findByName(employeeDto.getBranch()).isEmpty()) {
                    isValid = false;
                }
                if (employeeCardRepository.findByNumber(employeeDto.getEmployeeCard()).isEmpty()) {
                    isValid = false;
                }
                if (!isValid) {
                    sb.append("Invalid employee\n");
                } else {
                    Employee employee = modelMapper.map(employeeDto, Employee.class);


                    Optional<Branch> branch = branchRepository.findByName(employeeDto.getBranch());
                    employee.setBranch(branch.get());

                    Optional<EmployeeCard> employeeCard = employeeCardRepository.findByNumber(employeeDto.getEmployeeCard());
                    employee.setEmployeeCard(employeeCard.get());

                    employeeRepository.save(employee);
                    sb.append(String.format("Successfully imported Employee %s %s.\n", employee.getFirstName(),employee.getLastName()));
                }
            }
        }

        return sb.toString();
    }

    @Override
    public String exportProductiveEmployees() {
        StringBuilder sb = new StringBuilder();
        List<Employee> employees = employeeRepository.export();
        for (Employee employee : employees) {
            sb.append(String.format("Name: %s %s\n" +
                                    "Position: %s\n" +
                                    "Card number: %s\n"+
                              "----------------------------\n",
                    employee.getFirstName(),employee.getLastName(),
                    employee.getPosition(),employee.getEmployeeCard().getNumber()));
        }
        return sb.toString();
    }
}
