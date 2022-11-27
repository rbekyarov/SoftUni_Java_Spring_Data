package hiberspring.service.Impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.ImportBranchDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.service.BranchService;
import hiberspring.service.TownService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class BranchServiceImpl implements BranchService {
    private static final String BRANCHES_FILE_PATH = "src/main/resources/files/branches.json";
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final TownService townService;
    private final BranchRepository branchRepository;

    public BranchServiceImpl(ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, TownService townService, BranchRepository branchRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.townService = townService;
        this.branchRepository = branchRepository;
    }


    @Override
    public Boolean branchesAreImported() {
        return branchRepository.count() > 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return Files
                .readString(Path.of(BRANCHES_FILE_PATH));
    }

    @Override
    public String importBranches(String branchesFileContent) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (branchRepository.count() == 0) {

            ImportBranchDto[] importBranchDtos = this.gson.fromJson(readBranchesJsonFile(), ImportBranchDto[].class);
            for (ImportBranchDto branchDto : importBranchDtos) {
                boolean isValid = validationUtil.isValid(branchDto);

                Branch branch = modelMapper.map(branchDto, Branch.class);
                Optional<Branch> branchByName = branchRepository.findByName(branchDto.getName());
                Optional<Town> townByName = townService.findByName(branchDto.getTown());
                if (branchByName.isPresent() || !isValid || townByName.isEmpty()) {
                    sb.append("Error: Invalid data.\n");

                } else {
                    branch.setTown(townByName.get());
                    branchRepository.save(branch);
                    sb.append(String.format("branch %s.\n", branch.getName()));
                }
            }
        }
        return sb.toString();
    }
}
