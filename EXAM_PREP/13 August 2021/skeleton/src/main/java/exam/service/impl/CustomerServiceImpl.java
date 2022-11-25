package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.ImportCustomerDto;
import exam.model.entity.Customer;
import exam.model.entity.Town;
import exam.repository.CustomerRepository;
import exam.repository.TownRepository;
import exam.service.CustomerService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final String CUSTOMERS_FILE_PATH = "src/main/resources/files/json/customers.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private CustomerRepository customerRepository;
    private TownRepository townRepository;

    public CustomerServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, CustomerRepository customerRepository, TownRepository townRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.customerRepository = customerRepository;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return customerRepository.count()>0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(Path.of(CUSTOMERS_FILE_PATH));
    }

    @Override
    public String importCustomers() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (customerRepository.count() == 0) {

            ImportCustomerDto[] importCustomerDtos = gson.fromJson(readCustomersFileContent(), ImportCustomerDto[].class);
            for (ImportCustomerDto importCustomerDto : importCustomerDtos) {
                boolean isValid = validationUtil.isValid(importCustomerDto);
                // проверка дали вече съществува
                Customer customer = modelMapper.map(importCustomerDto, Customer.class);

                Optional<Customer> customerByName = customerRepository.findByFirstName(customer.getFirstName());

                if (customerByName.isPresent() || !isValid) {
                    sb.append("Invalid Customer\n");
                } else {
                    //проверка по town name
                    Optional<Town> townByName= townRepository.findTownByName(importCustomerDto.getTown().getName());
                    if (townByName.isEmpty()) {
                        sb.append("Invalid Customer\n");

                    }else {
                        customer.setEmail(importCustomerDto.getEmail());
                        customer.setFirstName(importCustomerDto.getFirstName());
                        customer.setLastName(importCustomerDto.getLastName());

                        String registeredOn = importCustomerDto.getRegisteredOn();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate localDate = LocalDate.parse(registeredOn, formatter);
                        customer.setRegisteredOn(localDate);


                        customer.setTown(townByName.get());


                        customerRepository.save(customer);
                        sb.append(String.format("Successfully imported Customer %s %s - %s\n", customer.getFirstName(),customer.getLastName(), customer.getEmail()));
                    }

                }
            }
        }

        return sb.toString();
    }
}
